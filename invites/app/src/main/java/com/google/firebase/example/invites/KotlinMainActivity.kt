package com.google.firebase.example.invites

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.appinvite.AppInviteInvitation
import com.google.firebase.example.invites.interfaces.MainActivityInterface
import devrel.firebase.google.com.firebaseoptions.R

class KotlinMainActivity : AppCompatActivity(), MainActivityInterface {

    private val IOS_APP_CLIENT_ID = "foo-bar-baz"
    private val REQUEST_INVITE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun sendInvitationIOS() {
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