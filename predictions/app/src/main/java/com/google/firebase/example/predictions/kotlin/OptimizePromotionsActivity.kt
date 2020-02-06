package com.google.firebase.example.predictions.kotlin

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import java.util.HashMap

class OptimizePromotionsActivity : AppCompatActivity() {

    private lateinit var config: FirebaseRemoteConfig
    private var promotedBundle: String? = null

    private fun initConfig() {
        // [START pred_optimize_promotions_init]
        config = FirebaseRemoteConfig.getInstance()

        val remoteConfigDefaults = hashMapOf<String, Any>(
                "promoted_bundle" to "basic"
        )
        config.setDefaultsAsync(remoteConfigDefaults)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Default value successfully set
                    } else {
                        // Failed to set default value
                    }
                }
        // [END pred_optimize_promotions_init]
    }

    private fun fetchConfig() {
        // [START pred_optimize_promotions_fetch]
        config.fetchAndActivate()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Act on the retrieved parameters

                        // Set the bundle to promote based on parameters retrieved with
                        // Remote Config. This depends entirely on your app, but for
                        // example, you might retrieve and use image assets based on the
                        // specified bundle name.
                        promotedBundle = config.getString("promoted_bundle")
                        // ...
                    }
                }
        // [END pred_optimize_promotions_fetch]
    }
}
