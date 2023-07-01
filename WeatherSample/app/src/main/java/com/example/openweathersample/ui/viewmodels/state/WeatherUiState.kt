package com.example.openweathersample.ui.viewmodels.state

import com.example.openweathersample.ui.viewmodels.uidata.WeatherData

sealed class WeatherUiState {
    object Loading: WeatherUiState()
    data class Success(val weatherUIData: WeatherData): WeatherUiState()
    data class Error(val exception: Throwable): WeatherUiState()
}