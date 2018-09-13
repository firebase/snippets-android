package com.google.firebase.example.mlkit;

import android.support.v7.app.AppCompatActivity;

import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;

public class MainActivity extends AppCompatActivity {

    public void buildCloudVisionOptions() {
        // [START ml_build_cloud_vision_options]
        FirebaseVisionCloudDetectorOptions options =
                new FirebaseVisionCloudDetectorOptions.Builder()
                        .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                        .setMaxResults(15)
                        .build();
        // [END ml_build_cloud_vision_options]
    }

}
