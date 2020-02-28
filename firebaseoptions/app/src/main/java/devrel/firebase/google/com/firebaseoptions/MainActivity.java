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

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // [START firebase_options]
        // Manually configure Firebase Options. The following fields are REQUIRED:
        //   - Project ID
        //   - App ID
        //   - API Key
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setProjectId("my-firebase-project")
                .setApplicationId("1:27992087142:android:ce3b6448250083d1")
                .setApiKey("AIzaSyADUe90ULnQDuGShD9W23RDP0xmeDc6Mvw")
                // setDatabaseURL(...)
                // setStorageBucket(...)
                .build();
        // [END firebase_options]

        // [START firebase_secondary]
        // Initialize with secondary app
        FirebaseApp.initializeApp(this /* Context */, options, "secondary");

        // Retrieve secondary FirebaseApp
        FirebaseApp secondary = FirebaseApp.getInstance("secondary");
        // [END firebase_secondary]
    }
}
