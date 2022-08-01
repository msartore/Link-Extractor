package dev.msartore.linkextractor.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import dev.msartore.linkextractor.R

object Regex {
    val urlRegex = Regex("(http|ftp|https):\\/\\/([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:\\/~+#-]*[\\w@?^=%&\\/~+#-])")
}

fun urlFinder(text: String) =
    Regex.urlRegex.findAll(text).map { it.value }.toSet()

fun ClipboardManager.copyToClipboard(context: Context, text: String) {
    setPrimaryClip(
        ClipData.newPlainText(
            context.getString(R.string.url_clipboard), text
        )
    )
}