package com.google.firebase.example.predictions.kotlin

import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.firebase.example.predictions.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import java.util.HashMap

class ConditionalAdsActivity : AppCompatActivity() {

    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfig

    fun initRemoteConfig() {
        // [START pred_conditional_ads_init]
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        val remoteConfigDefaults = HashMap<String, Any>()
        remoteConfigDefaults["ads_enabled"] = "true"
        firebaseRemoteConfig.setDefaults(remoteConfigDefaults)
        // [END pred_conditional_ads_init]
    }

    fun fetchRemoteConfig() {
        // [START pred_conditional_ads_fetch]
        firebaseRemoteConfig.fetch(CACHE_EXPIRATION)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        firebaseRemoteConfig.activateFetched()
                    }

                    // Act on the retrieved parameters

                    // Show ads based on the ad policy retrieved with Remote Config
                    executeAdsPolicy()

                    // ...
                }
        // [END pred_conditional_ads_fetch]
    }

    // [START pred_conditional_ads_policy]
    private fun executeAdsPolicy() {
        val showAds = firebaseRemoteConfig.getBoolean("ads_enabled")
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

    companion object {
        private val CACHE_EXPIRATION = 60 * 1000L
    }
}
