package com.example.openweathersample.network.service

import com.example.openweathersample.network.RetrofitNetworkClient


/***
 * Network service to fetch today weather response from Open weather API
 */
internal object WeatherService {
    internal val weatherApi = RetrofitNetworkClient.retrofit.create(WeatherApi::class.java)
}