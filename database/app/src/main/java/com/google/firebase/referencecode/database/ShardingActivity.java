package com.google.firebase.referencecode.database;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShardingActivity extends AppCompatActivity {

    private void getMultipleInstances() {
        // [START rtdb_multi_instance]
        // Get the default database instance for an app
        DatabaseReference primary = FirebaseDatabase.getInstance()
                .getReference();

        // Get a secondary database instance by URL
        DatabaseReference secondary = FirebaseDatabase.getInstance("https://testapp-1234.firebaseio.com")
                .getReference();
        // [END rtdb_multi_instance]
    }
}
