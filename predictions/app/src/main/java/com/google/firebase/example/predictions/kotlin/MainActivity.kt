package com.google.firebase.example.predictions.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.example.predictions.R
import com.google.firebase.example.predictions.interfaces.MainActivityInterface
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import java.util.*

class MainActivity : AppCompatActivity(), MainActivityInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun configShowAds() {
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

    override fun executeAdsPolicy() {
        // [START pred_ads_policy]
        val config = FirebaseRemoteConfig.getInstance()
        val adPolicy = config.getString("ads_policy")
        val will_not_spend = config.getBoolean("will_not_spend")
        val mAdView = findViewById<View>(R.id.adView) as AdView

        if (adPolicy == "ads_always" || adPolicy == "ads_nonspenders" && will_not_spend) {
            val adRequest = AdRequest.Builder().build()
            mAdView.loadAd(adRequest)
            mAdView.visibility = View.VISIBLE
        } else {
            mAdView.visibility = View.GONE
        }

        FirebaseAnalytics.getInstance(this).logEvent("ads_policy_set", Bundle())
        // [END pred_ads_policy]
    }

    override fun configPromoStrategy() {
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
    override fun getPromotedBundle(): String {
        FirebaseAnalytics.getInstance(this).logEvent("promotion_set", Bundle())

        val config = FirebaseRemoteConfig.getInstance()
        val promotedBundle = config.getString("promoted_bundle")
        val will_spend = config.getBoolean("predicted_will_spend")

        return if (promotedBundle == "predicted" && will_spend) {
            "premium"
        } else {
            promotedBundle
        }
    }
    // [END pred_get_promoted_bundle]

    override fun configPreventChurn() {
        val cacheExpiration = 60L

        // [START pred_config_prevent_churn]
        val config = FirebaseRemoteConfig.getInstance()

        val remoteConfigDefaults = HashMap<String, Any>()
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
    override fun executeGiftPolicy() {
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

    override fun grantGiftOnLevel2() {
        // Nothing
    }

    override fun grantGiftNow() {
        // Nothing
    }

}
