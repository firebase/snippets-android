package com.google.firebase.example.instanceid.kotlin

import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId

class MainActivity : AppCompatActivity() {

    fun logInstanceIdToken() {
        // [START log_iid_token]
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener { task -> Log.d("IID_TOKEN", task.result.token) }
        // [END log_iid_token]
    }

}
