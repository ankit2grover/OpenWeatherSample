package com.example.openweathersample.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

@Parcelize
data class DailyTemp(
    @SerializedName("day") val averageTemp: Double,
    @SerializedName("min") val minTemp: Double,
    @SerializedName("max") val maxTemp: Double,
    @SerializedName("night") val nightTemp: Double,
    @SerializedName("eve") val eveningTemp: Double,
    @SerializedName("morn") val morningTemp: Double
): Parcelable
