package com.example.weatherapp.ui.dashboard

fun translateWeatherDescription(desc: String): String {
    return when (desc.lowercase()) {
        "clear sky" -> "bezchmurnie"
        "few clouds" -> "małe zachmurzenie"
        "scattered clouds" -> "rozproszone chmury"
        "broken clouds" -> "pochmurno"
        "overcast clouds" -> "całkowite zachmurzenie"
        "light rain" -> "lekki deszcz"
        "moderate rain" -> "umiarkowany deszcz"
        "heavy intensity rain" -> "silny deszcz"
        "thunderstorm" -> "burza"
        "snow" -> "śnieg"
        "mist" -> "mgła"
        else -> desc
    }
}