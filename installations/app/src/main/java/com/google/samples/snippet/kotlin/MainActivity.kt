package com.google.samples.snippet.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.installations.FirebaseInstallations
import com.google.samples.snippet.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        logInstallationAuthToken()
        logInstallationID()
    }

    private fun logInstallationAuthToken() {
        // [START get_installation_token]
        FirebaseInstallations.getInstance().getToken(/* forceRefresh */ true)
            .addOnCompleteListener {task ->
                if (task.isComplete) {
                    Log.d("Installations", "Installation auth token: " + task.result?.token)
                }
            }
        // [END get_installation_token]
    }

    private fun logInstallationID() {
        // [START get_installation_id]
        FirebaseInstallations.getInstance().id.addOnCompleteListener { task ->
            if (task.isComplete) {
                Log.d("Installations", "Installation ID: " + task.result)
            }
        }
        // [END get_installation_id]
    }
}