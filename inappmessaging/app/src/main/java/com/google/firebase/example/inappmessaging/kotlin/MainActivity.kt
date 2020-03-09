package com.google.firebase.example.inappmessaging.kotlin

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.example.inappmessaging.MyClickListener
import com.google.firebase.inappmessaging.ktx.inAppMessaging
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private fun addClickListener() {
        // [START fiam_add_click_listener]
        val listener = MyClickListener()
        Firebase.inAppMessaging.addClickListener(listener)
        // [END fiam_add_click_listener]
    }

    private fun suppressMessages() {
        // [START fiam_suppress_messages]
        Firebase.inAppMessaging.setMessagesSuppressed(true)
        // [END fiam_suppress_messages]
    }

    private fun enableDataCollection() {
        // [START fiam_enable_data_collection]
        // Only needed if firebase_inapp_messaging_auto_data_collection_enabled is set to
        // false in AndroidManifest.xml
        Firebase.inAppMessaging.isAutomaticDataCollectionEnabled = true
        // [END fiam_enable_data_collection]
    }
}
