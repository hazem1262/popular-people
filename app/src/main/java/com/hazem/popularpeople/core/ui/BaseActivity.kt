package com.hazem.popularpeople.core.ui

import ae.government.tamm.components.shared.dialogs.SimpleErrorDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.hazem.popularpeople.R
import com.hazem.popularpeople.core.network.ConnectivityReceiver
import com.hazem.popularpeople.core.viewModel.BaseViewModel
import com.hazem.popularpeople.di.components.AppComponent
import com.hazem.popularpeople.di.components.DaggerAppComponent
import com.tapadoo.alerter.Alerter
import javax.inject.Inject

open class BaseActivity<MBaseViewModel : BaseViewModel> : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener{


    @Inject
    lateinit var viewModel: MBaseViewModel

    var loadingAlter:Alerter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // register the connectivity receiver to handle network changes
        ConnectivityReceiver.connectivityReceiverListener = this

        // show the back btn in the tool bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        viewModel.loading.observe(this, Observer {
            if (it) showLoading()
            else hideLoading()
        })

        viewModel.error.observe(this, Observer {
            hideLoading()
            handleServerError(it)
        })
    }

    private fun hideLoading() {
        /*loadingAlter?.apply {
            Alerter.hide()
        }
        loadingAlter = null*/
    }

    private fun showLoading() {
        /*loadingAlter = Alerter.create(this).apply {
            setText("loading")
            setBackgroundColorRes(R.color.app_error_color)
            enableInfiniteDuration(true)
            enableProgress(true)
            show()
        }*/
    }


    fun showInternetError() {
        showError(getString(R.string.app_internet_error))
    }
    fun handleServerError(e:String){
        val dialog = SimpleErrorDialog().apply {
            setUi(e, R.string.retry, act = {
                dismiss()
                viewModel.retry(e)
            })
        }

        dialog.show(supportFragmentManager, "error_dialog")
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
        viewModel.clearSubscription()
    }

    // handle the tool bar back btn clicked
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
