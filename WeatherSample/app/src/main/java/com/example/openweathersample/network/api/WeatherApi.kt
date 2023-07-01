package com.example.openweathersample.network.api

import com.example.openweathersample.model.GeocodeCity
import com.example.openweathersample.model.TodayWeather
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API interface for invoking Open weather api. Hardcoded units, excludeFromResponse and appKey.
 * We can replace hardcoded values with constants here.
 */
interface WeatherApi {
    @GET("data/2.5/onecall")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String? = "imperial",
        @Query("exclude") excludeFromResponse: String? = "minutely,alerts",
        @Query("appid") appKey: String? = API_KEY
    ) : TodayWeather

    @GET("geo/1.0/direct")
    suspend fun getLatLongByCity(
        @Query("q") query: String,
        @Query("appid") appKey: String? = API_KEY
    ): List<GeocodeCity>
}
private const val API_KEY = "7e66b0c27b5730bd3adf5e221527e9a8"