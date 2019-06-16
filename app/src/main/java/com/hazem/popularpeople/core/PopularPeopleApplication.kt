package com.hazem.popularpeople.core

import android.app.Application
import android.content.IntentFilter
import com.hazem.popularpeople.R

class PopularPeopleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // register the broad cast receiver
        registerReceiver(ConnectivityReceiver(), IntentFilter(resources.getString(R.string.changeAction)))
    }
}