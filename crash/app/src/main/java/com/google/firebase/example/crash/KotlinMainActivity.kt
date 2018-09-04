package com.google.firebase.example.crash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.crash.FirebaseCrash
import com.google.firebase.example.crash.interfaces.MainActivityInterface

class KotlinMainActivity : AppCompatActivity(), MainActivityInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun logSimple() {
        // [START crash_log_simple]
        FirebaseCrash.log("Activity created")
        // [END crash_log_simple]
    }

    override fun disableCollection() {
        // [START crash_disable_collection]
        FirebaseCrash.setCrashCollectionEnabled(false)
        // [END crash_disable_collection]
    }

    override fun enableCollection() {
        // [START crash_enable_collection]
        FirebaseCrash.setCrashCollectionEnabled(true)
        // [END crash_enable_collection]
    }

}