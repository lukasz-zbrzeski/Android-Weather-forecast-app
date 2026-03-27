package com.example.weatherapp.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.weatherapp.PreferencesManager
import com.example.weatherapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun RefreshIntervalSelection(
    prefs: PreferencesManager,
    coroutineScope: CoroutineScope,
    refreshIntervals: List<Int>,
    selectedIntervalState: MutableState<Int>,
    onRefreshIntervalChange: (Int) -> Unit
) {
    val configuration = LocalConfiguration.current
    val minDimension = minOf(configuration.screenWidthDp, configuration.screenHeightDp)
    val isTablet = minDimension >= 600
    val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT

    Spacer(modifier = Modifier.height(16.dp))
    Text("Automatyczne odświeżanie co:", fontSize = dimenResourceAsSp(R.dimen.text_size_large))

    if (!isTablet && !isPortrait) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                listOf(1, 3, 6).forEach { hours ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedIntervalState.value == hours,
                            onClick = {
                                selectedIntervalState.value = hours
                                coroutineScope.launch {
                                    prefs.setRefreshInterval(hours)
                                    onRefreshIntervalChange(hours)
                                }
                            }
                        )
                        Text("$hours godz.", fontSize = dimenResourceAsSp(R.dimen.text_size_medium))
                    }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                listOf(12, 24).forEach { hours ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedIntervalState.value == hours,
                            onClick = {
                                selectedIntervalState.value = hours
                                coroutineScope.launch {
                                    prefs.setRefreshInterval(hours)
                                    onRefreshIntervalChange(hours)
                                }
                            }
                        )
                        Text("$hours godz.", fontSize = dimenResourceAsSp(R.dimen.text_size_medium))
                    }
                }
            }
        }
    } else {
        refreshIntervals.forEach { hours ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedIntervalState.value == hours,
                    onClick = {
                        selectedIntervalState.value = hours
                        coroutineScope.launch {
                            prefs.setRefreshInterval(hours)
                            onRefreshIntervalChange(hours)
                        }
                    }
                )
                Text("$hours godz.", fontSize = dimenResourceAsSp(R.dimen.text_size_medium))
            }
        }
    }
}