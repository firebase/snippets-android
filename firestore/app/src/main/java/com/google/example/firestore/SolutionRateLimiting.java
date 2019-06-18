package com.google.example.firestore;

import android.os.Handler;
import androidx.annotation.Nullable;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Demonstrates how to write to the same document repeatedly without worrying about rate limits.
 */
public class SolutionRateLimiting {

    public static final String TAG = "RateLimiting";

    public static final int UPDATE_INTERVAL_MS = 1000;

    private FirebaseFirestore db;
    private ListenerRegistration mRegistration;

    private Handler mHandler;
    private boolean mReadyForUpdate = false;

    public SolutionRateLimiting() {
        db = FirebaseFirestore.getInstance();
        mHandler = new Handler();
    }

    public void startUpdates() {
        String deviceId = "my-device-id";
        final DocumentReference reference = db.collection("readings").document(deviceId);

        // Listen to the document, including metadata changes so we get notified
        // when writes have propagated to the server.
        mRegistration = reference.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "onEvent:error", e);
                }

                if (documentSnapshot != null && !documentSnapshot.getMetadata().hasPendingWrites()) {
                    Log.d(TAG, "Got server snapshot.");
                   mReadyForUpdate = true;
                }
            }
        });

        // On a regular interval, attempt to update the document. Only perform the update
        // if we have gotten a snapshot from the server since the last update, which means
        // we are ready to update again.
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mReadyForUpdate) {
                    Log.d(TAG, "Updating sensor data");

                    // Update the document
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("temperature", getCurrentTemperature());
                    updates.put("timestamp", FieldValue.serverTimestamp());
                    reference.set(updates, SetOptions.merge());


                    // Mark 'ready for update' as false until we get the next snapshot from the server
                    mReadyForUpdate = false;
                } else {
                    Log.d(TAG, "Not ready for update");
                }

                // Schedule the next update
                mHandler.postDelayed(this, UPDATE_INTERVAL_MS);
            }
        }, UPDATE_INTERVAL_MS);
    }

    public void stopUpdates() {
        mHandler.removeCallbacksAndMessages(null);
        if (mRegistration != null) {
            mRegistration.remove();
            mRegistration = null;
        }

    }

    public int getCurrentTemperature() {
        return new Random().nextInt(30) + 60;
    }


}
