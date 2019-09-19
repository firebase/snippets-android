package com.google.example.firestore;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class EmulatorSuite {

    public void emulatorSettings() {
        // [START fs_emulator_connect]
        // 10.0.2.2 is the special IP address to connect to the 'localhost' of
        // the host computer from an Android emulator.
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setHost("10.0.2.2:8080")
                .setSslEnabled(false)
                .setPersistenceEnabled(false)
                .build();

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.setFirestoreSettings(settings);
        // [END fs_emulator_connect]
    }

}
