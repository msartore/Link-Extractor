package dev.msartore.linkextractor.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dev.msartore.linkextractor.utils.read
import dev.msartore.linkextractor.utils.write

class Settings(
    private val dataStore: DataStore<Preferences>
) {

    var downloadMedia: MutableState<Boolean> = mutableStateOf(false)
    var startApp: MutableState<Boolean> = mutableStateOf(false)

    suspend fun update() {
        downloadMedia.value = dataStore.read(Keys.DownloadMedia.key) == true
        startApp.value = dataStore.read(Keys.StartApp.key) == true
    }

    suspend fun save() {
        dataStore.write(Keys.DownloadMedia.key, downloadMedia.value)
        dataStore.write(Keys.StartApp.key, startApp.value)
    }

    enum class Keys(val key: String) {
        DownloadMedia("download_media"),
        StartApp("start_app")
    }
}
