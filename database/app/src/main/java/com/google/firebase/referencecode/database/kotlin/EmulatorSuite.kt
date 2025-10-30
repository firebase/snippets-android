package com.google.firebase.referencecode.database.kotlin

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.Firebase

class EmulatorSuite {

    fun emulatorSettings() {
        // [START rtdb_emulator_connect]
        // 10.0.2.2 is the special IP address to connect to the 'localhost' of
        // the host computer from an Android emulator.
        val database = Firebase.database
        database.useEmulator("10.0.2.2", 9000)
        // [END rtdb_emulator_connect]
    }

    fun flushRealtimeDatabase(database: FirebaseDatabase) {
        // [START rtdb_emulator_flush]
        // With a DatabaseReference, write null to clear the database.
        database.reference.setValue(null)
        // [END rtdb_emulator_flush]
    }
}
