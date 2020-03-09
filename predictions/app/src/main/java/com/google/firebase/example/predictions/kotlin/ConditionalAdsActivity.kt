package com.google.firebase.example.predictions.kotlin

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.firebase.example.predictions.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig

class ConditionalAdsActivity : AppCompatActivity() {

    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfig

    fun initRemoteConfig() {
        // [START pred_conditional_ads_init]
        firebaseRemoteConfig = Firebase.remoteConfig

        val remoteConfigDefaults = hashMapOf<String, Any>(
                "ads_enabled" to true
        )
        firebaseRemoteConfig.setDefaultsAsync(remoteConfigDefaults)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Default value successfully set
                    } else {
                        // Failed to set default value
                    }
                }
        // [END pred_conditional_ads_init]
    }

    fun fetchRemoteConfig() {
        // [START pred_conditional_ads_fetch]
        firebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Act on the retrieved parameters

                        // Show ads based on the ad policy retrieved with Remote Config
                        executeAdsPolicy()

                        // ...
                    } else {
                        // Handle errors
                    }
                }
        // [END pred_conditional_ads_fetch]
    }

    // [START pred_conditional_ads_policy]
    private fun executeAdsPolicy() {
        val showAds = firebaseRemoteConfig["ads_enabled"].asBoolean()
        val adView = findViewById<AdView>(R.id.adView)

        if (showAds) {
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
            adView.visibility = View.VISIBLE
        } else {
            adView.visibility = View.GONE
        }
    }
    // [END pred_conditional_ads_policy]
}
