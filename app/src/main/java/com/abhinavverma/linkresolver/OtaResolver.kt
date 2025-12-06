package com.abhinavverma.linkresolver

import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

object OtaResolver {

    private val client = OkHttpClient.Builder()
        .followRedirects(true)
        .followSslRedirects(true)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    fun resolveDownloadCheck(
        originalUrl: String,
        callback: (Boolean, String) -> Unit
    ) {

        if (!originalUrl.startsWith("http")) {
            callback(false, "Invalid URL")
            return
        }

        val cleanUrl = originalUrl.replace("\\u0026", "&")

        val request = Request.Builder()
            .url(cleanUrl)
            .addHeader("userId", "oplus-ota|16002018")
            .addHeader("User-Agent", "okhttp/3.12.12")
            .addHeader("Accept", "*/*")
            .addHeader("Accept-Encoding", "gzip, deflate")
            .addHeader("Connection", "Keep-Alive")
            .addHeader("Cache-Control", "no-cache")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                callback(false, e.message ?: "Network Error")
            }

            override fun onResponse(call: Call, response: Response) {

                val finalUrl = response.request.url.toString()
                val contentType = response.header("Content-Type") ?: ""
                val contentLength = response.header("Content-Length")?.toLongOrNull() ?: 0

                if ((contentType.contains("text/html", true)
                            || contentType.contains("application/json", true))
                    && contentLength < 1024
                ) {
                    callback(false, "Fallback URL: The resolved URL may not be a direct download link.")
                    return
                }

                if (response.isRedirect && finalUrl == originalUrl) {
                    callback(false, "Redirect loop detected")
                    return
                }

                if (finalUrl != cleanUrl) {
                    callback(true, finalUrl)
                } else {
                    callback(false, "Could not resolve URL")
                }
            }
        })
    }
}
