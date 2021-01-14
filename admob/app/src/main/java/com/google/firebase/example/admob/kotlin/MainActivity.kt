package com.google.firebase.example.admob.kotlin

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import devrel.firebase.google.com.firebaseoptions.R

class MainActivity : AppCompatActivity() {

    private lateinit var adView: AdView
    private lateinit var interstitialAd: InterstitialAd
    private lateinit var loadInterstitialButton: Button
    private val context = this

    // [START ads_on_create]
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ...
        MobileAds.initialize(this)
    }
    // [END ads_on_create]

    private fun loadAdBanner() {
        // [SNIPPET load_banner_ad]
        // Load an ad into the AdView.
        // [START load_banner_ad]

        // Initialize the Google Mobile Ads SDK
        MobileAds.initialize(context)

        val adRequest = AdRequest.Builder().build()

        adView.loadAd(adRequest)
        // [END load_banner_ad]
    }

    private fun initInterstitialAd() {
        // [START instantiate_interstitial_ad]
        // Create an InterstitialAd object. This same object can be re-used whenever you want to
        // show an interstitial.
        interstitialAd = InterstitialAd(context)
        interstitialAd.adUnitId = getString(R.string.interstitial_ad_unit_id)
        // [END instantiate_interstitial_ad]
    }

    private fun createAdListener() {
        // [START create_interstitial_ad_listener]
        interstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                requestNewInterstitial()
                beginSecondActivity()
            }

            override fun onAdLoaded() {
                // Ad received, ready to display
                // ...
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                Log.w(TAG, "onAdFailedToLoad: ${error.message}")
            }
        }
        // [END create_interstitial_ad_listener]
    }

    private fun displayInterstitialAd() {
        // [START display_interstitial_ad]
        loadInterstitialButton.setOnClickListener {
            if (interstitialAd.isLoaded) {
                interstitialAd.show()
            } else {
                beginSecondActivity()
            }
        }
        // [END display_interstitial_ad]
    }

    /**
     * Load a new interstitial ad asynchronously.
     */
    // [START request_new_interstitial]
    private fun requestNewInterstitial() {
        val adRequest = AdRequest.Builder()
                .build()

        interstitialAd.loadAd(adRequest)
    }
    // [END request_new_interstitial]

    private fun beginSecondActivity() { }

    // [START add_lifecycle_methods]
    /** Called when leaving the activity  */
    public override fun onPause() {
        adView.pause()
        super.onPause()
    }

    /** Called when returning to the activity  */
    public override fun onResume() {
        super.onResume()
        adView.resume()
        if (!interstitialAd.isLoaded) {
            requestNewInterstitial()
        }
    }

    /** Called before the activity is destroyed  */
    public override fun onDestroy() {
        adView.destroy()
        super.onDestroy()
    }
    // [END add_lifecycle_methods]

    companion object {
        private const val TAG = "MainActivity"
    }
}
