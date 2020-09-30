package devrel.firebase.google.com.functions.kotlin

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    fun emulatorSettings() {
        // [START functions_emulator_connect]
        // 10.0.2.2 is the special IP address to connect to the 'localhost' of
        // the host computer from an Android emulator.
        val functions = Firebase.functions
        functions.useEmulator("10.0.2.2.", 5001)
        // [END functions_emulator_connect]
    }
}
