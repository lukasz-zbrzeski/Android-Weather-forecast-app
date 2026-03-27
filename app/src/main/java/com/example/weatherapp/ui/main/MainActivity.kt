package com.example.weatherapp.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.NetworkUtils
import com.example.weatherapp.ui.settings.SettingsScreen
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.example.weatherapp.work.scheduleWeatherRefresh

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    @SuppressLint("UnusedContentLambdaTargetStateParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = WeatherViewModel(applicationContext)

        if (!NetworkUtils.isInternetAvailable(applicationContext)) {
            Toast.makeText(this, "Brak połączenia z Internetem. Dane pogodowe mogą być nieaktualne.", Toast.LENGTH_LONG).show()
        }

        viewModel.startAutoRefresh()

        setContent {
            val navController = rememberNavController()

            WeatherAppTheme {
                NavHost(
                    navController = navController,
                    startDestination = NavRoutes.MAIN
                ) {
                    composable(NavRoutes.MAIN) {
                        AnimatedContent(
                            targetState = NavRoutes.MAIN,
                            transitionSpec = {
                                slideInHorizontally(animationSpec = tween(300)) { fullWidth -> fullWidth } togetherWith
                                        slideOutHorizontally(animationSpec = tween(300)) { fullWidth -> -fullWidth }
                            },
                            label = "MainToSettingsSlide"
                        ) {
                            MainScreen(
                                context = applicationContext,
                                viewModel = viewModel,
                                onNavigateToSettings = { navController.navigate(NavRoutes.SETTINGS) }
                            )
                        }
                    }

                    composable(NavRoutes.SETTINGS) {
                        AnimatedContent(
                            targetState = NavRoutes.SETTINGS,
                            transitionSpec = {
                                slideInHorizontally(animationSpec = tween(300)) { fullWidth -> fullWidth } togetherWith
                                        slideOutHorizontally(animationSpec = tween(300)) { fullWidth -> -fullWidth }
                            },
                            label = "SettingsSlide"
                        ) {
                            SettingsScreen(
                                context = applicationContext,
                                viewModel = viewModel,
                                onSaveAndClose = { navController.popBackStack() },
                                onRefreshNow = { viewModel.refresh() },
                                onRefreshIntervalChange = { scheduleWeatherRefresh(applicationContext, it.toLong()) },
                                onUnitChange = { viewModel.updateUnitSystem(it) },
                            )
                        }
                    }
                }
            }
        }

    }

}

