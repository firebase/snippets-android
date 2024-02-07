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

 Firebase Android Snippets

This repository holds code snippets used in Android documentation
on [firebase.google.com](https://firebase.google.com/docs/).

## Contributing

We love contributions! See [CONTRIBUTING.md](./CONTRIBUTING.md) for guidelines.


[![Actions Status][gh-actions-badge]][gh-actions]

[gh-actions