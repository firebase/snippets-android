package com.google.firebase.example.mlkit.kotlin

import android.support.v7.app.AppCompatActivity
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions

class MainActivity : AppCompatActivity() {

    fun buildCloudVisionOptions() {
        // [START ml_build_cloud_vision_options]
        val options = FirebaseVisionCloudDetectorOptions.Builder()
                .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                .setMaxResults(15)
                .build()
        // [END ml_build_cloud_vision_options]
    }
}
