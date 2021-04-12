package com.google.firebase.example.admob.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {

    // [START ads_on_create]
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ...
        MobileAds.initialize(this)
    }
    // [END ads_on_create]
}
