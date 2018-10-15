package com.google.firebase.example.appindexing.kotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.firebase.appindexing.FirebaseAppIndex

// [START appindexing_update_receiver]
/** Receives broadcast for App Indexing Update. */
class AppIndexingUpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent != null && FirebaseAppIndex.ACTION_UPDATE_INDEX == intent.action) {
            // Schedule the job to be run in the background.
            AppIndexingUpdateService.enqueueWork(context)
        }
    }
}
// [END appindexing_update_receiver]
