package com.example.weatherapp.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExtraWeatherInfo(
    windSpeed: Float,
    windDeg: Int,
    humidity: Int,
    visibility: Int,
    unitSystem: String
) {
    val speedUnit = if (unitSystem == "imperial") "mph" else "m/s"
    val visibilityText = if (unitSystem == "imperial") {
        val miles = visibility / 1609.34
        String.format("%.1f mi", miles)
    } else {
        "${visibility / 1000.0} km"
    }

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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "DANE DODATKOWE",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text("🌬️ Wiatr: $windSpeed $speedUnit", fontSize = 20.sp, color = Color.White)
            Text("🧭 Kierunek: $windDeg°", fontSize = 20.sp, color = Color.White)
            Text("💧 Wilgotność: $humidity%", fontSize = 20.sp, color = Color.White)
            Text("👁️ Widoczność: $visibilityText", fontSize = 20.sp, color = Color.White)
        }
    }
}