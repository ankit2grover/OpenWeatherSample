package com.example.openweathersample.datastore

import com.example.openweathersample.model.TodayWeather
import com.example.openweathersample.network.api.WeatherApi
import com.example.openweathersample.network.service.WeatherService
import kotlinx.coroutines.flow.flow

class WeatherRemoteDataStore {

    internal suspend fun getWeather(weatherRequest: WeatherRequest): TodayWeather? {
        var latLong: LatLong? = null
        weatherRequest.searchQuery?.let {
            WeatherService.weatherApi.getLatLongByCity(it).firstOrNull()?.also { geocodeCity ->
                latLong = LatLong(geocodeCity.latitude, geocodeCity.longitude)
            } ?: run{
                WeatherApiException("Weather Api results are empty")
            }
            latLong?.let {
                return WeatherService.weatherApi.getWeather(it.lat, it.long)
            }?: run{
                WeatherApiException("Lat Long is null")
            }
        } ?: run{
            WeatherApiException("Search Query is null")
        }
        return null
    }
}


data class WeatherRequest(val latLong: LatLong? = null, val searchQuery: String? = null)
data class LatLong(val lat: Double, val long: Double)