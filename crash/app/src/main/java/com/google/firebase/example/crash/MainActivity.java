package com.google.firebase.example.crash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.crash.FirebaseCrash;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void logSimple() {
        // [START crash_log_simple]
        FirebaseCrash.log("Activity created");
        // [END crash_log_simple]
    }

    public void disableCollection() {
        // [START crash_disable_collection]
        FirebaseCrash.setCrashCollectionEnabled(false);
        // [END crash_disable_collection]
    }

    public void enableCollection() {
        // [START crash_enable_collection]
        FirebaseCrash.setCrashCollectionEnabled(true);
        // [END crash_enable_collection]
    }

}
