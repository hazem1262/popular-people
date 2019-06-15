package com.hazem.popularpeople.util

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment

// The path of the media directory relative to the external directory.
fun getMediaFilePath() = Environment.DIRECTORY_DCIM + "/Popular People/"

//https://developer.android.com/guide/background
fun downloadFile(context: Context, url: String, fileName: String ) {
    val request = DownloadManager.Request(Uri.parse(url))

    val downloadedFilePath = getMediaFilePath()
    request.setDestinationInExternalPublicDir(downloadedFilePath, fileName)
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
    val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    manager.enqueue(request)

}
