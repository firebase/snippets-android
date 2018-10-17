package com.google.firebase.quickstart.dynamiclinks.kotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks

/**
 * Snippets for the "rewarded referral" use case.
 */
abstract class ReferralActivity : AppCompatActivity() {

    private var mInvitationUrl: Uri? = null

    fun createLink() {
        // [START ddl_referral_create_link]
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user!!.uid
        val link = "https://mygame.example.com/?invitedby=$uid"
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(link))
                .setDynamicLinkDomain("example.page.link")
                .setAndroidParameters(
                        DynamicLink.AndroidParameters.Builder("com.example.android")
                                .setMinimumVersion(125)
                                .build())
                .setIosParameters(
                        DynamicLink.IosParameters.Builder("com.example.ios")
                                .setAppStoreId("123456789")
                                .setMinimumVersion("1.0.1")
                                .build())
                .buildShortDynamicLink()
                .addOnSuccessListener { shortDynamicLink ->
                    mInvitationUrl = shortDynamicLink.shortLink
                    // ...
                }
        // [END ddl_referral_create_link]
    }

    fun sendInvitation() {
        // [START ddl_referral_send]
        val referrerName = FirebaseAuth.getInstance().currentUser?.displayName
        val subject = String.format("%s wants you to play MyExampleGame!", referrerName)
        val invitationLink = mInvitationUrl.toString()
        val msg = "Let's play MyExampleGame together! Use my referrer link: $invitationLink"
        val msgHtml = String.format("<p>Let's play MyExampleGame together! Use my " +
                "<a href=\"%s\">referrer link</a>!</p>", invitationLink)

        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, msg)
        intent.putExtra(Intent.EXTRA_HTML_TEXT, msgHtml)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
        // [END ddl_referral_send]
    }

    // [START ddl_referral_on_create]
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ...

        FirebaseDynamicLinks.getInstance()
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
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user == null &&
                            deepLink != null &&
                            deepLink.getBooleanQueryParameter("invitedby", false)) {
                        val referrerUid = deepLink.getQueryParameter("invitedby")
                        createAnonymousAccountWithReferrerInfo(referrerUid)
                    }
                }
    }

    private fun createAnonymousAccountWithReferrerInfo(referrerUid: String?) {
        FirebaseAuth.getInstance()
                .signInAnonymously()
                .addOnSuccessListener {
                    // Keep track of the referrer in the RTDB. Database calls
                    // will depend on the structure of your app's RTDB.
                    val user = FirebaseAuth.getInstance().currentUser
                    val userRecord = FirebaseDatabase.getInstance().reference
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
        FirebaseAuth.getInstance().currentUser!!
                .linkWithCredential(credential)
                .addOnSuccessListener {
                    // Complete any post sign-up tasks here.
                }
        // [END ddl_referral_link_cred]
    }

    fun rewardUser(credential: AuthCredential) {
        // [START ddl_referral_reward_user]
        FirebaseAuth.getInstance().currentUser!!
                .linkWithCredential(credential)
                .addOnSuccessListener {
                    // Complete any post sign-up tasks here.

                    // Trigger the sign-up reward function by creating the
                    // "last_signin_at" field. (If this is a value you want to track,
                    // you would also update this field in the success listeners of
                    // your Firebase Authentication signIn calls.)
                    val user = FirebaseAuth.getInstance().currentUser
                    val userRecord = FirebaseDatabase.getInstance().reference
                            .child("users")
                            .child(user!!.uid)
                    userRecord.child("last_signin_at").setValue(ServerValue.TIMESTAMP)
                }
        // [END ddl_referral_reward_user]
    }
}
