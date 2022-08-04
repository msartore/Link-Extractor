package dev.msartore.linkextractor.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.webkit.URLUtil
import dev.msartore.linkextractor.R

object Regex {
    val urlRegex = Regex("(http|ftp|https):\\/\\/([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:\\/~+#-]*[\\w@?^=%&\\/~+#-])")
}

fun urlFinder(text: String): Collection<String> {

    val urls = Regex.urlRegex.findAll(text).map { it.value }.toSet()
    urls.filter { URLUtil.isValidUrl(it) }

    return urls
}

fun checkIfUrlIsMedia(url: String): Pair<Boolean, String> {

    var name = "LinkExtractor"
    var result = false

    url.split("/").last { string ->

        name = string

        result = string.endsWith(".jpg", true) ||
        string.endsWith(".jpeg", true) ||
        string.endsWith(".png", true) ||
        string.endsWith(".gif", true) ||
        string.endsWith(".mp4", true) ||
        string.endsWith(".webm", true) ||
        string.endsWith(".mp3", true) ||
        string.endsWith(".ogg", true) ||
        string.endsWith(".wav", true) ||
        string.endsWith(".flac", true) ||
        string.endsWith(".m4a", true) ||
        string.endsWith(".aac", true) ||
        string.endsWith(".wma", true) ||
        string.endsWith(".m4v", true) ||
        string.endsWith(".avi", true) ||
        string.endsWith(".mkv", true) ||
        string.endsWith(".mov", true) ||
        string.endsWith(".wmv", true) ||
        string.endsWith(".mpg", true) ||
        string.endsWith(".mpeg", true) ||
        string.endsWith(".3gp", true) ||
        string.endsWith(".3g2", true) ||
        string.endsWith(".flv", true) ||
        string.endsWith(".swf", true) ||
        string.endsWith(".swf", true) ||
        string.endsWith(".zip", true) ||
        string.endsWith(".rar", true) ||
        string.endsWith(".7z", true) ||
        string.endsWith(".tar", true) ||
        string.endsWith(".gz", true) ||
        string.endsWith(".bz2", true) ||
        string.endsWith(".bz", true) ||
        string.endsWith(".xz", true) ||
        string.endsWith(".z", true) ||
        string.endsWith(".tar.gz", true) ||
        string.endsWith(".tar.bz2", true) ||
        string.endsWith(".tar.xz", true) ||
        string.endsWith(".xml", true)

        true
    }

    return Pair(result, name)
}

fun ClipboardManager.copyToClipboard(context: Context, text: String) {
    setPrimaryClip(
        ClipData.newPlainText(
            context.getString(R.string.url_clipboard), text
        )
    )
}