package com.example.parkezkotlin.core

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import okhttp3.Interceptor
import okhttp3.OkHttpClient

object RetrofitHelper {

    // Define your API key here
    private const val apiKey = "my_api_key"

    fun getRetrofit(): Retrofit {
        // Create an OkHttpClient instance with an interceptor that adds the X-API-Key header
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("X-API-Key", apiKey)
            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()

        // Create the Retrofit instance with the OkHttpClient
        return Retrofit.Builder()
            .baseUrl("http://api.parkez.xyz:8082/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}
