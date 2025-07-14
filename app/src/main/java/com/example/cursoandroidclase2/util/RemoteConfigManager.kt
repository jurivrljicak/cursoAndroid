package com.example.cursoandroidclase2.util

import android.util.Log
import com.example.cursoandroidclase2.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

object RemoteConfigManager {

    private val TAG = RemoteConfigManager :: class.java.simpleName

    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    init {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0) // en producción usar 3600 o más
            .build()

        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }

     fun fetchAndActivate(onComplete: (Boolean) -> Unit) {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Fetch and activate succeeded")
                    logAllValues()
                    onComplete(true)
                } else {
                    Log.e(TAG, "Fetch failed", task.exception)
                    onComplete(false)
                }
            }
    }

    fun getString(key: String): String = remoteConfig.getString(key)

    fun getBoolean(key: String): Boolean = remoteConfig.getBoolean(key)

    private fun logAllValues() {
        val label = getString("label")
        Log.d(TAG, "logAllValues : $label")
    }


}