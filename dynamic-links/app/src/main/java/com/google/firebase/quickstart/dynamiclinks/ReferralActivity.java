package com.google.firebase.quickstart.dynamiclinks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

/**
 * Snippets for the "rewarded referral" use case.
 */
public class ReferralActivity extends AppCompatActivity {

    private Uri mInvitationUrl;

    public void createLink() {
        // [START ddl_referral_create_link]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        String link = "https://mygame.example.com/?invitedby=" + uid;
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(link))
                .setDomainUriPrefix("https://example.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.example.android")
                                .setMinimumVersion(125)
                                .build())
                .setIosParameters(
                        new DynamicLink.IosParameters.Builder("com.example.ios")
                                .setAppStoreId("123456789")
                                .setMinimumVersion("1.0.1")
                                .build())
                .buildShortDynamicLink()
                .addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
                    @Override
                    public void onSuccess(ShortDynamicLink shortDynamicLink) {
                        mInvitationUrl = shortDynamicLink.getShortLink();
                        // ...
                    }
                });
        // [END ddl_referral_create_link]
    }

    public void sendInvitation() {
        // [START ddl_referral_send]
        String referrerName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String subject = String.format("%s wants you to play MyExampleGame!", referrerName);
        String invitationLink = mInvitationUrl.toString();
        String msg = "Let's play MyExampleGame together! Use my referrer link: "
                + invitationLink;
        String msgHtml = String.format("<p>Let's play MyExampleGame together! Use my "
                + "<a href=\"%s\">referrer link</a>!</p>", invitationLink);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        intent.putExtra(Intent.EXTRA_HTML_TEXT, msgHtml);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        // [END ddl_referral_send]
    }

    // [START ddl_referral_on_create]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ...

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }
                        //
                        // If the user isn't signed in and the pending Dynamic Link is
                        // an invitation, sign in the user anonymously, and record the
                        // referrer's UID.
                        //
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user == null
                                && deepLink != null
                                && deepLink.getBooleanQueryParameter("invitedby", false)) {
                            String referrerUid = deepLink.getQueryParameter("invitedby");
                            createAnonymousAccountWithReferrerInfo(referrerUid);
                        }
                    }
                });
    }

    private void createAnonymousAccountWithReferrerInfo(final String referrerUid) {
        FirebaseAuth.getInstance()
                .signInAnonymously()
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Keep track of the referrer in the RTDB. Database calls
                        // will depend on the structure of your app's RTDB.
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference userRecord =
                                FirebaseDatabase.getInstance().getReference()
                                        .child("users")
                                        .child(user.getUid());
                        userRecord.child("referred_by").setValue(referrerUid);
                    }
                });
    }
    // [END ddl_referral_on_create]

    public void getCredential(String email, String password) {
        // [START ddl_referral_get_cred]
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        // [END ddl_referral_get_cred]
    }

    public void linkCredential(AuthCredential credential) {
        // [START ddl_referral_link_cred]
        FirebaseAuth.getInstance().getCurrentUser()
                .linkWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Complete any post sign-up tasks here.
                    }
                });
        // [END ddl_referral_link_cred]
    }

    public void rewardUser(AuthCredential credential) {
        // [START ddl_referral_reward_user]
        FirebaseAuth.getInstance().getCurrentUser()
                .linkWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Complete any post sign-up tasks here.

                        // Trigger the sign-up reward function by creating the
                        // "last_signin_at" field. (If this is a value you want to track,
                        // you would also update this field in the success listeners of
                        // your Firebase Authentication signIn calls.)
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference userRecord =
                                FirebaseDatabase.getInstance().getReference()
                                        .child("users")
                                        .child(user.getUid());
                        userRecord.child("last_signin_at").setValue(ServerValue.TIMESTAMP);
                    }
                });
        // [END ddl_referral_reward_user]
    }
}
