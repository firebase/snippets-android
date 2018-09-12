package com.google.firebase.quickstart.dynamiclinks.interfaces;

import com.google.firebase.auth.AuthCredential;

public interface ReferralActivityInterface {
    void createLink();

    void sendInvitation();

    void getCredential(String email, String password);

    void linkCredential(AuthCredential credential);

    void rewardUser(AuthCredential credential);
}
