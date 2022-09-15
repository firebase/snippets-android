package com.google.firebase.referencecode.storage;

import com.google.firebase.storage.FirebaseStorage;

public class EmulatorSuite {

    public void emulatorSettings() {
        // [START storage_emulator_connect]
        FirebaseStorage.getInstance().useEmulator("10.0.2.2", 9199);
        // [END storage_emulator_connect]
    }

}
