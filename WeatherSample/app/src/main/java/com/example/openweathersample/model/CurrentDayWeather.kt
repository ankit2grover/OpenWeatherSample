package com.example.openweathersample.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

@Parcelize
data class CurrentDayWeather(
    @SerializedName("dt") val dateTimeInUTC: Long,
    @SerializedName("temp") val currentTemp: Double,
    @SerializedName("weather") val weatherDetails: ArrayList<WeatherDetails>
): Parcelable