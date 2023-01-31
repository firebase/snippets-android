package com.google.firebase.example.appcheck.kotlin

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.AppCheckProvider
import com.google.firebase.appcheck.AppCheckProviderFactory
import com.google.firebase.appcheck.AppCheckToken
import com.google.firebase.appcheck.FirebaseAppCheck

class CustomProvider {
    // [START appcheck_custom_provider]
    class YourCustomAppCheckToken(
        private val token: String,
        private val expiration: Long
    ) : AppCheckToken() {
        override fun getToken(): String = token
        override fun getExpireTimeMillis(): Long = expiration
    }

    class YourCustomAppCheckProvider(firebaseApp: FirebaseApp) : AppCheckProvider {
        override fun getToken(): Task<AppCheckToken> {
            // Logic to exchange proof of authenticity for an App Check token and
            //   expiration time.
            // [START_EXCLUDE]
            val expirationFromServer = 0L
            val tokenFromServer = "token"
            // [END_EXCLUDE]

            // Refresh the token early to handle clock skew.
            val expMillis = expirationFromServer * 1000L - 60000L

            // Create AppCheckToken object.
            val appCheckToken: AppCheckToken = YourCustomAppCheckToken(tokenFromServer, expMillis)
            return Tasks.forResult(appCheckToken)
        }
    }
    // [END appcheck_custom_provider]

    // [START appcheck_custom_provider_factory]
    class YourCustomAppCheckProviderFactory : AppCheckProviderFactory {
        override fun create(firebaseApp: FirebaseApp): AppCheckProvider {
            // Create and return an AppCheckProvider object.
            return YourCustomAppCheckProvider(firebaseApp)
        }
    }
    // [END appcheck_custom_provider_factory]

    private fun init(context: Context) {
        // [START appcheck_initialize_custom_provider]
        FirebaseApp.initializeApp(/*context=*/ context)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            YourCustomAppCheckProviderFactory()
        )
        // [END appcheck_initialize_custom_provider]
    }
}
