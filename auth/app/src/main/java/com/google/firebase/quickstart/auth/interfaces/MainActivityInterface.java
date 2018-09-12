package com.google.firebase.quickstart.auth.interfaces;

import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;

/**
 * Created by harshitdwivedi on 14/03/18.
 */

public interface MainActivityInterface {

    void checkCurrentUser();

    void getUserProfile();

    void getProviderData();

    void updateProfile();

    void updateEmail();

    void updatePassword();

    void sendEmailVerification();

    void sendEmailVerificationWithContinueUrl();

    void sendPasswordReset();

    void deleteUser();

    void reauthenticate();

    void authWithGithub();

    void linkAndMerge(AuthCredential credential);

    void unlink(String providerId);

    void buildActionCodeSettings();

    void sendSignInLink(String email, ActionCodeSettings actionCodeSettings);

    void verifySignInLink();

    void linkWithSignInLink(String email, String emailLink);

    void reauthWithLink(String email, String emailLink);

    void differentiateLink(String email);

    void getGoogleCredentials();

    void getFbCredentials();

    void getEmailCredentials();

    void signOut() ;

    void testPhoneVerify();

    void testPhoneAutoRetrieve();

}
