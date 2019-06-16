package com.hazem.popularpeople.core

import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hazem.popularpeople.R
import com.tapadoo.alerter.Alerter

open class BaseActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // register the connectivity receiver to handle network changes
        ConnectivityReceiver.connectivityReceiverListener = this

        // show the back btn in the tool bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun showInternetError() {
        showError(getString(R.string.app_internet_error))
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
