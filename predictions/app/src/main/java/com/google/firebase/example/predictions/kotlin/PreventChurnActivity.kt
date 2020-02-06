package com.google.firebase.example.predictions.kotlin

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import java.util.HashMap

class PreventChurnActivity : AppCompatActivity() {

    private fun preventChurn() {
        // [START pred_prevent_churn]
        val config = FirebaseRemoteConfig.getInstance()

        val remoteConfigDefaults = hashMapOf<String, Any>(
                "grant_retention_gift" to false
        )
        config.setDefaultsAsync(remoteConfigDefaults)
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    config.fetchAndActivate()
                }
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Act on the retrieved parameters. For example, grant the
                        // retention gift to players who haven't yet received one.
                        val shouldGrantGift = config.getBoolean("grant_retention_gift")
                        if (shouldGrantGift && !playerAlreadyReceivedGift()) {
                            grantGiftToPlayer()
                        }
                    } else {
                        // Handle errors
                    }
                }
        // [END pred_prevent_churn]
    }

    private fun playerAlreadyReceivedGift(): Boolean {
        // Dummy method
        return false
    }

    private fun grantGiftToPlayer() {
        // Dummy method
    }
}
