/*
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package devrel.firebase.google.com.firebaseoptions;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
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
