package com.google.firebase.example.admob

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds

class KotlinMainActivity : AppCompatActivity() {

    // [START ads_on_create]
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ...
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "YOUR_ADMOB_APP_ID")
    }
    // [END ads_on_create]

}