package com.google.firebase.example.invites.kotlin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.android.gms.appinvite.AppInviteInvitation
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.appinvite.FirebaseAppInvite
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import devrel.firebase.google.com.firebaseoptions.R
import kotlinx.android.synthetic.main.activity_main.inviteButton
import kotlinx.android.synthetic.main.activity_main.snackbarLayout

/**
 * App Invites is deprecated, this file serves only to contain snippets that are still
 * referenced in some documentation.
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val TAG = "MainActivity"
        private const val IOS_APP_CLIENT_ID = "foo-bar-baz"
        private const val REQUEST_INVITE = 101
    }

    // [START on_create]
    override fun onCreate(savedInstanceState: Bundle?) {
        // [START_EXCLUDE]
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Invite button click listener
        inviteButton.setOnClickListener(this)
        // [END_EXCLUDE]

        // Check for App Invite invitations and launch deep-link activity if possible.
        // Requires that an Activity is registered in AndroidManifest.xml to handle
        // deep-link URLs.
        FirebaseDynamicLinks.getInstance().getDynamicLink(intent)
                .addOnSuccessListener(this, OnSuccessListener { data ->
                    if (data == null) {
                        Log.d(TAG, "getInvitation: no data")
                        return@OnSuccessListener
                    }

                    // Get the deep link
                    val deepLink = data.link

                    // Extract invite
                    val invite = FirebaseAppInvite.getInvitation(data)
                    val invitationId = invite.invitationId

                    // Handle the deep link
                    // [START_EXCLUDE]
                    Log.d(TAG, "deepLink:$deepLink")
                    deepLink?.let {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setPackage(packageName)
                        intent.data = deepLink

                        startActivity(intent)
                    }
                    // [END_EXCLUDE]
                })
                .addOnFailureListener(this) { e -> Log.w(TAG, "getDynamicLink:onFailure", e) }
    }
    // [END on_create]

    /**
     * User has clicked the 'Invite' button, launch the invitation UI with the proper
     * title, message, and deep link
     */
    // [START on_invite_clicked]
    private fun onInviteClicked() {
        val intent = AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build()
        startActivityForResult(intent, REQUEST_INVITE)
    }
    // [END on_invite_clicked]

    // [START on_activity_result]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult: requestCode=$requestCode, resultCode=$resultCode")

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == Activity.RESULT_OK) {
                // Get the invitation IDs of all sent messages
                val ids = AppInviteInvitation.getInvitationIds(resultCode, data!!)
                for (id in ids) {
                    Log.d(TAG, "onActivityResult: sent invitation $id")
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // [START_EXCLUDE]
                showMessage(getString(R.string.send_failed))
                // [END_EXCLUDE]
            }
        }
    }
    // [END on_activity_result]

    fun sendInvitationIOS() {
        // [START invites_send_invitation_ios]
        val intent = AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                // ...
                .setOtherPlatformsTargetApplication(
                        AppInviteInvitation.IntentBuilder.PlatformMode.PROJECT_PLATFORM_IOS,
                        IOS_APP_CLIENT_ID)
                // ...
                .build()
        // [END invites_send_invitation_ios]
    }

    private fun showMessage(msg: String) {
        Snackbar.make(snackbarLayout, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onClick(view: View) {
        val i = view.id
        if (i == R.id.inviteButton) {
            onInviteClicked()
        }
    }
}
