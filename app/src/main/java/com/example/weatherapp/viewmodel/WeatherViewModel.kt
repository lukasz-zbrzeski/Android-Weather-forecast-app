package com.example.weatherapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.NetworkUtils
import com.example.weatherapp.PreferencesManager
import com.example.weatherapp.api.RetrofitInstance
import com.example.weatherapp.loadForecastFromFile
import com.example.weatherapp.loadWeatherFromFile
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.saveForecastToFile
import com.example.weatherapp.saveWeatherToFile
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class WeatherViewModel(private val context: Context) : ViewModel() {

    var weatherMap by mutableStateOf<Map<String, WeatherResponse>>(emptyMap())
        private set

    private val apiKey = "6d5fcfe84cdef6809f9f7c45664a9e78"

    init {
        viewModelScope.launch {
            if (NetworkUtils.isInternetAvailable(context)) {
                try {
                    val result = RetrofitInstance.api.getWeather("Warsaw", apiKey)
                    val newMap = weatherMap.toMutableMap()
                    newMap["Warsaw"] = result
                    weatherMap = newMap
                    saveWeatherToFile(context, Gson().toJson(result), "Warsaw.json")
                } catch (e: Exception) {
                    Log.e("Weather", "Error: ${e.message}")
                    loadOfflineData()
                }
            } else {
                loadOfflineData()
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                val city = preferences.lastLocation.first()
                val unit = preferences.unitSystem.first()

                if (!city.isNullOrBlank()) {
                    fetchWeatherForCity(city, unit)
                    fetchForecastForCity(city)
                }
            } catch (e: Exception) {
                Log.e("Weather", "Refresh error: ${e.message}")
            }
        }
    }

    private fun loadOfflineData() {
        loadWeatherFromFile(context, "Warsaw.json")?.let {
            weatherMap = mapOf("Warsaw" to Gson().fromJson(it, WeatherResponse::class.java))
        }
    }

    var forecastMap by mutableStateOf<Map<String, List<String>>>(emptyMap())
        private set


    private val preferences = PreferencesManager(context)

    init {
        viewModelScope.launch {
            val city = preferences.lastLocation.first()
            val unit = preferences.unitSystem.first()
            if (!city.isNullOrBlank()) {
                fetchWeatherForCity(city, unit)
                fetchForecastForCity(city)
            }
        }
    }

    suspend fun fetchWeatherForCity(city: String, unit: String) {
        if (NetworkUtils.isInternetAvailable(context)) {
            try {
                val result = RetrofitInstance.api.getWeather(city, apiKey, units = unit)
                val newMap = weatherMap.toMutableMap()
                newMap[city] = result
                weatherMap = newMap
                saveWeatherToFile(context, Gson().toJson(result), "$city.json")
            } catch (e: Exception) {
                val json = loadWeatherFromFile(context, "$city.json")
                json?.let {
                    val fallback = Gson().fromJson(it, WeatherResponse::class.java)
                    val newMap = weatherMap.toMutableMap()
                    newMap[city] = fallback
                    weatherMap = newMap
                }
            }
        } else {
            val json = loadWeatherFromFile(context, "$city.json")
            json?.let {
                val fallback = Gson().fromJson(it, WeatherResponse::class.java)
                val newMap = weatherMap.toMutableMap()
                newMap[city] = fallback
                weatherMap = newMap
            }
        }
    }

    suspend fun fetchForecastForCity(city: String) {
        try {
            val result = RetrofitInstance.api.getForecast(city, apiKey)
            val forecastList = result.list
                .filterIndexed { index, _ -> index % 8 == 0 }
                .map {
                    val date = it.dt_txt.substringBefore(" ")
                    val desc = it.weather.firstOrNull()?.description ?: "brak danych"
                    val temp = it.main.temp
                    "$date: $temp°C, $desc"
                }

            val newMap = forecastMap.toMutableMap()
            newMap[city] = forecastList
            forecastMap = newMap

            saveForecastToFile(context, Gson().toJson(forecastList), "${city}_forecast.json")

        } catch (e: Exception) {
            val fallback = loadForecastFromFile(context, "${city}_forecast.json")
            fallback?.let {
                val list = Gson().fromJson(it, Array<String>::class.java).toList()
                val newMap = forecastMap.toMutableMap()
                newMap[city] = list
                forecastMap = newMap
            }

            Log.e("Forecast", "Błąd pobierania prognozy dla $city: ${e.message}")
        }
    }

    suspend fun isCityValid(city: String, unit: String): Boolean {
        return try {
            val response = RetrofitInstance.api.getWeather(city, apiKey, units = unit)
            response.name.isNotBlank()
        } catch (e: Exception) {
            false
        }
    }

    fun updateUnitSystem(unit: String) {
        viewModelScope.launch {
            prefs.setUnitSystem(unit)
        }
    }

    private val prefs = PreferencesManager(context)
    private var autoRefreshJob: Job? = null

    fun startAutoRefresh() {
        autoRefreshJob?.cancel()
        viewModelScope.launch {
            val interval = prefs.refreshInterval.first()
            autoRefreshJob = launch {
                while (true) {
                    val city = prefs.lastLocation.first()
                    val unit = preferences.unitSystem.first()
                    if (!city.isNullOrBlank()) {
                        fetchWeatherForCity(city, unit)
                        fetchForecastForCity(city)
                    }
                    delay(interval * 60 * 60 * 1000L)
                }
            }
        }
    }

}
