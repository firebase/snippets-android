package com.google.firebase.referencecode.storage;

import com.google.firebase.storage.FirebaseStorage;

public class EmulatorSuite {

    public void emulatorSettings() {
        // [START storage_emulator_connect]
        // 10.0.2.2 is the special IP address to connect to the 'localhost' of
        // the host computer from an Android emulator.
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storage.useEmulator("10.0.2.2", 9199);
        // [END storage_emulator_connect]
    }

}
