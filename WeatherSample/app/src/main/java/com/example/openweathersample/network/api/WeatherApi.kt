package com.example.openweathersample.network.api

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API interface for invoking Open weather api. Hardcoded units, excludeFromResponse and appKey.
 * We can replace hardcoded values with constants here.
 */
interface WeatherApi {
    @GET("onecall")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String? = "imperial",
        @Query("exclude") excludeFromResponse: String? = "minutely,alerts",
        @Query("appid") appKey: String? = "7e66b0c27b5730bd3adf5e221527e9a8"
    ) : TodayWeather
}