package com.google.firebase.example.invites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.appinvite.AppInviteInvitation;

import devrel.firebase.google.com.firebaseoptions.R;

public class MainActivity extends AppCompatActivity {

    private static final String IOS_APP_CLIENT_ID = "foo-bar-baz";
    private static final int REQUEST_INVITE = 101;

    public void sendInvitationIOS() {
        // [START invites_send_invitation_ios]
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                // ...
                .setOtherPlatformsTargetApplication(
                        AppInviteInvitation.IntentBuilder.PlatformMode.PROJECT_PLATFORM_IOS,
                        IOS_APP_CLIENT_ID)
                // ...
                .build();
        // [END invites_send_invitation_ios]
    }

}
