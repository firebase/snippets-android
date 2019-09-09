package com.google.firebase.referencecode.database;

import com.google.firebase.database.FirebaseDatabase;

public class EmulatorSuite {

    public void emulatorSettings() {
        // [START rtdb_emulator_connect]
        // 10.0.2.2 is the special IP address to connect to the 'localhost' of
        // the host computer from an Android emulator.
        // Here we provide the database  namespace 'foo'.
        FirebaseDatabase database = FirebaseDatabase.getInstance("http://10.0.2.2:9000?ns=foo");
        // [END rtdb_emulator_connect]
    }

}
