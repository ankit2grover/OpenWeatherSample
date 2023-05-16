package com.example.openweathersample.viewmodels.state

import com.example.openweathersample.viewmodels.uidata.WeatherUIData

sealed class WeatherUiState {
    object Loading: WeatherUiState()
    data class Success(val weatherUIData: WeatherUIData): WeatherUiState()
    data class Error(val exception: Exception): WeatherUiState()
}