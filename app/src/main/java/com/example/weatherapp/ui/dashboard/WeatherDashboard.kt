package com.example.weatherapp.ui.dashboard

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeatherDashboard(
    basicData: @Composable () -> Unit,
    extraData: @Composable () -> Unit,
    forecastData: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val minDimension = minOf(configuration.screenWidthDp, configuration.screenHeightDp)
    val isTablet = minDimension >= 600
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    when {
        !isTablet && isPortrait -> PhonePortraitDashboard(basicData, extraData, forecastData)
        !isTablet && !isPortrait -> PhoneLandscapeDashboard(basicData, extraData, forecastData)
        isTablet && isPortrait -> TabletPortraitDashboard(basicData, extraData, forecastData)
        isTablet && !isPortrait -> TabletLandscapeDashboard(basicData, extraData, forecastData)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhonePortraitDashboard(
    basicData: @Composable () -> Unit,
    extraData: @Composable () -> Unit,
    forecastData: @Composable () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })

    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
        when (page) {
            0 -> basicData()
            1 -> extraData()
            2 -> forecastData()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhoneLandscapeDashboard(
    basicData: @Composable () -> Unit,
    extraData: @Composable () -> Unit,
    forecastData: @Composable () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })

    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
        when (page) {
            0 -> basicData()
            1 -> extraData()
            2 -> forecastData()
        }
    }
}

@Composable
fun TabletLandscapeDashboard(
    basicData: @Composable () -> Unit,
    extraData: @Composable () -> Unit,
    forecastData: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) { basicData() }
        Column(modifier = Modifier.weight(1f)) { extraData() }
        Column(modifier = Modifier.weight(1f)) { forecastData() }
    }
}

@Composable
fun TabletPortraitDashboard(
    basicData: @Composable () -> Unit,
    extraData: @Composable () -> Unit,
    forecastData: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
        ) {
            basicData()
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
        ) {
            extraData()
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
        ) {
            forecastData()
        }
    }
}
