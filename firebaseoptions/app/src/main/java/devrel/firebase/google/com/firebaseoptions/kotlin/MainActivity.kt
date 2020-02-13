package devrel.firebase.google.com.firebaseoptions.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.ktx.initialize
import devrel.firebase.google.com.firebaseoptions.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // [START firebase_options]
        // Manually configure Firebase Options
        val options = FirebaseOptions.Builder()
                .setApplicationId("1:27992087142:android:ce3b6448250083d1") // Required for Analytics.
                .setApiKey("AIzaSyADUe90ULnQDuGShD9W23RDP0xmeDc6Mvw") // Required for Auth.
                .setDatabaseUrl("https://myproject.firebaseio.com") // Required for RTDB.
                .build()
        // [END firebase_options]

        // [START firebase_secondary]
        // Initialize secondary FirebaseApp.
        Firebase.initialize(this /* Context */, options, "secondary")

        // Retrieve secondary FirebaseApp.
        val secondary = Firebase.app("secondary")
        // Get the database for the other app.
        val secondaryDatabase = Firebase.database(secondary)
        // [END firebase_secondary]
    }
}
