package com.example.openweathersample.repository

import com.example.openweathersample.datastore.RemoteResult
import com.example.openweathersample.datastore.WeatherApiException
import com.example.openweathersample.datastore.WeatherRemoteDataStore
import com.example.openweathersample.datastore.WeatherRequest
import com.example.openweathersample.model.TodayWeather
import com.example.openweathersample.model.WeatherDataState
import com.example.openweathersample.ui.viewmodels.state.WeatherUiState
import com.example.openweathersample.ui.viewmodels.uidata.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.lang.Exception

class WeatherRepository(private val weatherRemoteDataStore: WeatherRemoteDataStore =
    WeatherRemoteDataStore()) {
    fun getWeather(weatherRequest: WeatherRequest) = flow {
        try {
            weatherRemoteDataStore.getWeather(weatherRequest)?.let { todayWeather ->
                emit(
                    WeatherDataState.Success(
                        WeatherData(
                            todayWeather.currentDayWeather.dateTimeInUTC,
                            todayWeather.currentDayWeather.currentTemp,
                            todayWeather.currentDayWeather.weatherDetails.first().icon
                        )
                    )
                )
            } ?: run {
                emit(WeatherDataState.Error(WeatherApiException("Data is empty")))
            }
        } catch (ex: Exception) {
            emit(WeatherDataState.Error(ex))
        }
    }.flowOn(Dispatchers.IO)

}





