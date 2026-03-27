package com.example.weatherapp.ui.dashboard

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun BasicWeatherInfo(
    city: String,
    lat: Double,
    lon: Double,
    temp: Float,
    pressure: Int,
    description: String,
    icon: String,
    unitSystem: String
) {
    val configuration = LocalConfiguration.current
    val minDimension = minOf(configuration.screenWidthDp, configuration.screenHeightDp)
    val isTablet = minDimension >= 600
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    when {
        isTablet && isPortrait -> BasicWeatherTabletPortrait(city, lat, lon, temp, pressure, description, icon, unitSystem)
        isTablet && !isPortrait -> BasicWeatherTabletLandscape(city, lat, lon, temp, pressure, description, icon, unitSystem)
        !isTablet && isPortrait -> BasicWeatherPortrait(city, lat, lon, temp, pressure, description, icon, unitSystem)
        else -> BasicWeatherLandscape(city, lat, lon, temp, pressure, description, icon, unitSystem)
    }
}

@Composable
fun BasicWeatherTabletPortrait(
    city: String,
    lat: Double,
    lon: Double,
    temp: Float,
    pressure: Int,
    description: String,
    icon: String,
    unitSystem: String
) {
    BasicWeatherLandscape(
        city, lat, lon, temp, pressure, description, icon, unitSystem
    )
}

@Composable
fun BasicWeatherTabletLandscape(
    city: String,
    lat: Double,
    lon: Double,
    temp: Float,
    pressure: Int,
    description: String,
    icon: String,
    unitSystem: String
) {
    BasicWeatherPortrait(
        city, lat, lon, temp, pressure, description, icon, unitSystem
    )
}



@Composable
fun BasicWeatherPortrait(
    city: String,
    lat: Double,
    lon: Double,
    temp: Float,
    pressure: Int,
    description: String,
    icon: String,
    unitSystem: String
) {
    val unit = if (unitSystem == "imperial") "°F" else "°C"
    val translatedDescription = translateWeatherDescription(description)
    val iconUrl = "https://openweathermap.org/img/wn/${icon}@4x.png"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1565C0), Color(0xFF1E88E5))
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Miasto
            Text(
                text = city,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            // Koordynaty
            Text(
                text = "($lat, $lon)",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Ikona pogody
            AsyncImage(
                model = iconUrl,
                contentDescription = null,
                modifier = Modifier.size(140.dp)
            )

            // Opis
            Text(
                text = translatedDescription.uppercase(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Temperatura i ciśnienie
            Text(
                text = "${temp.toInt()}$unit",
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "Ciśnienie: $pressure hPa",
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun BasicWeatherLandscape(
    city: String,
    lat: Double,
    lon: Double,
    temp: Float,
    pressure: Int,
    description: String,
    icon: String,
    unitSystem: String
) {
    val unit = if (unitSystem == "imperial") "°F" else "°C"
    val translatedDescription = translateWeatherDescription(description)
    val iconUrl = "https://openweathermap.org/img/wn/${icon}@4x.png"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1565C0), Color(0xFF1E88E5))
                )
            )
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Lewa kolumna: Miasto, koordynaty, temperatura, ciśnienie
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = city,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "($lat, $lon)",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${temp.toInt()}$unit",
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Ciśnienie: $pressure hPa",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            // Prawa kolumna: ikona + opis
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = iconUrl,
                    contentDescription = null,
                    modifier = Modifier.size(140.dp)
                )

                Text(
                    text = translatedDescription.uppercase(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}