package com.google.firebase.example.appindexing.kotlin

import android.net.ParseException
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.appindexing.AndroidAppUri

// [START appindexing_measure_activity]
class MeasureActivity : AppCompatActivity() {

    override fun getReferrer(): Uri? {

        // There is a built in function available from SDK>=22
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return super.getReferrer()
        }

        val intent = intent
        val referrer = intent?.extras?.get("android.intent.extra.REFERRER") as Uri?
        if (referrer != null) {
            return referrer
        }

        val referrerName = intent.getStringExtra("android.intent.extra.REFERRER_NAME")

        if (referrerName != null) {
            try {
                return Uri.parse(referrerName)
            } catch (e: ParseException) {
                // ...
            }
        }

        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ...
        val referrer = referrer

        if (referrer != null) {
            if (referrer.scheme == "http" || referrer.scheme == "https") {
                // App was opened from a browser
                // host will contain the host path (e.g. www.google.com)
                val host = referrer.host

                // Add analytics code below to track this click from web Search
                // ...
            } else if (referrer.scheme == "android-app") {
                // App was opened from another app
                val appUri = AndroidAppUri.newAndroidAppUri(referrer)
                val referrerPackage = appUri.packageName
                if ("com.google.android.googlequicksearchbox" == referrerPackage) {
                    // App was opened from the Google app
                    // host will contain the host path (e.g. www.google.com)
                    val host = appUri.deepLinkUri.host

                    // Add analytics code below to track this click from the Google app
                    // ...
                } else if ("com.google.appcrawler" == referrerPackage) {
                    // Make sure this is not being counted as part of app usage
                    // ...
                }
            }
        }
        // ...
    }
}
// [END appindexing_measure_activity]
