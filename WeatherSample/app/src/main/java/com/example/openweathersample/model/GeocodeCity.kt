package com.example.openweathersample.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GeocodeCity(
    @SerializedName("name") val cityName: String,
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double,
    @SerializedName("state") val state: String,
    @SerializedName("country") val country: String,
): Parcelable
