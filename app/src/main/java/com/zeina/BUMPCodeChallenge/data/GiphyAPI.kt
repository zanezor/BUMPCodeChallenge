package com.zeina.BUMPCodeChallenge.data

import com.google.gson.GsonBuilder
import com.zeina.BUMPCodeChallenge.BuildConfig
import com.zeina.BUMPCodeChallenge.search.SearchService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GiphyAPI {

    private var retrofit: Retrofit
    private const val BASE_URL = "http://api.giphy.com"
    const val GIPHY_API_KEY = "PASTE_API_KEY_HERE"

    init {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.addInterceptor {
            chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
            requestBuilder.addHeader("api_key", GIPHY_API_KEY)
            val request = requestBuilder.build()
            chain.proceed(request)
        }

        if (BuildConfig.DEBUG){
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
            okHttpClient.addInterceptor(loggingInterceptor)
        }

        retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
    }

    fun searching(): SearchService = retrofit.create(SearchService::class.java)
}