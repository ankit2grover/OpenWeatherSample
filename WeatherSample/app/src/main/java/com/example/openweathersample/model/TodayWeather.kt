package com.example.openweathersample.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

/*
* Model maps to Today Weather API daily weather field in response
*/
@Parcelize
data class TodayWeather(
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double,
    val timezone: String?,
    @SerializedName("current") val currentDayWeather: CurrentDayWeather,
    @SerializedName("daily") val dailyWeather: ArrayList<DailyWeather>
):Parcelable
