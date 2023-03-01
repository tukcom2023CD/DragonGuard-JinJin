package com.dragonguard.android.connect

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.widget.Toast

class NetworkCheck {

    companion object {
        fun checkNetworkState(context: Context): Boolean {
            val connectivityManager: ConnectivityManager = context.getSystemService(ConnectivityManager::class.java)
            val network: Network = connectivityManager.activeNetwork ?: return false
            val actNetwork: NetworkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
//                    Toast.makeText(context, "인터넷 연결됨!!", Toast.LENGTH_SHORT).show()
                    true
                }
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
//                    Toast.makeText(context, "인터넷 연결됨!!", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> {
                    Toast.makeText(context, "인터넷을 연결하세요!!", Toast.LENGTH_SHORT).show()
                    false
                }
            }
        }
    }

}