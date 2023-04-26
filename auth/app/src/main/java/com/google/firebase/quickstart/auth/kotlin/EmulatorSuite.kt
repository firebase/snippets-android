package com.google.firebase.quickstart.auth.kotlin

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EmulatorSuite {

    fun emulatorSettings() {
        // [START auth_emulator_connect]
        // 10.0.2.2 is the special IP address to connect to the 'localhost' of
        // the host computer from an Android emulator.
        Firebase.auth.useEmulator("10.0.2.2", 9099)
        // [END auth_emulator_connect]
    }
}
