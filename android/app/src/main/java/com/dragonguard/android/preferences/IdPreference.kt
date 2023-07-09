package com.dragonguard.android.preferences

import android.content.Context
import android.content.SharedPreferences

class IdPreference (context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("id", Context.MODE_PRIVATE)

    fun getWalletAddress(defValue: String): String {
        return prefs.getString("wallet_address", defValue)!!
    }
    fun setWalletAddress(address: String) {
        prefs.edit().putString("wallet_address", address).apply()
    }

    fun getKey(defValue: String):String {
        return prefs.getString("key", defValue)!!
    }

    fun setKey(requestKey:String) {
        prefs.edit().putString("key", requestKey).apply()
    }

    fun setJwtToken(requestKey:String) {
        prefs.edit().putString("token", requestKey).apply()
    }

    fun getJwtToken(defValue: String):String {
        return prefs.getString("token", defValue)!!
    }

    fun setRefreshToken(requestKey:String) {
        prefs.edit().putString("refresh", requestKey).apply()
    }

    fun getRefreshToken(defValue: String):String {
        return prefs.getString("refresh", defValue)!!
    }

    fun setPostAddress(value: Boolean) {
        prefs.edit().putBoolean("post", value).apply()
    }

    fun getPostAddress(defValue: Boolean):Boolean {
        return prefs.getBoolean("post", defValue)!!
    }

    fun setRepeat(value: Boolean) {
        prefs.edit().putBoolean("repeat", value).apply()
    }

    fun getRepeat(defValue: Boolean):Boolean {
        return prefs.getBoolean("repeat", defValue)!!
    }
}