package com.google.example.firestore.kotlin

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase

class EmulatorSuite {

    fun emulatorSettings() {
        // [START fs_emulator_connect]
        // 10.0.2.2 is the special IP address to connect to the 'localhost' of
        // the host computer from an Android emulator.
        val firestore = Firebase.firestore
        firestore.firestoreSettings = firestoreSettings {
            host = "http://10.0.0.2:8080"
            isSslEnabled = false
            isPersistenceEnabled = false
        }
        // [END fs_emulator_connect]
    }
}
