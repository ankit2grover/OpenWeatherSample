package com.example.openweathersample.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
/**
 * Retrofit network client to manage all network operations.
 * Improvement : We can add no internet connectivity interceptor to show no network message to user.
 */
internal object RetrofitNetworkClient {

    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    internal val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(buildHttpClient())
        .build()

    private fun buildHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
//        if (BuildConfig.DEBUG) {
//            val interceptor = HttpLoggingInterceptor()
//            interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
//            client.apply {
//                addInterceptor(interceptor)
//            }
//        }
        return client.build()
    }
}