package com.google.firebase.quickstart.auth;

import com.google.firebase.auth.FirebaseAuth;

public class EmulatorSuite {
    public void emulatorSettings() {
        // [START auth_emulator_connect]
        // 10.0.2.2 is the special IP address to connect to the 'localhost' of
        // the host computer from an Android emulator.
        FirebaseAuth.getInstance().useEmulator("10.0.2.2", 9099);
        // [END auth_emulator_connect]
    }
}
