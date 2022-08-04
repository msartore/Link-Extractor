package dev.msartore.linkextractor.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

suspend fun DataStore<Preferences>.write(key: String, value: Boolean) {

    edit { settings ->
        settings[booleanPreferencesKey(key)] = value
    }
}

suspend fun DataStore<Preferences>.read(key: String) =
    data.map { it.toPreferences() }.first()[booleanPreferencesKey(key)]
