package com.altnik.downloader

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment

object DownloadHelper {

    fun enqueue(context: Context, fileUrl: String, fileName: String): Long {
        val req = DownloadManager.Request(Uri.parse(fileUrl))
            .setTitle(fileName)
            .setDescription("Altnik Downloader")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
            // Saved to Downloads/AltnikDownloader/fileName — visible in Gallery/Files
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "AltnikDownloader/$fileName"
            )
        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        return dm.enqueue(req)
    }

    fun makeFileName(url: String, isVideo: Boolean): String {
        val ext = if (isVideo) "mp4" else "jpg"
        return "altnik_${System.currentTimeMillis()}.$ext"
    }
}
