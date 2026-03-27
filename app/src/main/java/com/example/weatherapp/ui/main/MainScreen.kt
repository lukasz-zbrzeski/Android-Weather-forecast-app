package com.example.weatherapp.ui.main

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.PreferencesManager
import com.example.weatherapp.ui.dashboard.BasicWeatherInfo
import com.example.weatherapp.ui.dashboard.ExtraWeatherInfo
import com.example.weatherapp.ui.dashboard.ForecastDay
import com.example.weatherapp.ui.dashboard.ForecastWeatherInfo
import com.example.weatherapp.ui.dashboard.WeatherDashboard
import com.example.weatherapp.viewmodel.WeatherViewModel

@Composable
fun MainScreen(
    context: Context,
    viewModel: WeatherViewModel,
    onNavigateToSettings: () -> Unit
) {
    val prefs = PreferencesManager(context)

    val favorites by prefs.favorites.collectAsState(initial = setOf("Warsaw"))
    val lastSelectedCity = prefs.lastLocation.collectAsState(initial = favorites.firstOrNull() ?: "")
    val selectedCity = remember { mutableStateOf("") }

    val unitSystem by prefs.unitSystem.collectAsState(initial = "metric")

    LaunchedEffect(lastSelectedCity.value) {
        val city = lastSelectedCity.value
        if (!city.isNullOrEmpty()) {
            selectedCity.value = city
            viewModel.fetchWeatherForCity(city, unitSystem)
            viewModel.fetchForecastForCity(city)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Row {
            Button(
                onClick = onNavigateToSettings,
                modifier = Modifier
                    .height(48.dp)
                    .widthIn(min = 140.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text("Ustawienia", fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        WeatherDashboard(
            basicData = {
                val data = viewModel.weatherMap[selectedCity.value]
                if (data != null) {
                    BasicWeatherInfo(
                        city = selectedCity.value,
                        lat = data.coord.lat,
                        lon = data.coord.lon,
                        temp = data.main.temp,
                        pressure = data.main.pressure,
                        description = data.weather.firstOrNull()?.description ?: "",
                        icon = data.weather.firstOrNull()?.icon ?: "",
                        unitSystem = unitSystem
                    )
                } else {
                    Text("Brak danych.")
                }
            },
            extraData = {
                val data = viewModel.weatherMap[selectedCity.value]
                if (data != null) {
                    ExtraWeatherInfo(
                        windSpeed = data.wind.speed,
                        windDeg = data.wind.deg,
                        humidity = data.main.humidity,
                        visibility = 10000,
                        unitSystem = unitSystem
                    )
                } else {
                    Text("Brak danych.")
                }
            },
            forecastData = {
                val forecastList = viewModel.forecastMap[selectedCity.value] ?: emptyList()
                val forecastDays = forecastList.mapNotNull { line ->
                    val parts = line.split(": ", ", ")
                    if (parts.size == 3) {
                        val date = parts[0]
                        val tempStr = parts[1].replace("°C", "").replace("°F", "")
                        val temp = tempStr.toFloatOrNull() ?: return@mapNotNull null
                        val icon = getWeatherIconFromDescription(parts[2])
                        ForecastDay(date, icon, temp)
                    } else null
                }
                ForecastWeatherInfo(forecastDays, unitSystem)
            }
        )
    }
}

fun getWeatherIconFromDescription(description: String): String {
    return when (description.lowercase()) {
        "clear sky" -> "01d"
        "few clouds" -> "02d"
        "scattered clouds" -> "03d"
        "broken clouds" -> "04d"
        "overcast clouds" -> "04d"
        "light rain" -> "10d"
        "moderate rain" -> "10d"
        "heavy intensity rain" -> "10d"
        "thunderstorm" -> "11d"
        "snow" -> "13d"
        "mist" -> "50d"
        else -> "01d"
    }
}
