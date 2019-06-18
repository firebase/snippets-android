package com.google.firebase.example.instanceid.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId

class MainActivity : AppCompatActivity() {

    fun logInstanceIdToken() {
        // [START log_iid_token]
        FirebaseInstanceId.getInstance().instanceId
                .addOnSuccessListener { result ->
                    Log.d("IID_TOKEN", result.token)
                }
        // [END log_iid_token]
    }
}
