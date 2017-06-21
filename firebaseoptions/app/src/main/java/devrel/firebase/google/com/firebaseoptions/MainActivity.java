package devrel.firebase.google.com.firebaseoptions;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // [START firebase_options]
        // Manually configure Firebase Options
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:27992087142:android:ce3b6448250083d1") // Required for Analytics.
                .setApiKey("AIzaSyADUe90ULnQDuGShD9W23RDP0xmeDc6Mvw") // Required for Auth.
                .setDatabaseUrl("https://myproject.firebaseio.com") // Required for RTDB.
                .build();
        // [END firebase_options]

        // [START firebase_secondary]
        // Initialize with secondary app.
        FirebaseApp.initializeApp(this /* Context */, options, "secondary");

        // Retrieve secondary app.
        FirebaseApp secondary = FirebaseApp.getInstance("secondary");
        // Get the database for the other app.
        FirebaseDatabase secondaryDatabase = FirebaseDatabase.getInstance(secondary);
        // [END firebase_secondary]
    }
}