package dev.msartore.linkextractor.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import dev.msartore.linkextractor.R


fun DownloadManager.downloadFile(url: String, fileName: String, context: Context) {

    DownloadManager.Request(Uri.parse(url)).apply {
        setAllowedOverRoaming(false)
        setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        setTitle(fileName)
        setDescription(context.getString(R.string.download_description) + fileName)
        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        enqueue(this)
    }

    Toast.makeText(context, context.getString(R.string.download_description) + fileName, Toast.LENGTH_LONG).show()
}