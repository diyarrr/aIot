package com.kg.mobilecomapp.utility

import android.content.Context
import android.util.Log
import javax.inject.Singleton

object SharedPreferencesIpAddress {
    private var isSaved : Boolean = false
    fun getIsSaved(): Boolean {
        return isSaved
    }
    fun saveIpAddress(context: Context, ipAddress: String) {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        isSaved = true
        Log.d("SharedPreferencesIpAddress", "saveIpAddress: $ipAddress")
        with(sharedPreferences.edit()) {
            putString("ip_address", ipAddress)
            apply() // Save the IP address
        }
    }

    fun getSavedIpAddress(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString(
            "ip_address",
            null
        ) // Return null if no IP address is saved
    }

    fun clearSavedIpAddress(context: Context) {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        isSaved = false
        with(sharedPreferences.edit()) {
            remove("ip_address")
            apply() // Clear the saved IP address
        }
    }
}