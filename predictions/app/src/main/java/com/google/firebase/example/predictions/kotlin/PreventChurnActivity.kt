package com.google.firebase.example.predictions.kotlin

import android.support.v7.app.AppCompatActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import java.util.HashMap

class PreventChurnActivity : AppCompatActivity() {

    private fun preventChurn() {
        // [START pred_prevent_churn]
        val config = FirebaseRemoteConfig.getInstance()

        val remoteConfigDefaults = HashMap<String, Any>()
        remoteConfigDefaults["grant_retention_gift"] = false
        config.setDefaults(remoteConfigDefaults)

        // ...

        config.fetch(CACHE_EXPIRATION)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        config.activateFetched()
                    }

                    // Act on the retrieved parameters. For example, grant the
                    // retention gift to players who haven't yet received one.
                    val shouldGrantGift = config.getBoolean("grant_retention_gift")
                    if (shouldGrantGift && !playerAlreadyReceivedGift()) {
                        grantGiftToPlayer()
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

    companion object {
        private val CACHE_EXPIRATION = (60 * 1000).toLong()
    }

}
