package com.google.firebase.referencecode.storage.kotlin

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.firebase.ktx.Firebase

class EmulatorSuite {

    fun emulatorSettings() {
        // [START storage_emulator_connect]
        Firebase.storage.useEmulator("10.0.2.2", 9199);
        // [END storage_emulator_connect]
    }

}
