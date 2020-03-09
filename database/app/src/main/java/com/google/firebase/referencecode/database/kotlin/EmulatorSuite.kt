package com.google.firebase.referencecode.database.kotlin

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class EmulatorSuite {

    fun emulatorSettings() {
        // [START rtdb_emulator_connect]
        // 10.0.2.2 is the special IP address to connect to the 'localhost' of
        // the host computer from an Android emulator.
        // In almost all cases the ns (namespace) is your project ID.
        val database = Firebase.database("http://10.0.2.2:9000?ns=YOUR_DATABASE_NAMESPACE")
        // [END rtdb_emulator_connect]
    }

    fun flushRealtimeDatabase(database: FirebaseDatabase) {
        // [START rtdb_emulator_flush]
        // With a DatabaseReference, write null to clear the database.
        database.reference.setValue(null)
        // [END rtdb_emulator_flush]
    }
}
