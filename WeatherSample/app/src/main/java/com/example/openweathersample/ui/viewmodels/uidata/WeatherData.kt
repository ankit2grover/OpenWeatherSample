package com.example.openweathersample.ui.viewmodels.uidata

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherData(
    val dateTimeInUTC: Long,
    val currentTemp: Double,
    val weatherImageUrl: String): Parcelable
