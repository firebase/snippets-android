package com.google.firebase.example.appcheck.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.example.appcheck.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun init() {
        // [START appcheck_initialize]
        Firebase.initialize(context = this)
        val firebaseAppCheck = Firebase.appCheck
        // [END appcheck_initialize]
    }

    private fun installSafetyNet() {
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        // [START appcheck_initialize_safetynet]
        firebaseAppCheck.installAppCheckProviderFactory(
            SafetyNetAppCheckProviderFactory.getInstance()
        )
        // [END appcheck_initialize_safetynet]
    }

    private fun installDebug() {
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        // [START appcheck_initialize_debug]
        firebaseAppCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        )
        // [END appcheck_initialize_debug]
    }

    private fun installPlayIntegrity() {
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        // [START appcheck_initialize_playintegrity]
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )
        // [END appcheck_initialize_playintegrity]
    }
}