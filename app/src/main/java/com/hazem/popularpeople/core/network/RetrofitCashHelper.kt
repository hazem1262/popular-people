package com.hazem.popularpeople.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import okhttp3.Cache

class RetrofitCashHelper(var context:Context) {
    private val cacheSize = (5 * 1024 * 1024).toLong()      // limit the cache size to 5MB
    val myCache = Cache(context.cacheDir, cacheSize)

    fun hasNetwork(): Boolean? {
        var isConnected: Boolean? = false // Initial Value
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }
}