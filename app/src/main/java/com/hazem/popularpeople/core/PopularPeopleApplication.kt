package com.hazem.popularpeople.core

import android.app.Activity
import android.app.Application
import android.content.IntentFilter
import com.hazem.popularpeople.R
import com.hazem.popularpeople.core.network.ConnectivityReceiver
import com.hazem.popularpeople.core.network.RetrofitCashHelper
import com.hazem.popularpeople.di.utility.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class PopularPeopleApplication : Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingAndroidInjector
    }

    companion object{
        var retrofitCashHelper: RetrofitCashHelper? = null
    }
    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
        // register the broad cast receiver
        registerReceiver(ConnectivityReceiver(), IntentFilter(resources.getString(R.string.changeAction)))
        retrofitCashHelper = RetrofitCashHelper(applicationContext)
    }
}