package com.google.firebase.example.predictions.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.activity_main.adView

class MainActivity : AppCompatActivity() {

    fun configShowAds() {
        val cacheExpiration = 60L

        // [START pred_config_show_ads]
        val config = FirebaseRemoteConfig.getInstance()

        val remoteConfigDefaults = HashMap<String, Any>()
        remoteConfigDefaults["ads_policy"] = "ads_never"
        config.setDefaults(remoteConfigDefaults)

        // ...

        config.fetch(cacheExpiration)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        config.activateFetched()
                    }

                    // Act on the retrieved parameters

                    // Show ads based on the ad policy retrieved with Remote Config
                    executeAdsPolicy()

                    // ...
                }
        // [END pred_config_show_ads]
    }

    private fun executeAdsPolicy() {
        // [START pred_ads_policy]
        val config = FirebaseRemoteConfig.getInstance()
        val adPolicy = config.getString("ads_policy")
        val willNotSpend = config.getBoolean("will_not_spend")

        if (adPolicy == "ads_always" || adPolicy == "ads_nonspenders" && willNotSpend) {
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
            adView.visibility = View.VISIBLE
        } else {
            adView.visibility = View.GONE
        }

        FirebaseAnalytics.getInstance(this).logEvent("ads_policy_set", Bundle())
        // [END pred_ads_policy]
    }

    fun configPromoStrategy() {
        val cacheExpiration = 60L

        // [START config_promo_strategy]
        val config = FirebaseRemoteConfig.getInstance()

        val remoteConfigDefaults = HashMap<String, Any>()
        remoteConfigDefaults["promoted_bundle"] = "basic"
        config.setDefaults(remoteConfigDefaults)

        // ...

        config.fetch(cacheExpiration)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        config.activateFetched()
                    }

                    // Act on the retrieved parameters
                    val promotedBundle = getPromotedBundle()
                    // ...
                }
        // [END config_promo_strategy]
    }

    // [START pred_get_promoted_bundle]
    private fun getPromotedBundle(): String {
        FirebaseAnalytics.getInstance(this).logEvent("promotion_set", Bundle())

        val config = FirebaseRemoteConfig.getInstance()
        val promotedBundle = config.getString("promoted_bundle")
        val willSpend = config.getBoolean("predicted_will_spend")

        return if (promotedBundle == "predicted" && willSpend) {
            "premium"
        } else {
            promotedBundle
        }
    }
    // [END pred_get_promoted_bundle]

    fun configPreventChurn() {
        val cacheExpiration = 60L

        // [START pred_config_prevent_churn]
        val config = FirebaseRemoteConfig.getInstance()

        val remoteConfigDefaults = hashMapOf<String, Any>()
        remoteConfigDefaults["gift_policy"] = "gift_never"
        config.setDefaults(remoteConfigDefaults)

        // ...

        config.fetch(cacheExpiration)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        config.activateFetched()
                    }

                    // Act on the retrieved parameters
                    executeGiftPolicy()
                    // ...
                }
        // [END pred_config_prevent_churn]
    }

    // [START pred_execute_gift_policy]
    private fun executeGiftPolicy() {
        val config = FirebaseRemoteConfig.getInstance()
        val giftPolicy = config.getString("gift_policy")
        val willChurn = config.getBoolean("will_churn")

        if (giftPolicy == "gift_achievement") {
            grantGiftOnLevel2()
        } else if (giftPolicy == "gift_likelychurn" && willChurn) {
            grantGiftNow()
        }

        FirebaseAnalytics.getInstance(this).logEvent("gift_policy_set", Bundle())
    }
    // [END pred_execute_gift_policy]

    private fun grantGiftOnLevel2() {
        // Nothing
    }

    private fun grantGiftNow() {
        // Nothing
    }
}
