package com.google.firebase.example.mlkit.kotlin

import android.support.v7.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions

class ImageLabelingActivity : AppCompatActivity() {

    private fun labelImages(image: FirebaseVisionImage) {
        // [START set_detector_options]
        val options = FirebaseVisionLabelDetectorOptions.Builder()
                .setConfidenceThreshold(0.8f)
                .build()
        // [END set_detector_options]

        // [START get_detector_default]
        val detector = FirebaseVision.getInstance()
                .visionLabelDetector
        // [END get_detector_default]

        /*
        // [START get_detector_options]
        // Or, to set the minimum confidence required:
        val detector = FirebaseVision.getInstance()
                .getVisionLabelDetector(options)
        // [END get_detector_options]
        */

        // [START run_detector]
        val result = detector.detectInImage(image)
                .addOnSuccessListener { labels ->
                    // Task completed successfully
                    // [START_EXCLUDE]
                    // [START get_labels]
                    for (label in labels) {
                        val text = label.label
                        val entityId = label.entityId
                        val confidence = label.confidence
                    }
                    // [END get_labels]
                    // [END_EXCLUDE]
                }
                .addOnFailureListener(
                        object : OnFailureListener {
                            override fun onFailure(e: Exception) {
                                // Task failed with an exception
                                // ...
                            }
                        })
        // [END run_detector]
    }

    private fun labelImagesCloud(image: FirebaseVisionImage) {
        // [START set_detector_options_cloud]
        val options = FirebaseVisionCloudDetectorOptions.Builder()
                .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                .setMaxResults(30)
                .build()
        // [END set_detector_options_cloud]

        // [START get_detector_cloud]
        val detector = FirebaseVision.getInstance()
                .visionCloudLabelDetector
        // Or, to change the default settings:
        // val detector = FirebaseVision.getInstance()
        //         .getVisionCloudLabelDetector(options)
        // [END get_detector_cloud]

        // [START run_detector_cloud]
        val result = detector.detectInImage(image)
                .addOnSuccessListener(
                        object : OnSuccessListener<List<FirebaseVisionCloudLabel>> {
                            override fun onSuccess(labels: List<FirebaseVisionCloudLabel>) {
                                // Task completed successfully
                                // [START_EXCLUDE]
                                // [START get_labels_cloud]
                                for (label in labels) {
                                    val text = label.label
                                    val entityId = label.entityId
                                    val confidence = label.confidence
                                }
                                // [END get_labels_cloud]
                                // [END_EXCLUDE]
                            }
                        })
                .addOnFailureListener(
                        object : OnFailureListener {
                            override fun onFailure(e: Exception) {
                                // Task failed with an exception
                                // ...
                            }
                        })
        // [END run_detector_cloud]
    }
}
