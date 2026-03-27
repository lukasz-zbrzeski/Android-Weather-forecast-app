package com.example.weatherapp.ui.settings

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.weatherapp.PreferencesManager
import com.example.weatherapp.R
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.example.weatherapp.work.scheduleWeatherRefresh
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    context: Context,
    viewModel: WeatherViewModel,
    onSaveAndClose: () -> Unit,
    onRefreshNow: () -> Unit,
    onRefreshIntervalChange: (Int) -> Unit,
    onUnitChange: (String) -> Unit
) {
    val prefs = PreferencesManager(context)
    val coroutineScope = rememberCoroutineScope()
    val refreshIntervals = listOf(1, 3, 6, 12, 24)

    val favorites by prefs.favorites.collectAsState(initial = setOf("Warsaw"))
    val newCity = remember { mutableStateOf("") }
    val expanded = remember { mutableStateOf(false) }

    var selectedUnit by remember { mutableStateOf("metric") }
    val selectedInterval = remember { mutableStateOf(1) }

    LaunchedEffect(Unit) {
        selectedUnit = prefs.unitSystem.first()
        selectedInterval.value = prefs.refreshInterval.first()
    }

    val lastLocation by prefs.lastLocation.collectAsState(initial = favorites.firstOrNull() ?: "")
    val selectedCity = remember { mutableStateOf("") }

    LaunchedEffect(lastLocation, favorites) {
        if (favorites.contains(lastLocation)) {
            selectedCity.value = lastLocation.toString()
        } else if (favorites.isNotEmpty()) {
            selectedCity.value = favorites.first()
        }
    }

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT


    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (!isPortrait) {
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    SettingsHeader(onRefreshNow)
                    FavoriteCities(prefs, viewModel, favorites, selectedCity, newCity, expanded)
                }
                Column(modifier = Modifier.weight(1f)) {
                    RefreshIntervalSelection(
                        prefs = prefs,
                        coroutineScope = coroutineScope,
                        refreshIntervals = refreshIntervals,
                        selectedIntervalState = selectedInterval,
                        onRefreshIntervalChange = {
                            scheduleWeatherRefresh(context, it.toLong())
                        }
                    )
                    UnitSelection(selectedUnit, onUnitChange) {
                        selectedUnit = it
                        coroutineScope.launch {
                            prefs.setUnitSystem(it)
                        }
                    }
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(bottom = 64.dp)) {
                SettingsHeader(onRefreshNow)
                FavoriteCities(prefs, viewModel, favorites, selectedCity, newCity, expanded)
                Spacer(modifier = Modifier.height(16.dp))
                RefreshIntervalSelection(
                    prefs = prefs,
                    coroutineScope = coroutineScope,
                    refreshIntervals = refreshIntervals,
                    selectedIntervalState = selectedInterval,
                    onRefreshIntervalChange = {
                        Toast.makeText(context, "Wybrano interwał: $it", Toast.LENGTH_SHORT).show()
                        scheduleWeatherRefresh(context, it.toLong())
                    }
                )
                UnitSelection(selectedUnit, onUnitChange) {
                    selectedUnit = it
                    coroutineScope.launch {
                        prefs.setUnitSystem(it)
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                scope.launch {
                    prefs.setUnitSystem(selectedUnit)
                    prefs.setRefreshInterval(selectedInterval.value)
                    onUnitChange(selectedUnit)
                    WeatherViewModel(context).startAutoRefresh()
                    onRefreshNow()
                    onSaveAndClose()
                }
            }) {
                Text("Zapisz", fontSize = dimenResourceAsSp(R.dimen.text_size_medium))
            }
        }
    }
}
