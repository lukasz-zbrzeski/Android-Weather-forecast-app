package com.example.weatherapp


import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("settings")

val REFRESH_INTERVAL_KEY = intPreferencesKey("refresh_interval")
val UNIT_SYSTEM_KEY = stringPreferencesKey("unit_system")

class PreferencesManager(private val context: Context) {
    companion object {
        val FAVORITES_KEY = stringSetPreferencesKey("favorite_locations")
        val LAST_LOCATION_KEY = stringPreferencesKey("last_location")
    }

    val favorites: Flow<Set<String>> = context.dataStore.data.map {
        it[FAVORITES_KEY] ?: setOf("Warsaw")
    }

    suspend fun saveFavorites(locations: Set<String>) {
        context.dataStore.edit { it[FAVORITES_KEY] = locations }
    }

    suspend fun saveLastLocation(location: String) {
        context.dataStore.edit { it[LAST_LOCATION_KEY] = location }
    }

    val lastLocation: Flow<String?> = context.dataStore.data.map {
        it[LAST_LOCATION_KEY]
    }

    val refreshInterval: Flow<Int> = context.dataStore.data.map {
        it[REFRESH_INTERVAL_KEY] ?: 3
    }

    suspend fun setRefreshInterval(hours: Int) {
        context.dataStore.edit {
            it[REFRESH_INTERVAL_KEY] = hours
        }
    }

    val unitSystem: Flow<String> = context.dataStore.data.map {
        it[UNIT_SYSTEM_KEY] ?: "metric"
    }

    suspend fun setUnitSystem(unit: String) {
        context.dataStore.edit {
            it[UNIT_SYSTEM_KEY] = unit
        }
    }

}
