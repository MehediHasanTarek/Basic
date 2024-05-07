package com.example.my_application

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.TextUtils
import android.text.format.Formatter
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.net.Inet4Address
import java.net.NetworkInterface


class MainActivity : AppCompatActivity() {

 //   private lateinit var appUpdateManager: AppUpdateManager

    private lateinit var textIp :TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

     //   appUpdateManager = AppUpdateManagerFactory.create(this)

        textIp = findViewById(R.id.text_ipAddress)
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        if(checkVPNStatus()) {

            Toast.makeText(this, "VPN is Connected ", Toast.LENGTH_SHORT).show()
        } else {

            Toast.makeText(this, "VPN Not Connected", Toast.LENGTH_SHORT).show()
        }
    }



    @SuppressLint("SetTextI18n")
    fun getNetworkInterfaceIpAddress() {
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val networkInterface = en.nextElement()
                val enumIpAddr = networkInterface.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        val ipAddress = inetAddress.getHostAddress()
                        if (!TextUtils.isEmpty(ipAddress)) {

                            textIp.text = "found vpn address: ${ipAddress}"
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            Log.e("IP Address", "getLocalIpAddress", ex)
        }

    }


    @SuppressLint("SuspiciousIndentation")
    private fun checkVPNStatus():Boolean{

    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return  if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            Log.d("VPN-RAJ", " ${network.hashCode()}")

            capabilities!= null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)


        } else {
            Log.d("VPN-RAJ", "in Old Android Version")

            Toast.makeText(this, "is Not VPN Connection", Toast.LENGTH_SHORT).show()
            connectivityManager.getNetworkInfo(TYPE_VPN)!!.isConnectedOrConnecting
        }

    }



/*    private fun checkUpdate(){

        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE

                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                // Request the update.
                appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // an activity result launcher registered via registerForActivityResult
                    activityResultLauncher,
                    // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                    // flexible updates.
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build())
            }
        }
    }

    private val  activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
        result: ActivityResult ->
        // handle callback
        if (result.resultCode != RESULT_OK) {

           Log.d("Update flow failed! Result code: ","${result.resultCode}")
        }
    }*/
}