package com.example.weatherapp.ui.dashboard

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

data class ForecastDay(
    val date: String,
    val icon: String,
    val temp: Float
)

@Composable
fun ForecastWeatherInfo(forecast: List<ForecastDay>, unitSystem: String) {
    val configuration = LocalConfiguration.current
    val minDimension = minOf(configuration.screenWidthDp, configuration.screenHeightDp)
    val isTablet = minDimension >= 600
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    when {
        isTablet && isPortrait -> ForecastWeatherTabletPortrait(forecast, unitSystem)
        isTablet && !isPortrait -> ForecastWeatherTabletLandscape(forecast, unitSystem)
        !isTablet && isPortrait -> ForecastWeatherPortrait(forecast, unitSystem)
        else -> ForecastWeatherLandscape(forecast, unitSystem)
    }
}

@Composable
fun ForecastWeatherTabletPortrait(forecast: List<ForecastDay>, unitSystem: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1565C0), Color(0xFF1E88E5))
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "\uD83D\uDCC5 PROGNOZA",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(forecast) { day ->
                    ForecastTile(day, unitSystem, tileWidth = 120.dp, tileHeight = 180.dp)
                }
            }
        }
    }
}


@Composable
fun ForecastWeatherTabletLandscape(forecast: List<ForecastDay>, unitSystem: String) {
    ForecastWeatherPortrait(forecast, unitSystem)
}

@Composable
fun ForecastWeatherPortrait(forecast: List<ForecastDay>, unitSystem: String) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1565C0), Color(0xFF1E88E5))
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "\uD83D\uDCC5 PROGNOZA",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val rows = forecast.chunked(2)

            rows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (row.size == 2) Arrangement.SpaceEvenly else Arrangement.Center
                ) {
                    row.forEach { day ->
                        ForecastTile(day, unitSystem)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun ForecastWeatherLandscape(forecast: List<ForecastDay>, unitSystem: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1565C0), Color(0xFF1E88E5))
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "\uD83D\uDCC5 PROGNOZA",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    items(forecast) { day ->
//                        ForecastTile(day, unitSystem)
                        ForecastTile(
                            day = day,
                            unitSystem = unitSystem,
                            tileWidth = 140.dp,
                            tileHeight = 180.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ForecastTile(
    day: ForecastDay,
    unitSystem: String,
    tileWidth: Dp = 140.dp,
    tileHeight: Dp = 180.dp
) {
    val iconUrl = "https://openweathermap.org/img/wn/${day.icon}@2x.png"
    val temp = if (unitSystem == "imperial") (day.temp * 9 / 5 + 32) else day.temp
    val unit = if (unitSystem == "imperial") "°F" else "°C"

    Card(
        backgroundColor = Color.White.copy(alpha = 0.15f),
        elevation = 6.dp,
        modifier = Modifier
            .width(tileWidth)
            .height(tileHeight)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = day.date, color = Color.White, fontSize = 14.sp)

            AsyncImage(
                model = iconUrl,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )

            Text(
                text = "${temp.toInt()}$unit",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}