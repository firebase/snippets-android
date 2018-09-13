package com.google.firebase.quickstart.dynamiclinks.interfaces;

import android.net.Uri;

import com.google.firebase.dynamiclinks.ShortDynamicLink;

public interface MainActivityInterface {

    void createDynamicLink_Basic();

    void createDynamicLink_Advanced();

    void createShortLink();

    void shortenLongLink();

    void shareLink(Uri myDynamicLink);

    void getInvitation() ;

    void onboardingShare(ShortDynamicLink dl);

}
