package com.example.weatherapp.work

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.weatherapp.PreferencesManager
import com.example.weatherapp.viewmodel.WeatherViewModel
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

class WeatherWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val prefs = PreferencesManager(applicationContext)
        val city = prefs.lastLocation.first()
        val unit = prefs.unitSystem.first()
        val viewModel = WeatherViewModel(applicationContext)

        if (city != null) {
            if (city.isNotEmpty()) {
                viewModel.fetchWeatherForCity(city, unit)
                viewModel.fetchForecastForCity(city)
                Log.d("WeatherWorker", "doWork called — aktualizacja pogody.")
            }
        }

        return Result.success()
    }
}

fun scheduleWeatherRefresh(context: Context, intervalHours: Long) {
    val request = PeriodicWorkRequestBuilder<WeatherWorker>(intervalHours, TimeUnit.HOURS)
        .addTag("weather_refresh")
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "WeatherAutoRefresh",
        ExistingPeriodicWorkPolicy.UPDATE,
        request
    )
}