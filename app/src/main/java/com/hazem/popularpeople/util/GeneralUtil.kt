package com.hazem.popularpeople.util

import android.content.Context

// make dynamic number of cells according to device orientation
fun calculateNoOfColumns(context: Context, columnWidthDp: Float): Int { // For example columnWidthDp=180
    val displayMetrics = context.resources.displayMetrics
    val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
    return (screenWidthDp / columnWidthDp + 0.5).toInt()
}