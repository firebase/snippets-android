package devrel.firebase.google.com.functions.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    // [START define_functions_instance]
    private lateinit var functions: FirebaseFunctions
    // [END define_functions_instance]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // [START initialize_functions_instance]
        functions = Firebase.functions
        // [END initialize_functions_instance]
    }

    fun emulatorSettings() {
        // [START functions_emulator_connect]
        // 10.0.2.2 is the special IP address to connect to the 'localhost' of
        // the host computer from an Android emulator.
        val functions = Firebase.functions
        functions.useEmulator("10.0.2.2", 5001)
        // [END functions_emulator_connect]
    }

    // [START function_add_numbers]
    private fun addNumbers(a: Int, b: Int): Task<Int> {
        // Create the arguments to the callable function, which are two integers
        val data = hashMapOf(
            "firstNumber" to a,
            "secondNumber" to b
        )

        // Call the function and extract the operation from the result
        return functions
            .getHttpsCallable("addNumbers")
            .call(data)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then task.result will throw an Exception which will be
                // propagated down.
                val result = task.result?.data as Map<String, Any>
                result["operationResult"] as Int
            }
    }
    // [END function_add_numbers]

    // [START function_add_message]
    private fun addMessage(text: String): Task<String> {
        // Create the arguments to the callable function.
        val data = hashMapOf(
            "text" to text,
            "push" to true
        )

        return functions
            .getHttpsCallable("addMessage")
            .call(data)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then result will throw an Exception which will be
                // propagated down.
                val result = task.result?.data as String
                result
            }
    }
    // [END function_add_message]

    private fun callAddNumbers(firstNumber: Int, secondNumber: Int) {
        // [START call_add_numbers]
        addNumbers(firstNumber, secondNumber)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    val e = task.exception
                    if (e is FirebaseFunctionsException) {
                        // Function error code, will be INTERNAL if the failure
                        // was not handled properly in the function call.
                        val code = e.code

                        // Arbitrary error details passed back from the function,
                        // usually a Map<String, Any>.
                        val details = e.details
                    }
                }
            }
        // [END call_add_numbers]
    }

    private fun callAddMessage(inputMessage: String) {
        // [START call_add_message]
        addMessage(inputMessage)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    val e = task.exception
                    if (e is FirebaseFunctionsException) {
                        val code = e.code
                        val details = e.details
                    }
                }
            }
        // [END call_add_message]
    }
}
