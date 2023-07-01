package com.example.openweathersample.ui.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openweathersample.datastore.LatLong
import com.example.openweathersample.datastore.WeatherRequest
import com.example.openweathersample.model.WeatherDataState
import com.example.openweathersample.repository.WeatherRepository
import com.example.openweathersample.ui.viewmodels.state.WeatherUiState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class WeatherViewModel(private val weatherRepository: WeatherRepository = WeatherRepository()):
    ViewModel() {
    private val weatherMutableStateFlow: MutableStateFlow<WeatherUiState> =
        MutableStateFlow(WeatherUiState.Loading)
    internal val weatherStateFlow: StateFlow<WeatherUiState> = weatherMutableStateFlow.asStateFlow()
    internal val locationPermissionGranted : MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    internal val isUserSearching : MutableStateFlow<Boolean> =
        MutableStateFlow(false)

    private fun getWeather(searchCity: String?, latLong: LatLong?) {
        viewModelScope.launch {
            weatherMutableStateFlow.value = WeatherUiState.Loading
            weatherRepository.getWeather(WeatherRequest(LatLong(-37.56,63.23), "Pleasanton")).catch {exception ->
            }.catch {
                weatherMutableStateFlow.value =
                    WeatherUiState.Error(it)
            }.collect { weatherDataState ->
                when(weatherDataState) {
                    is WeatherDataState.Fetching -> weatherMutableStateFlow.value = WeatherUiState.Loading
                    is WeatherDataState.Success -> {
                        weatherMutableStateFlow.value =
                            WeatherUiState.Success(weatherDataState.weatherData)
                    }
                    is WeatherDataState.Error -> {
                        weatherMutableStateFlow.value =
                            WeatherUiState.Error(weatherDataState.exception)
                    }
                }
            }
        }
    }

    fun setLocationPermissionGranted(locationGranted: Boolean) {
        locationPermissionGranted.value = locationGranted
    }

    fun setIsUserSearchingState(isSearching: Boolean) {
        locationPermissionGranted.value = isSearching
    }

    fun getWeatherBySearchCity(searchCity: String?) {
        getWeather(searchCity, null)
    }


    fun getWeatherByLocation(locationClient: FusedLocationProviderClient) {
        viewModelScope.launch {
            weatherMutableStateFlow.value = WeatherUiState.Loading
            val latLong =
                withContext(Dispatchers.IO) { getUserLatLong(locationClient) }
            getWeather(null, latLong)
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun getUserLatLong(locationClient: FusedLocationProviderClient): LatLong {
        val result = locationClient.lastLocation.await()
        val latitude = result.latitude
        val longitude = result.longitude
        return LatLong(latitude, longitude)
    }

}