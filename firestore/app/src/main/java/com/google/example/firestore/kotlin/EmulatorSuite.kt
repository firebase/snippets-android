package com.google.example.firestore.kotlin

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class EmulatorSuite {

    fun emulatorSettings() {
        // [START fs_emulator_connect]
        // 10.0.2.2 is the special IP address to connect to the 'localhost' of
        // the host computer from an Android emulator.
        val settings = FirebaseFirestoreSettings.Builder()
                .setHost("10.0.2.2:8080")
                .setSslEnabled(false)
                .setPersistenceEnabled(false)
                .build()

        val firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = settings
        // [END fs_emulator_connect]
    }
}
