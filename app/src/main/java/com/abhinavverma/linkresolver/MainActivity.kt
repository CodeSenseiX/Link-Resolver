package com.abhinavverma.linkresolver

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.abhinavverma.linkresolver.ui.theme.OTAResolveTheme
import kotlinx.coroutines.launch

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object About : Screen("about", "About", Icons.Default.Info)
}

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            OTAResolveTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("OPLUS Link Resolver", fontWeight = FontWeight.SemiBold) }
            )
        },
        bottomBar = {
            BottomAppBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val items = listOf(Screen.Home, Screen.About)
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = { navController.navigate(screen.route) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { ResolveScreen() }
            composable(Screen.About.route) { AboutScreen() }
        }
    }
}

@Composable
fun ResolveScreen(modifier: Modifier = Modifier) {

    var inputUrl by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf<String?>(null) }
    var isSuccess by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val resolveButtonInteractionSource = remember { MutableInteractionSource() }
    val isResolveButtonPressed by resolveButtonInteractionSource.collectIsPressedAsState()
    val resolveButtonScale by animateFloatAsState(
        targetValue = if (isResolveButtonPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "ResolveButtonScale"
    )

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = inputUrl,
                onValueChange = { inputUrl = it },
                label = { Text("Paste downloadCheck link") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (inputUrl.isEmpty()) {
                            IconButton(
                                onClick = {
                                    clipboardManager.getText()?.let {
                                        inputUrl = it.text
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.Outlined.Assignment,
                                    contentDescription = "Paste"
                                )
                            }
                        }
                        if (inputUrl.isNotEmpty()) {
                            IconButton(
                                onClick = { inputUrl = "" }
                            ) {
                                Icon(
                                    Icons.Filled.Clear,
                                    contentDescription = "Clear"
                                )
                            }
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    isLoading = true
                    resultText = null

                    OtaResolver.resolveDownloadCheck(inputUrl) { success, result ->
                        isLoading = false
                        isSuccess = success
                        resultText = result
                    }
                },
                enabled = !isLoading && inputUrl.isNotBlank(),
                interactionSource = resolveButtonInteractionSource,
                modifier = Modifier.graphicsLayer {
                    scaleX = resolveButtonScale
                    scaleY = resolveButtonScale
                }
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Resolving...")
                } else {
                    Text("Resolve")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            resultText?.let { result ->
                val cardColors = if (isSuccess) {
                    CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                } else {
                    CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = cardColors
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = if (isSuccess) "✅ Resolved URL" else "❌ $result",
                            style = MaterialTheme.typography.titleSmall
                        )
                        if (isSuccess) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = result,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.align(Alignment.CenterEnd),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val shareButtonInteractionSource = remember { MutableInteractionSource() }
                                    val isShareButtonPressed by shareButtonInteractionSource.collectIsPressedAsState()
                                    val shareButtonScale by animateFloatAsState(
                                        targetValue = if (isShareButtonPressed) 0.95f else 1f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessHigh
                                        ),
                                        label = "ShareButtonScale"
                                    )

                                    val downloadButtonInteractionSource = remember { MutableInteractionSource() }
                                    val isDownloadButtonPressed by downloadButtonInteractionSource.collectIsPressedAsState()
                                    val downloadButtonScale by animateFloatAsState(
                                        targetValue = if (isDownloadButtonPressed) 0.95f else 1f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessHigh
                                        ),
                                        label = "DownloadButtonScale"
                                    )

                                    val copyButtonInteractionSource = remember { MutableInteractionSource() }
                                    val isCopyButtonPressed by copyButtonInteractionSource.collectIsPressedAsState()
                                    val copyButtonScale by animateFloatAsState(
                                        targetValue = if (isCopyButtonPressed) 0.95f else 1f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessHigh
                                        ),
                                        label = "CopyButtonScale"
                                    )

                                    OutlinedIconButton(
                                        onClick = {
                                            val sendIntent: Intent = Intent().apply {
                                                action = Intent.ACTION_SEND
                                                putExtra(Intent.EXTRA_TEXT, result)
                                                type = "text/plain"
                                            }
                                            val shareIntent = Intent.createChooser(sendIntent, null)
                                            context.startActivity(shareIntent)
                                        },
                                        interactionSource = shareButtonInteractionSource,
                                        modifier = Modifier.graphicsLayer {
                                            scaleX = shareButtonScale
                                            scaleY = shareButtonScale
                                        },
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                                    ) {
                                        Icon(Icons.Outlined.Share, "Share")
                                    }

                                    OutlinedIconButton(
                                        onClick = {
                                            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                                            val uri = Uri.parse(result)
                                            val request = DownloadManager.Request(uri)
                                            request.setTitle(uri.lastPathSegment ?: "OTA Download")
                                            request.setDescription("Downloading OTA update")
                                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.lastPathSegment)
                                            try {
                                                downloadManager.enqueue(request)
                                                Toast.makeText(context, "Download started.", Toast.LENGTH_SHORT).show()
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "Failed to start download: ${e.message}", Toast.LENGTH_LONG).show()
                                            }
                                        },
                                        interactionSource = downloadButtonInteractionSource,
                                        modifier = Modifier.graphicsLayer {
                                            scaleX = downloadButtonScale
                                            scaleY = downloadButtonScale
                                        },
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                                    ) {
                                        Icon(Icons.Filled.Download, "Download")
                                    }

                                    OutlinedIconButton(
                                        onClick = {
                                            coroutineScope.launch {
                                                clipboardManager.setText(AnnotatedString(result))
                                                Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        interactionSource = copyButtonInteractionSource,
                                        modifier = Modifier.graphicsLayer {
                                            scaleX = copyButtonScale
                                            scaleY = copyButtonScale
                                        },
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                                    ) {
                                        Icon(Icons.Outlined.ContentCopy, "Copy")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
