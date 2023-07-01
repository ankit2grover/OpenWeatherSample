package com.example.openweathersample.model

import com.example.openweathersample.ui.viewmodels.uidata.WeatherData

sealed class WeatherDataState {
    object Fetching: WeatherDataState()
    data class Success(val weatherData: WeatherData): WeatherDataState()
    data class Error(val exception: Exception): WeatherDataState()
}