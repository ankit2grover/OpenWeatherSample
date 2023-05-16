package com.example.openweathersample.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName


/**
 * Model maps to Open Weather API daily weather field in response
 */
@Parcelize
data class DailyWeather(
    @SerializedName("temp") val dailyTemp: DailyTemp?,
    @SerializedName("weather") val weatherDetails: ArrayList<WeatherDetails>?
):Parcelable
