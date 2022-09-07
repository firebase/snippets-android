package com.google.firebase.referencecode.database;

import com.google.firebase.database.FirebaseDatabase;

public class EmulatorSuite {

    public void emulatorSettings() {
        // [START rtdb_emulator_connect]
        // 10.0.2.2 is the special IP address to connect to the 'localhost' of
        // the host computer from an Android emulator.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.useEmulator("10.0.2.2", 9000);
        // [END rtdb_emulator_connect]
    }

    public void flushRealtimeDatabase(FirebaseDatabase database) {
        // [START rtdb_emulator_flush]
        // With a DatabaseReference, write null to clear the database.
        database.getReference().setValue(null);
        // [END rtdb_emulator_flush]

    }

}
