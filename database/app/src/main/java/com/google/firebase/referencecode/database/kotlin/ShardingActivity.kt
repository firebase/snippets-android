package com.google.firebase.referencecode.database.kotlin

import android.support.v7.app.AppCompatActivity

import com.google.firebase.database.FirebaseDatabase

class ShardingActivity : AppCompatActivity() {

    private fun getMultipleInstances() {
        // [START rtdb_multi_instance]
        // Get the default database instance for an app
        val primary = FirebaseDatabase.getInstance().reference

        // Get a secondary database instance by URL
        val secondary = FirebaseDatabase
                .getInstance("https://testapp-1234.firebaseio.com").reference
        // [END rtdb_multi_instance]
    }
}
