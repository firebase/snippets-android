package com.google.firebase.quickstart.auth.kotlin

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseException
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GithubAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PlayGamesAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.quickstart.auth.R
import java.util.concurrent.TimeUnit

/**
 * Created by harshitdwivedi on 14/03/18.
 */
abstract class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun checkCurrentUser() {
        // [START check_current_user]
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
        } else {
            // No user is signed in
        }
        // [END check_current_user]
    }

    private fun getUserProfile() {
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

    private fun getProviderData() {
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

    private fun updateProfile() {
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

    private fun updateEmail() {
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

    private fun updatePassword() {
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

    private fun sendEmailVerification() {
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

    private fun sendEmailVerificationWithContinueUrl() {
        // [START send_email_verification_with_continue_url]
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        val url = "http://www.example.com/verify?uid=" + user?.uid
        val actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl(url)
                .setIOSBundleId("com.example.ios")
                // The default for this is populated with the current android package name.
                .setAndroidPackageName("com.example.android", false, null)
                .build()

        user?.sendEmailVerification(actionCodeSettings)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Email sent.")
                    }
                }

        // [END send_email_verification_with_continue_url]
        // [START localize_verification_email]
        auth.setLanguageCode("fr")
        // To apply the default app language instead of explicitly setting it.
        // auth.useAppLanguage()
        // [END localize_verification_email]
    }

    private fun sendPasswordReset() {
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

    private fun deleteUser() {
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

    private fun reauthenticate() {
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

    private fun authWithGithub() {
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
                        Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    }

                    // ...
                }
        // [END auth_with_github]
    }

    private fun linkAndMerge(credential: AuthCredential) {
        val mAuth = FirebaseAuth.getInstance()

        // [START auth_link_and_merge]
        val prevUser = FirebaseAuth.getInstance().currentUser
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener { result ->
                    val currentUser = result.user
                    // Merge prevUser and currentUser accounts and data
                    // ...
                }
                .addOnFailureListener {
                    // ...
                }
        // [END auth_link_and_merge]
    }

    private fun unlink(providerId: String) {
        val mAuth = FirebaseAuth.getInstance()

        // [START auth_unlink]
        mAuth.currentUser!!.unlink(providerId)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Auth provider unlinked from account
                        // ...
                    }
                }
        // [END auth_unlink]
    }

    private fun buildActionCodeSettings() {
        // [START auth_build_action_code_settings]
        val actionCodeSettings = ActionCodeSettings.newBuilder()
                // URL you want to redirect back to. The domain (www.example.com) for this
                // URL must be whitelisted in the Firebase Console.
                .setUrl("https://www.example.com/finishSignUp?cartId=1234")
                // This must be true
                .setHandleCodeInApp(true)
                .setIOSBundleId("com.example.ios")
                .setAndroidPackageName(
                        "com.example.android",
                        true, /* installIfNotAvailable */
                        "12" /* minimumVersion */)
                .build()
        // [END auth_build_action_code_settings]
    }

    private fun sendSignInLink(email: String, actionCodeSettings: ActionCodeSettings) {
        // [START auth_send_sign_in_link]
        val auth = FirebaseAuth.getInstance()
        auth.sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Email sent.")
                    }
                }
        // [END auth_send_sign_in_link]
    }

    private fun verifySignInLink() {
        // [START auth_verify_sign_in_link]
        val auth = FirebaseAuth.getInstance()
        val intent = intent
        val emailLink = intent.data!!.toString()

        // Confirm the link is a sign-in with email link.
        if (auth.isSignInWithEmailLink(emailLink)) {
            // Retrieve this from wherever you stored it
            val email = "someemail@domain.com"

            // The client SDK will parse the code from the link for you.
            auth.signInWithEmailLink(email, emailLink)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "Successfully signed in with email link!")
                            val result = task.result
                            // You can access the new user via result.getUser()
                            // Additional user info profile *not* available via:
                            // result.getAdditionalUserInfo().getProfile() == null
                            // You can check if the user is new or existing:
                            // result.getAdditionalUserInfo().isNewUser()
                        } else {
                            Log.e(TAG, "Error signing in with email link", task.exception)
                        }
                    }
        }
        // [END auth_verify_sign_in_link]
    }

    private fun linkWithSignInLink(email: String, emailLink: String) {
        val auth = FirebaseAuth.getInstance()

        // [START auth_link_with_link]
        // Construct the email link credential from the current URL.
        val credential = EmailAuthProvider.getCredentialWithLink(email, emailLink)

        // Link the credential to the current user.
        auth.currentUser!!.linkWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Successfully linked emailLink credential!")
                        val result = task.result
                        // You can access the new user via result.getUser()
                        // Additional user info profile *not* available via:
                        // result.getAdditionalUserInfo().getProfile() == null
                        // You can check if the user is new or existing:
                        // result.getAdditionalUserInfo().isNewUser()
                    } else {
                        Log.e(TAG, "Error linking emailLink credential", task.exception)
                    }
                }
        // [END auth_link_with_link]
    }

    private fun reauthWithLink(email: String, emailLink: String) {
        val auth = FirebaseAuth.getInstance()

        // [START auth_reauth_with_link]
        // Construct the email link credential from the current URL.
        val credential = EmailAuthProvider.getCredentialWithLink(email, emailLink)

        // Re-authenticate the user with this credential.
        auth.currentUser!!.reauthenticateAndRetrieveData(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // User is now successfully reauthenticated
                    } else {
                        Log.e(TAG, "Error reauthenticating", task.exception)
                    }
                }
        // [END auth_reauth_with_link]
    }

    private fun differentiateLink(email: String) {
        val auth = FirebaseAuth.getInstance()

        // [START auth_differentiate_link]
        auth.fetchSignInMethodsForEmail(email)
                .addOnSuccessListener { result ->
                    val signInMethods = result.signInMethods
                    if (signInMethods!!.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)) {
                        // User can sign in with email/password
                    } else if (signInMethods.contains(EmailAuthProvider.EMAIL_LINK_SIGN_IN_METHOD)) {
                        // User can sign in with email/link
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting sign in methods for user", exception)
                }
        // [END auth_differentiate_link]
    }

    private fun getGoogleCredentials() {
        val googleIdToken = ""
        // [START auth_google_cred]
        val credential = GoogleAuthProvider.getCredential(googleIdToken, null)
        // [END auth_google_cred]
    }

    private fun getFbCredentials() {
        val token = AccessToken.getCurrentAccessToken()
        // [START auth_fb_cred]
        val credential = FacebookAuthProvider.getCredential(token.token)
        // [END auth_fb_cred]
    }

    private fun getEmailCredentials() {
        val email = ""
        val password = ""
        // [START auth_email_cred]
        val credential = EmailAuthProvider.getCredential(email, password)
        // [END auth_email_cred]
    }

    private fun signOut() {
        // [START auth_sign_out]
        FirebaseAuth.getInstance().signOut()
        // [END auth_sign_out]
    }

    private fun testPhoneVerify() {
        // [START auth_test_phone_verify]
        val phoneNum = "+16505554567"
        val testVerificationCode = "123456"

        // Whenever verification is triggered with the whitelisted number,
        // provided it is not set for auto-retrieval, onCodeSent will be triggered.
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNum, 30L /*timeout*/, TimeUnit.SECONDS,
                this, object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onCodeSent(
                verificationId: String?,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken?
            ) {
                // Save the verification id somewhere
                // ...

                // The corresponding whitelisted code above should be used to complete sign-in.
                this@MainActivity.enableUserManuallyInputCode()
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                // Sign in with the credential
                // ...
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // ...
            }
        })
        // [END auth_test_phone_verify]
    }

    private fun enableUserManuallyInputCode() {
        // No-op
    }

    private fun testPhoneAutoRetrieve() {
        // [START auth_test_phone_auto]
        // The test phone number and code should be whitelisted in the console.
        val phoneNumber = "+16505554567"
        val smsCode = "123456"

        val firebaseAuth = FirebaseAuth.getInstance()
        val firebaseAuthSettings = firebaseAuth.firebaseAuthSettings

        // Configure faking the auto-retrieval with the whitelisted numbers.
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNumber, smsCode)

        val phoneAuthProvider = PhoneAuthProvider.getInstance()
        phoneAuthProvider.verifyPhoneNumber(
                phoneNumber,
                60L,
                TimeUnit.SECONDS,
                this, /* activity */
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        // Instant verification is applied and a credential is directly returned.
                        // ...
                    }

                    // [START_EXCLUDE]
                    override fun onVerificationFailed(e: FirebaseException) {
                    }
                    // [END_EXCLUDE]
                })
        // [END auth_test_phone_auto]
    }

    private fun gamesMakeGoogleSignInOptions() {
        // [START games_google_signin_options]
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .requestServerAuthCode(getString(R.string.default_web_client_id))
                .build()
        // [END games_google_signin_options]
    }

    // [START games_auth_with_firebase]
    // Call this both in the silent sign-in task's OnCompleteListener and in the
    // Activity's onActivityResult handler.
    private fun firebaseAuthWithPlayGames(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithPlayGames:" + acct.id!!)

        val auth = FirebaseAuth.getInstance()
        val credential = PlayGamesAuthProvider.getCredential(acct.serverAuthCode!!)
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
                        Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }

                    // ...
                }
    }
    // [END games_auth_with_firebase]

    private fun gamesGetUserInfo() {
        val auth = FirebaseAuth.getInstance()

        // [START games_get_user_info]
        val user = auth.currentUser
        user?.let {
            val playerName = user.displayName

            // The user's Id, unique to the Firebase project.
            // Do NOT use this value to authenticate with your backend server, if you
            // have one; use FirebaseUser.getIdToken() instead.
            val uid = user.uid
        }

        // [END games_get_user_info]
    }

    private fun updateUI(user: FirebaseUser?) {
        // No-op
    }
}
