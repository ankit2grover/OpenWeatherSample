package com.example.openweathersample.datastore

import com.example.openweathersample.model.TodayWeather
import com.example.openweathersample.network.api.WeatherApi
import com.example.openweathersample.network.service.WeatherService
import kotlinx.coroutines.flow.flow

internal class WeatherRemoteDataStore() {
    internal suspend fun getWeather(weatherRequest: WeatherRequest) = flow<RemoteResult> {
        var latLong: LatLong? = null
        weatherRequest.searchQuery?.let {
           WeatherService.weatherApi.getLatLongByCity(it).firstOrNull()?.also { geocodeCity ->
               latLong = LatLong(geocodeCity.latitude, geocodeCity.longitude)
           }
        }
        latLong?.let {
            emit(RemoteResult.Success(WeatherService.weatherApi.getWeather(it.lat, it.long)))
        }?: run {
            emit(RemoteResult.Error(WeatherApiException("LatLong fetching failed by city")))
        }
    }

}


data class WeatherRequest(val latLong: LatLong?, val searchQuery: String?)
data class LatLong(val lat: Double, val long: Double)