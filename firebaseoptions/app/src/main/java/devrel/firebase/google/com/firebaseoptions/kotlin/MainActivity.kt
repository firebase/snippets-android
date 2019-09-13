package devrel.firebase.google.com.firebaseoptions.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase
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
        // Initialize with secondary app.
        FirebaseApp.initializeApp(this /* Context */, options, "secondary")

        // Retrieve secondary app.
        val secondary = FirebaseApp.getInstance("secondary")
        // Get the database for the other app.
        val secondaryDatabase = FirebaseDatabase.getInstance(secondary)
        // [END firebase_secondary]
    }
}
