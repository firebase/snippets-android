package com.google.firebase.quickstart.auth.interfaces;

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

}
