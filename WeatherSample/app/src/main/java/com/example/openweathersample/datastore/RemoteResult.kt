package com.example.openweathersample.datastore

import com.example.openweathersample.model.TodayWeather

sealed class RemoteResult {
    data class Success(val todayWeather: TodayWeather): RemoteResult()
    data class Error(val error: WeatherApiException): RemoteResult()
}


class WeatherApiException(message: String): Exception(message)
