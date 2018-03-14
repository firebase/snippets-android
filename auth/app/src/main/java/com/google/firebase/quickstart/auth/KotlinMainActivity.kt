package com.google.firebase.quickstart.auth

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.quickstart.auth.interfaces.MainActivityInterface

/**
 * Created by harshitdwivedi on 14/03/18.
 */
class KotlinMainActivity : AppCompatActivity(), MainActivityInterface {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun checkCurrentUser() {
        // [START check_current_user]
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
        } else {
            // No user is signed in
        }
        // [END check_current_user]
    }

    override fun getUserProfile() {
        // [START get_user_profile]
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid = user.uid
        }
        // [END get_user_profile]
    }

    override fun getProviderData() {
        // [START get_provider_data]
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            for (profile in it.providerData) {
                // Id of the provider (ex: google.com)
                val providerId = profile.providerId

                // UID specific to the provider
                val uid = profile.uid

                // Name, email address, and profile photo Url
                val name = profile.displayName
                val email = profile.email
                val photoUrl = profile.photoUrl
            }
        }
        // [END get_provider_data]
    }

    override fun updateProfile() {
        // [START update_profile]
        val user = FirebaseAuth.getInstance().currentUser

        val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName("Jane Q. User")
                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build()

        user?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User profile updated.")
                    }
                }
        // [END update_profile]
    }

    override fun updateEmail() {
        // [START update_email]
        val user = FirebaseAuth.getInstance().currentUser

        user?.updateEmail("user@example.com")
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User email address updated.")
                    }
                }
        // [END update_email]
    }

    override fun updatePassword() {
        // [START update_password]
        val user = FirebaseAuth.getInstance().currentUser
        val newPassword = "SOME-SECURE-PASSWORD"

        user?.updatePassword(newPassword)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User password updated.")
                    }
                }
        // [END update_password]
    }

    override fun sendEmailVerification() {
        // [START send_email_verification]
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        user?.sendEmailVerification()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Email sent.")
                    }
                }
        // [END send_email_verification]
    }

    override fun sendEmailVerificationWithContinueUrl() {
        // [START send_email_verification_with_continue_url]
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        val url = "http://www.example.com/verify?uid=" + user!!.uid
        val actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl(url)
                .setIOSBundleId("com.example.ios")
                // The default for this is populated with the current android package name.
                .setAndroidPackageName("com.example.android", false, null)
                .build()

        user.sendEmailVerification(actionCodeSettings)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Email sent.")
                    }
                }

        // [END send_email_verification_with_continue_url]
        // [START localize_verification_email]
        auth.setLanguageCode("fr")
        // To apply the default app language instead of explicitly setting it.
        // auth.useAppLanguage();
        // [END localize_verification_email]
    }

    override fun sendPasswordReset() {
        // [START send_password_reset]
        val auth = FirebaseAuth.getInstance()
        val emailAddress = "user@example.com"

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Email sent.")
                    }
                }
        // [END send_password_reset]
    }

    override fun deleteUser() {
        // [START delete_user]
        val user = FirebaseAuth.getInstance().currentUser

        user?.delete()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User account deleted.")
                    }
                }
        // [END delete_user]
    }

    override fun reauthenticate() {
        // [START reauthenticate]
        val user = FirebaseAuth.getInstance().currentUser

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        val credential = EmailAuthProvider
                .getCredential("user@example.com", "password1234")

        // Prompt the user to re-provide their sign-in credentials
        user?.reauthenticate(credential)
                ?.addOnCompleteListener { Log.d(TAG, "User re-authenticated.") }
        // [END reauthenticate]
    }

    override fun authWithGithub() {
        val mAuth = FirebaseAuth.getInstance()

        // [START auth_with_github]
        val token = "<GITHUB-ACCESS-TOKEN>"
        val credential = GithubAuthProvider.getCredential(token)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful)

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful) {
                        Log.w(TAG, "signInWithCredential", task.exception)
                        Toast.makeText(this@KotlinMainActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    }

                    // ...
                }
        // [END auth_with_github]
    }

}