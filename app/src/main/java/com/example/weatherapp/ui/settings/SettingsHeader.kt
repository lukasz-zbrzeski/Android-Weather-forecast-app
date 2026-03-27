package com.example.weatherapp.ui.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.weatherapp.NetworkUtils
import com.example.weatherapp.R
import com.example.weatherapp.viewmodel.WeatherViewModel

@Composable
fun SettingsHeader(onRefreshNow: () -> Unit) {
    val context = LocalContext.current
    val viewModel = remember { WeatherViewModel(context) }

    Text("Ustawienia", fontSize = dimenResourceAsSp(R.dimen.text_size_title))
    Spacer(modifier = Modifier.height(16.dp))
    Button(onClick = {
        if (!NetworkUtils.isInternetAvailable(context)) {
            Toast.makeText(context, "Brak połączenia z Internetem", Toast.LENGTH_SHORT).show()
        } else {
            onRefreshNow()
            Toast.makeText(context, "Dane odświeżone", Toast.LENGTH_SHORT).show()
        }
    }) {
        Text("Odśwież dane teraz", fontSize = dimenResourceAsSp(R.dimen.text_size_small))
    }
    Spacer(modifier = Modifier.height(24.dp))
}