package ae.government.tamm.util


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.PowerManager
import android.util.Log


/**
 * Created by Caroline on 5/10/2018.
 * http://devdeeds.com/android-kotlin-listen-to-internet-connection-using-broadcastreceiver/
 * can be improved by
 */

class ConnectivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, arg1: Intent) {
	    connectivityReceiverListener?.onNetworkConnectionChanged(isConnectedOrConnecting(context))
    }

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    companion object {

	    private const val PING_RETRY = 3
	    private const val INITIAL_DELAY_MILLIS = 250L
	    private const val CHECK_INTERVAL_MILLIS = 5000L

        var connectivityReceiverListener: ConnectivityReceiverListener? = null

	    fun pingWithRetries(): Boolean {
		    for (i in 0 until PING_RETRY) {

			    if (ping()) { // run the ping outside of UI thread
				    return true
			    }
		    }

		    return false
	    }
	    fun ping(): Boolean {
		    return try {
			    val p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 8.8.8.8")
			    val returnVal = p1.waitFor()
			    returnVal == 0

		    } catch (e: Exception) {
			    e.printStackTrace()
			    false
		    }
	    }

	    fun isConnected(context: Context): Boolean {
		    Log.d(ConnectivityReceiver::class.java.simpleName, "isConnected -- isInteractive: ${isInteractive(context)} - isConnected: ${isConnectedOrConnecting(context)}")

		    // only test the connectivity if the device is awake
		    // some devices turnoff network access to apps that have been in the background for too long!
		    return if (isInteractive(context)) isConnectedOrConnecting(context) else true
	    }

	    private fun isConnectedOrConnecting(context: Context): Boolean {
		    val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		    val networkInfo = connMgr.activeNetworkInfo

		    // it appears that calling the function twice solves the problem!! (some devices turnoff network access to apps that have been in the background for too long)
		    Log.d(ConnectivityReceiver::class.java.simpleName, "isConnectedOrConnecting -- isInteractive: ${isInteractive(context)} - isConnected: ${networkInfo != null && networkInfo.isConnectedOrConnecting}")
		    return if (isInteractive(context)) networkInfo != null && networkInfo.isConnectedOrConnecting else true
	    }

	    // Checks if the device is awake
	    private fun isInteractive(context: Context): Boolean {
		    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

		    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			    powerManager.isInteractive && !powerManager.isDeviceIdleMode

		    } else {
			    powerManager.isInteractive
		    }
	    }

	    fun isConnectedWiFi(context: Context): Boolean {
		    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

		    return activeNetwork?.type == ConnectivityManager.TYPE_WIFI
	    }
    }
}
