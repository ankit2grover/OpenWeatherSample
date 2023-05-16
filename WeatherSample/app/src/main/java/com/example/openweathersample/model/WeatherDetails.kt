package com.example.openweathersample.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

@Parcelize
data class WeatherDetails(
    val id: Int,
    @SerializedName("main") val description: String?,
    @SerializedName("description") val detailedDescription: String?,
    val icon: String
): Parcelable
