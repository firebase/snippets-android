package com.google.firebase.example.mlkit.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.example.mlkit.interfaces.MainActivityInterface
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions

class MainActivity : AppCompatActivity(), MainActivityInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun buildCloudVisionOptions() {
        // [START ml_build_cloud_vision_options]
        val options = FirebaseVisionCloudDetectorOptions.Builder()
                .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                .setMaxResults(15)
                .build()
        // [END ml_build_cloud_vision_options]
    }

}
