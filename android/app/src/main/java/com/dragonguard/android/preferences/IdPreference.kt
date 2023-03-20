package com.dragonguard.android.preferences

import android.content.Context
import android.content.SharedPreferences

class IdPreference (context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("id", Context.MODE_PRIVATE)

    fun getWalletAddress(key:String, defValue: String): String {
        return prefs.getString(key, defValue)!!
    }
    fun setWalletAddress(key: String, address: String) {
        prefs.edit().putString(key, address).apply()
    }

    fun getKey(key:String, defValue: String):String {
        return prefs.getString(key, defValue)!!
    }

    fun setKey(key:String, requestKey:String) {
        prefs.edit().putString(key, requestKey).apply()
    }

    fun setJwtToken(key:String, requestKey:String) {
        prefs.edit().putString(key, requestKey).apply()
    }

    fun getJwtToken(key:String, defValue: String):String {
        return prefs.getString(key, defValue)!!
    }

    fun setRefreshToken(key:String, requestKey:String) {
        prefs.edit().putString(key, requestKey).apply()
    }

    fun getRefreshToken(key:String, defValue: String):String {
        return prefs.getString(key, defValue)!!
    }

    fun setPostAddress(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun getPostAddress(key:String, defValue: Boolean):Boolean {
        return prefs.getBoolean(key, defValue)!!
    }

}