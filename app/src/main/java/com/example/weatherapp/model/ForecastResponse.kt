package com.example.weatherapp.model

data class ForecastResponse(
    val list: List<ForecastItem>
)

data class ForecastItem(
    val dt_txt: String,
    val main: MainInfo,
    val weather: List<WeatherInfo>
)

data class MainInfo(val temp: Float)
data class WeatherInfo(val description: String)
