package com.abhinavverma.linkresolver

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.abhinavverma.linkresolver.ui.theme.OTAResolveTheme

class AboutFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                OTAResolveTheme {
                    AboutScreen()
                }
            }
        }
    }
}

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val packageInfo = remember {
        try {
            context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (_: Exception) {
            null
        }
    }

    val appIcon = remember {
        try {
            context.packageManager.getApplicationIcon(context.applicationInfo)
        } catch (_: Exception) {
            null
        }
    }

    val githubButtonInteractionSource = remember { MutableInteractionSource() }
    val isGithubButtonPressed by githubButtonInteractionSource.collectIsPressedAsState()
    val githubButtonScale by animateFloatAsState(if (isGithubButtonPressed) 0.95f else 1f)

    val telegramButtonInteractionSource = remember { MutableInteractionSource() }
    val isTelegramButtonPressed by telegramButtonInteractionSource.collectIsPressedAsState()
    val telegramButtonScale by animateFloatAsState(if (isTelegramButtonPressed) 0.95f else 1f)

    val buyMeACoffeeButtonInteractionSource = remember { MutableInteractionSource() }
    val isBuyMeACoffeeButtonPressed by buyMeACoffeeButtonInteractionSource.collectIsPressedAsState()
    val buyMeACoffeeButtonScale by animateFloatAsState(if (isBuyMeACoffeeButtonPressed) 0.95f else 1f)

    val upiIdValue = stringResource(id = R.string.upi_id_value)
    val upiIdCopiedToast = stringResource(id = R.string.upi_id_copied_toast)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (appIcon != null) {
            Image(
                bitmap = appIcon.toBitmap().asImageBitmap(),
                contentDescription = stringResource(id = R.string.app_icon_desc),
                modifier = Modifier.size(80.dp)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(id = R.string.app_icon_desc),
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.app_name_full),
            style = MaterialTheme.typography.headlineSmall
        )

        Row(
            modifier = Modifier.padding(top = 0.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Badge {
                Text(text = "v${packageInfo?.versionName ?: "1.0"}")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Badge {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(id = R.string.made_with))
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_heart_small),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.about_card_title),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = stringResource(id = R.string.about_app_description),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val creatorTelegramIntent =
                                Intent(Intent.ACTION_VIEW, "https://t.me/abhinav_v1".toUri())
                            context.startActivity(creatorTelegramIntent)
                        },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_avatar_placeholder),
                            contentDescription = stringResource(id = R.string.creator_avatar_desc),
                            modifier = Modifier.size(48.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer)
                        )
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                text = stringResource(id = R.string.created_by_label),
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = stringResource(id = R.string.creator_name),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        onClick = {
                            val githubIntent =
                                Intent(Intent.ACTION_VIEW, "https://github.com/CodeSenseiX/Link-Resolver".toUri())
                            context.startActivity(githubIntent)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .graphicsLayer {
                                scaleX = githubButtonScale
                                scaleY = githubButtonScale
                            },
                        interactionSource = githubButtonInteractionSource,
                        shape = CircleShape,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_github),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = stringResource(id = R.string.github_button_text))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(
                        onClick = {
                            val telegramIntent = Intent(
                                Intent.ACTION_VIEW,
                                "https://t.me/OTAPulseOfficial".toUri()
                            )
                            context.startActivity(telegramIntent)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .graphicsLayer {
                                scaleX = telegramButtonScale
                                scaleY = telegramButtonScale
                            },
                        interactionSource = telegramButtonInteractionSource,
                        shape = CircleShape,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_telegram),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = stringResource(id = R.string.telegram_button_text))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.donate_card_title),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_heart_small),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Red
                    )
                }

                Button(
                    onClick = {
                        val payPalIntent = Intent(
                            Intent.ACTION_VIEW,
                            "https://paypal.me/Abhinavftp?country.x=IN&locale.x=en_GB".toUri()
                        )
                        context.startActivity(payPalIntent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            scaleX = buyMeACoffeeButtonScale
                            scaleY = buyMeACoffeeButtonScale
                        },
                    interactionSource = buyMeACoffeeButtonInteractionSource,
                    shape = CircleShape
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_buy_me_a_coffee),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(id = R.string.buy_me_a_coffee_button_text))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val clipboard =
                                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText(
                                "UPI ID",
                                upiIdValue
                            )
                            clipboard.setPrimaryClip(clip)
                            Toast
                                .makeText(
                                    context,
                                    upiIdCopiedToast,
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_upi),
                            contentDescription = null,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(id = R.string.upi_id_value),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = stringResource(id = R.string.upi_id_copy_label),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Icon(
                            painter = painterResource(id = R.drawable.ic_copy),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        Text(
            text = stringResource(id = R.string.footer_copyright),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}
