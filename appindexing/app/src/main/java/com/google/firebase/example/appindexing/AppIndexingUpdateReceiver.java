package com.google.firebase.example.appindexing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.appindexing.FirebaseAppIndex;

// [START appindexing_update_receiver]
/** Receives broadcast for App Indexing Update. */
public class AppIndexingUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null
                && FirebaseAppIndex.ACTION_UPDATE_INDEX.equals(intent.getAction())) {
            // Schedule the job to be run in the background.
            AppIndexingUpdateService.enqueueWork(context);
        }
    }

}
// [END appindexing_update_receiver]
