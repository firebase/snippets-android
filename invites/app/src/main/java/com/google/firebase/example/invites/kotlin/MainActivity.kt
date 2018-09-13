package com.google.firebase.example.invites.kotlin

import android.support.v7.app.AppCompatActivity
import com.google.android.gms.appinvite.AppInviteInvitation
import devrel.firebase.google.com.firebaseoptions.R

class MainActivity : AppCompatActivity() {

    companion object {
        private val IOS_APP_CLIENT_ID = "foo-bar-baz"
        private val REQUEST_INVITE = 101
    }

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

}
