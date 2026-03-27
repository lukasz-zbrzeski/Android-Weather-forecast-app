package com.example.weatherapp.ui.settings

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.weatherapp.NetworkUtils
import com.example.weatherapp.PreferencesManager
import com.example.weatherapp.R
import com.example.weatherapp.viewmodel.WeatherViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteCities(
    prefs: PreferencesManager,
    viewModel: WeatherViewModel,
    favorites: Set<String>,
    selectedCity: MutableState<String>,
    newCity: MutableState<String>,
    expanded: MutableState<Boolean>,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Text("Ulubione lokalizacje:", fontSize = dimenResourceAsSp(R.dimen.text_size_large))

    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = !expanded.value }
    ) {
        OutlinedTextField(
            value = selectedCity.value,
            onValueChange = { selectedCity.value = it },
            readOnly = true,
            label = { Text("Wybierz lokalizację") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            favorites.forEach { city ->
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(city)
                            TextButton(onClick = {
                                coroutineScope.launch {
                                    val updated = favorites.toMutableSet().apply { remove(city) }
                                    prefs.saveFavorites(updated)

                                    if (selectedCity.value == city && updated.isNotEmpty()) {
                                        selectedCity.value = updated.first()
                                    }

                                    val weatherFile = File(context.filesDir, "$city.json")
                                    val forecastFile = File(context.filesDir, "${city}_forecast.json")

                                    if (weatherFile.exists()) {
                                        weatherFile.delete()
                                    }

                                    if (forecastFile.exists()) {
                                        forecastFile.delete()
                                    }

                                }
                            }) {
                                Text("Usuń")
                            }
                        }
                    },
                    onClick = {
                        selectedCity.value = city
                        expanded.value = false
                        coroutineScope.launch {
                            prefs.saveLastLocation(city)
                            val unit = prefs.unitSystem.first()
                            viewModel.fetchWeatherForCity(city, unit)
                        }
                    }
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = newCity.value,
        onValueChange = { newCity.value = it },
        label = { Text("Nowe miasto") },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(4.dp))

    Button(onClick = {
        val trimmed = newCity.value.trim()
        if (trimmed.isNotEmpty() && !favorites.contains(trimmed)) {
            coroutineScope.launch {
                val unit = prefs.unitSystem.first()

                if (!NetworkUtils.isInternetAvailable(context)) {
                    Toast.makeText(context, "Brak połączenia z Internetem", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val isValid = viewModel.isCityValid(trimmed, unit)

                if (isValid) {
                    val updated = favorites.toMutableSet().apply { add(trimmed) }
                    prefs.saveFavorites(updated)
                    newCity.value = ""
                    Toast.makeText(context, "Pomyślnie dodano miasto: $trimmed", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Nie znaleziono miasta: $trimmed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }) {
        Text("Dodaj do ulubionych", fontSize = dimenResourceAsSp(R.dimen.text_size_small))
    }

}