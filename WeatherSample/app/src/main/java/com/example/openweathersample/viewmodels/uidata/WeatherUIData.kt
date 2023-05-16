package com.example.openweathersample.viewmodels.uidata

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherUIData(
    val dateTimeInUTC: Long,
    val currentTemp: Double,
    val weatherImageUrl: String): Parcelable
