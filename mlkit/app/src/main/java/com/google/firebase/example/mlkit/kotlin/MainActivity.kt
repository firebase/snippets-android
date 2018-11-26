package com.google.firebase.example.mlkit.kotlin

import android.support.v7.app.AppCompatActivity
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import devrel.firebase.google.com.firebaseoptions.BuildConfig

class MainActivity : AppCompatActivity() {

    fun buildCloudVisionOptions() {
        // [START ml_build_cloud_vision_options]
        val options = FirebaseVisionCloudDetectorOptions.Builder()
                .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                .setMaxResults(15)
                .build()
        // [END ml_build_cloud_vision_options]
    }

    fun enforceCertificateMatching() {
        // Dummy variable
        val myImage = FirebaseVisionImage.fromByteArray(byteArrayOf(),
                FirebaseVisionImageMetadata.Builder().build())

        // [START mlkit_certificate_matching]
        val optionsBuilder = FirebaseVisionCloudDetectorOptions.Builder()
        if (!BuildConfig.DEBUG) {
            // Requires physical, non-rooted device:
            optionsBuilder.enforceCertFingerprintMatch()
        }

        // Set other options. For example:
        optionsBuilder.setModelType(FirebaseVisionCloudDetectorOptions.STABLE_MODEL)
        // ...

        // And lastly:
        val options = optionsBuilder.build()
        FirebaseVision.getInstance().getVisionCloudLabelDetector(options).detectInImage(myImage)
        // [END mlkit_certificate_matching]
    }
}
