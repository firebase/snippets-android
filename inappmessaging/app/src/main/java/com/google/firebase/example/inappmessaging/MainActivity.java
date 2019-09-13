package com.google.firebase.example.inappmessaging;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.inappmessaging.FirebaseInAppMessaging;

public class MainActivity extends AppCompatActivity {

    private void addClickListener() {
        // [START fiam_add_click_listener]
        MyClickListener listener = new MyClickListener();
        FirebaseInAppMessaging.getInstance().addClickListener(listener);
        // [END fiam_add_click_listener]
    }

    private void suppressMessages() {
        // [START fiam_suppress_messages]
        FirebaseInAppMessaging.getInstance().setMessagesSuppressed(true);
        // [END fiam_suppress_messages]
    }

    private void enableDataCollection() {
        // [START fiam_enable_data_collection]
        // Only needed if firebase_inapp_messaging_auto_data_collection_enabled is set to
        // false in AndroidManifest.xml
        FirebaseInAppMessaging.getInstance().setAutomaticDataCollectionEnabled(true);
        // [END fiam_enable_data_collection]
    }
}
