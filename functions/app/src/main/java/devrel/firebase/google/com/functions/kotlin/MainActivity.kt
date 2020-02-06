package devrel.firebase.google.com.functions.kotlin

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.functions.FirebaseFunctions

class MainActivity : AppCompatActivity() {

    fun emulatorSettings() {
        // [START functions_emulator_connect]
        FirebaseFunctions.getInstance().useFunctionsEmulator("http://10.0.2.2:5001")
        // [END functions_emulator_connect]
    }
}
