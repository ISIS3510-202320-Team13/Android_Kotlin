package com.example.parkezkotlin.core

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    // Define tu API key aquí
    private const val apiKey = "my_api_key"

    fun getRetrofit(): Retrofit {
        // Crea una instancia de HttpLoggingInterceptor y configura su nivel
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Crea una instancia de OkHttpClient con un interceptor
        // que añade la cabecera X-API-Key
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("X-API-Key", apiKey)
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()

        // Crea y devuelve la instancia de Retrofit con el OkHttpClient
        return Retrofit.Builder()
            .baseUrl("https://api.parkez.xyz:8082/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}
