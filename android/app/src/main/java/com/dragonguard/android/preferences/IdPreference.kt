package com.dragonguard.android.preferences

import android.content.Context
import android.content.SharedPreferences

class IdPreference (context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("id", Context.MODE_PRIVATE)

    fun getId(key: String, defValue: Int): Int {
        return prefs.getInt(key, defValue)
    }

    fun setId(key: String, id: Int) {
        prefs.edit().putInt(key, id).apply()
    }

    fun getWalletAddress(key:String, defValue: String): String {
        return prefs.getString(key, defValue)!!
    }
    fun setWalletAddress(key: String, address: String) {
        prefs.edit().putString(key, address).apply()
    }
}