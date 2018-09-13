package com.google.firebase.example.crash.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.crash.FirebaseCrash

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun logSimple() {
        // [START crash_log_simple]
        FirebaseCrash.log("Activity created")
        // [END crash_log_simple]
    }

    fun disableCollection() {
        // [START crash_disable_collection]
        FirebaseCrash.setCrashCollectionEnabled(false)
        // [END crash_disable_collection]
    }

    fun enableCollection() {
        // [START crash_enable_collection]
        FirebaseCrash.setCrashCollectionEnabled(true)
        // [END crash_enable_collection]
    }

}
