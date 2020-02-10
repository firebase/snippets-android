package devrel.firebase.google.com.functions.kotlin

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    fun emulatorSettings() {
        // [START functions_emulator_connect]
        Firebase.functions.useFunctionsEmulator("http://10.0.2.2:5001")
        // [END functions_emulator_connect]
    }
}
