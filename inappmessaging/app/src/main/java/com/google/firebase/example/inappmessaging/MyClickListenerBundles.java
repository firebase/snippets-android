package com.google.firebase.example.inappmessaging;

import com.google.firebase.inappmessaging.FirebaseInAppMessagingClickListener;
import com.google.firebase.inappmessaging.model.Action;
import com.google.firebase.inappmessaging.model.InAppMessage;

import java.util.Map;

// [START fiam_click_listener_bundles]
public class MyClickListenerBundles implements FirebaseInAppMessagingClickListener {

    @Override
    public void messageClicked(InAppMessage inAppMessage, Action action) {
        // Determine which URL the user clicked
        String url = action.getActionUrl();

        // Get data bundle for the inapp message
        Map<String, String> dataBundle = inAppMessage.getData();

        // ...
    }

}
// [END fiam_click_listener_bundles]