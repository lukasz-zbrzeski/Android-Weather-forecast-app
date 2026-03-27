package com.example.weatherapp

import android.content.Context

fun saveWeatherToFile(context: Context, data: String, filename: String) {
    context.openFileOutput(filename, Context.MODE_PRIVATE).use {
        it.write(data.toByteArray())
    }
}

fun loadWeatherFromFile(context: Context, filename: String): String? {
    return try {
        context.openFileInput(filename).bufferedReader().use { it.readText() }
    } catch (e: Exception) {
        null
    }
}

fun saveForecastToFile(context: Context, data: String, filename: String) {
    context.openFileOutput(filename, Context.MODE_PRIVATE).use {
        it.write(data.toByteArray())
    }
}

fun loadForecastFromFile(context: Context, filename: String): String? {
    return try {
        context.openFileInput(filename).bufferedReader().use { it.readText() }
    } catch (e: Exception) {
        null
    }
}