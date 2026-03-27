package com.example.weatherapp.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("coord") val coord: Coord,
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("main") val main: Main,
    @SerializedName("wind") val wind: Wind,
    @SerializedName("timezone") val timezone: Int,
    @SerializedName("dt") val dt: Long,
    @SerializedName("name") val name: String
)

data class Coord(val lon: Double, val lat: Double)
data class Main(val temp: Float, val pressure: Int, val humidity: Int)
data class Weather(val description: String, val icon: String)
data class Wind(val speed: Float, val deg: Int)
