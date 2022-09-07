package com.google.firebase.quickstart.dynamiclinks.kotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ktx.database
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.iosParameters
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.ktx.Firebase

/**
 * Snippets for the "rewarded referral" use case.
 */
abstract class ReferralActivity : AppCompatActivity() {

    private var mInvitationUrl: Uri? = null

    fun createLink() {
        // [START ddl_referral_create_link]
        val user = Firebase.auth.currentUser!!
        val uid = user.uid
        val invitationLink = "https://mygame.example.com/?invitedby=$uid"
        Firebase.dynamicLinks.shortLinkAsync {
            link = Uri.parse(invitationLink)
            domainUriPrefix = "https://example.page.link"
            androidParameters("com.example.android") {
                minimumVersion = 125
            }
            iosParameters("com.example.ios") {
                appStoreId = "123456789"
                minimumVersion = "1.0.1"
            }
        }.addOnSuccessListener { shortDynamicLink ->
            mInvitationUrl = shortDynamicLink.shortLink
            // ...
        }
        // [END ddl_referral_create_link]
    }

    fun sendInvitation() {
        // [START ddl_referral_send]
        val referrerName = Firebase.auth.currentUser?.displayName
        val subject = String.format("%s wants you to play MyExampleGame!", referrerName)
        val invitationLink = mInvitationUrl.toString()
        val msg = "Let's play MyExampleGame together! Use my referrer link: $invitationLink"
        val msgHtml = String.format("<p>Let's play MyExampleGame together! Use my " +
                "<a href=\"%s\">referrer link</a>!</p>", invitationLink)

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // only email apps should handle this
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, msg)
            putExtra(Intent.EXTRA_HTML_TEXT, msgHtml)
        }
        intent.resolveActivity(packageManager)?.let {
            startActivity(intent)
        }
        // [END ddl_referral_send]
    }

    // [START ddl_referral_on_create]
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ...

        Firebase.dynamicLinks
                .getDynamicLink(intent)
                .addOnSuccessListener(this) { pendingDynamicLinkData ->
                    // Get deep link from result (may be null if no link is found)
                    var deepLink: Uri? = null
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.link
                    }
                    //
                    // If the user isn't signed in and the pending Dynamic Link is
                    // an invitation, sign in the user anonymously, and record the
                    // referrer's UID.
                    //
                    val user = Firebase.auth.currentUser
                    if (user == null &&
                            deepLink != null &&
                            deepLink.getBooleanQueryParameter("invitedby", false)) {
                        val referrerUid = deepLink.getQueryParameter("invitedby")
                        createAnonymousAccountWithReferrerInfo(referrerUid)
                    }
                }
    }

    private fun createAnonymousAccountWithReferrerInfo(referrerUid: String?) {
        Firebase.auth
                .signInAnonymously()
                .addOnSuccessListener {
                    // Keep track of the referrer in the RTDB. Database calls
                    // will depend on the structure of your app's RTDB.
                    val user = Firebase.auth.currentUser
                    val userRecord = Firebase.database.reference
                            .child("users")
                            .child(user!!.uid)
                    userRecord.child("referred_by").setValue(referrerUid)
                }
    }
    // [END ddl_referral_on_create]

    fun getCredential(email: String, password: String) {
        // [START ddl_referral_get_cred]
        val credential = EmailAuthProvider.getCredential(email, password)
        // [END ddl_referral_get_cred]
    }

    fun linkCredential(credential: AuthCredential) {
        // [START ddl_referral_link_cred]
        Firebase.auth.currentUser!!
                .linkWithCredential(credential)
                .addOnSuccessListener {
                    // Complete any post sign-up tasks here.
                }
        // [END ddl_referral_link_cred]
    }

    fun rewardUser(credential: AuthCredential) {
        // [START ddl_referral_reward_user]
        Firebase.auth.currentUser!!
                .linkWithCredential(credential)
                .addOnSuccessListener {
                    // Complete any post sign-up tasks here.

                    // Trigger the sign-up reward function by creating the
                    // "last_signin_at" field. (If this is a value you want to track,
                    // you would also update this field in the success listeners of
                    // your Firebase Authentication signIn calls.)
                    val user = Firebase.auth.currentUser!!
                    val userRecord = Firebase.database.reference
                            .child("users")
                            .child(user.uid)
                    userRecord.child("last_signin_at").setValue(ServerValue.TIMESTAMP)
                }
        // [END ddl_referral_reward_user]
    }
}
