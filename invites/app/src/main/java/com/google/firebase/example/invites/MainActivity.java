package com.google.firebase.example.invites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.example.invites.interfaces.MainActivityInterface;

import devrel.firebase.google.com.firebaseoptions.R;

public class MainActivity extends AppCompatActivity implements MainActivityInterface {

    private static final String IOS_APP_CLIENT_ID = "foo-bar-baz";
    private static final int REQUEST_INVITE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
