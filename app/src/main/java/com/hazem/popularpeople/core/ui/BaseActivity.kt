package com.hazem.popularpeople.core.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hazem.popularpeople.R
import com.hazem.popularpeople.core.network.ConnectivityReceiver
import com.hazem.popularpeople.di.components.AppComponent
import com.hazem.popularpeople.di.components.DaggerAppComponent
import com.tapadoo.alerter.Alerter
import java.lang.Exception

open class BaseActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener{



    lateinit var appComponent: AppComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // init dagger2
        appComponent = initializeActivityComponent()
        // register the connectivity receiver to handle network changes
        ConnectivityReceiver.connectivityReceiverListener = this

        // show the back btn in the tool bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun initializeActivityComponent(): AppComponent {
        return DaggerAppComponent.builder()
            .build()
    }

    fun showInternetError() {
        showError(getString(R.string.app_internet_error))
    }
    fun handleServerError(e:Exception){
        // remove "java exception: " added by Exception class
        showError(e.message?.replaceBefore(":","")
            ?.replace(":","")
            ?:getString(R.string.error))
    }

    private fun showError(errorMessage: String) {
        Alerter.create(this).apply {
            setText(errorMessage)
            setBackgroundColorRes(R.color.app_error_color)
            show()
        }
    }
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (!isConnected){
            showInternetError()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // unsubscribe from the connection
        ConnectivityReceiver.connectivityReceiverListener = null
    }

    // handle the tool bar back btn clicked
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
