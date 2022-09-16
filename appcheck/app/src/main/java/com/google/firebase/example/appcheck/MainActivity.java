package com.google.firebase.example.appcheck;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void init() {
        // [START appcheck_initialize]
        FirebaseApp.initializeApp(/*context=*/ this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        // [END appcheck_initialize]
    }

    private void installSafetyNet() {
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        // [START appcheck_initialize_safetynet]
        firebaseAppCheck.installAppCheckProviderFactory(
                SafetyNetAppCheckProviderFactory.getInstance());
        // [END appcheck_initialize_safetynet]
    }

    private void installDebug() {
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        // [START appcheck_initialize_debug]
        firebaseAppCheck.installAppCheckProviderFactory(
                DebugAppCheckProviderFactory.getInstance());
        // [END appcheck_initialize_debug]
    }

    private void installPlayIntegrity() {
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        // [START appcheck_initialize_debug]
        firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance());
        // [END appcheck_initialize_debug]
    }
}