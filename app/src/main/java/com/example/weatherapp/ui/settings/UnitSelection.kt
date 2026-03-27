package com.example.weatherapp.ui.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R

@Composable
fun UnitSelection(
    selectedUnit: String,
    onUnitChange: (String) -> Unit,
    onChangePersisted: (String) -> Unit
) {
    Spacer(modifier = Modifier.height(16.dp))
    Text("Jednostki:", fontSize = dimenResourceAsSp(R.dimen.text_size_large))
    Row {
        RadioButton(selected = selectedUnit == "metric", onClick = {
            onUnitChange("metric")
            onChangePersisted("metric")
        })
        Text("Metric (°C, m/s)", fontSize = dimenResourceAsSp(R.dimen.text_size_medium))

        Spacer(modifier = Modifier.width(16.dp))

        RadioButton(selected = selectedUnit == "imperial", onClick = {
            onUnitChange("imperial")
            onChangePersisted("imperial")
        })
        Text("Imperial (°F, mph)", fontSize = dimenResourceAsSp(R.dimen.text_size_medium))
    }
}