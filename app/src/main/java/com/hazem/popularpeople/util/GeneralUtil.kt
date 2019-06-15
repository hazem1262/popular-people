package com.hazem.popularpeople.util

import android.content.Context

// make dynamic number of cells according to device orientation
fun calculateNoOfColumns(context: Context, columnWidthDp: Float): Int { // For example columnWidthDp=180
    val displayMetrics = context.resources.displayMetrics
    val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
    return (screenWidthDp / columnWidthDp + 0.5).toInt()
}

fun String.getImageUrl(): String = "http://image.tmdb.org/t/p/w185$this"
fun String.getOriginalImageUrl(): String = "http://image.tmdb.org/t/p/original$this"