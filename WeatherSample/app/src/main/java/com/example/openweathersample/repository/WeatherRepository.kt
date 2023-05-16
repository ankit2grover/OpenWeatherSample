package com.example.openweathersample.repository

import com.example.openweathersample.datastore.RemoteResult
import com.example.openweathersample.datastore.WeatherRemoteDataStore
import com.example.openweathersample.datastore.WeatherRequest
import com.example.openweathersample.viewmodels.state.WeatherUiState
import com.example.openweathersample.viewmodels.uidata.WeatherUIData
import kotlinx.coroutines.flow.map

object WeatherRepository {
    suspend fun getWeather(weatherRequest: WeatherRequest) =
        WeatherRemoteDataStore().getWeather(weatherRequest).map { remoteResult ->
            when(remoteResult) {
                is RemoteResult.Success -> WeatherUiState.Success(
                    WeatherUIData(remoteResult.todayWeather.currentDayWeather.dateTimeInUTC,
                        remoteResult.todayWeather.currentDayWeather.currentTemp,
                        remoteResult.todayWeather.currentDayWeather.weatherDetails.first().icon))
                is RemoteResult.Error -> WeatherUiState.Error(remoteResult.error)
            }
        }
}