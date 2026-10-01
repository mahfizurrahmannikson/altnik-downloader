package com.altnik.downloader

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object CobaltClient {
    // Public free instance. If one dies, change to another:
    // "https://api.cobalt.tools/", "https://cobalt-api.kwiatekmiki.com/", "https://co.wukko.xyz/"
    private const val BASE_URL = "https://cobalt-api.kwiatekmiki.com/"

    val api: CobaltApi by lazy {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        val client = OkHttpClient.Builder().addInterceptor(logging).build()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CobaltApi::class.java)
    }
}
