package com.google.firebase.quickstart.auth.kotlin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.google.firebase.quickstart.auth.R
import kotlinx.coroutines.launch

/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
class GoogleSignInActivity : AppCompatActivity() {

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    // [START declare_credential_manager]
    private lateinit var credentialManager: CredentialManager
    // [END declare_credential_manager]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]

        // [START initialize_credential_manager]
        // Initialize Credential Manager
        credentialManager = CredentialManager.create(baseContext)
        // [END initialize_credential_manager]

        // [START configure_credential_manager]
        // Create the configuration for the Credential Manager request
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(baseContext.getString(R.string.default_web_client_id))
            .build()

        // Create the Credential Manager request using the configuration created above
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        // [END configure_credential_manager]

        getCredential(request)
    }

    // [START on_start_check_user]
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
    // [END on_start_check_user]

    // [START get_user_credential]
    private fun getCredential(request: GetCredentialRequest) {
        lifecycleScope.launch {
            try {
                // Launch Credential Manager UI
                val result = credentialManager.getCredential(
                    request = request,
                    context = baseContext
                )

                // Extract credential from the result returned by Credential Manager
                val credential = result.credential

                // Check if credential is of type Google ID
                if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    // Sign in to Firebase with the Google ID Token
                    firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                } else {
                    Log.d(TAG, "Credential is not of type Google ID!")
                }
            } catch (e: GetCredentialException) {
                Log.e(TAG, "Couldn't retrieve user's credentials: ${e.localizedMessage}")
            }
        }
    }
    // [END get_user_credential]

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }
    // [END auth_with_google]

    private fun updateUI(user: FirebaseUser?) {
    }

    companion object {
        private const val TAG = "GoogleActivity"
    }
}
