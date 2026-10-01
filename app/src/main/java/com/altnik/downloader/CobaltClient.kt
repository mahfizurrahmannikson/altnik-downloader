package com.altnik.downloader

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cobalt no longer provides a public API intended for third-party apps.
 * Use a Cobalt instance that you operate or one whose owner has explicitly
 * given you access. The base URL is configured by the user in the app.
 */
object CobaltClient {
    fun apiFor(serverUrl: String): CobaltApi {
        val normalizedUrl = normalizeServerUrl(serverUrl)
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(normalizedUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CobaltApi::class.java)
    }

    fun normalizeServerUrl(url: String): String {
        val trimmed = url.trim()
        require(trimmed.startsWith("https://") || trimmed.startsWith("http://")) {
            "Server URL must start with https:// or http://"
        }
        return if (trimmed.endsWith("/")) trimmed else "$trimmed/"
    }
}
