package com.google.firebase.example.mlkit.kotlin

import android.support.v7.app.AppCompatActivity
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage

class TextRecognitionActivity : AppCompatActivity() {

    private fun recognizeText(image: FirebaseVisionImage) {

        // [START get_detector_default]
        val detector = FirebaseVision.getInstance()
                .onDeviceTextRecognizer
        // [END get_detector_default]

        // [START run_detector]
        val result = detector.processImage(image)
                .addOnSuccessListener { firebaseVisionText ->
                    // Task completed successfully
                    // [START_EXCLUDE]
                    // [START get_text]
                    for (block in firebaseVisionText.textBlocks) {
                        val boundingBox = block.boundingBox
                        val cornerPoints = block.cornerPoints
                        val text = block.text

                        for (line in block.lines) {
                            // ...
                            for (element in line.elements) {
                                // ...
                            }
                        }
                    }
                    // [END get_text]
                    // [END_EXCLUDE]
                }
                .addOnFailureListener {
                    // Task failed with an exception
                    // ...
                }
        // [END run_detector]
    }

    private fun recognizeTextCloud(image: FirebaseVisionImage) {
        // [START set_detector_options_cloud]
        val options = FirebaseVisionCloudDetectorOptions.Builder()
                .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                .setMaxResults(30)
                .build()
        // [END set_detector_options_cloud]

        // [START get_detector_cloud]
        val detector = FirebaseVision.getInstance()
                .cloudTextRecognizer
        // Or, to change the default settings:
        // FirebaseVisionTextDetector detector = FirebaseVision.getInstance()
        //         .getVisionCloudTextDetector(options);
        // [END get_detector_cloud]

        // [START run_detector_cloud]
        val result = detector.processImage(image)
                .addOnSuccessListener { firebaseVisionText ->
                    // Task completed successfully
                    // [START_EXCLUDE]
                    // [START get_text_cloud]
                    for (block in firebaseVisionText.textBlocks) {
                        val boundingBox = block.boundingBox
                        val cornerPoints = block.cornerPoints
                        val text = block.text

                        for (line in block.lines) {
                            // ...
                            for (element in line.elements) {
                                // ...
                            }
                        }
                    }
                    // [END get_text_cloud]
                    // [END_EXCLUDE]
                }
                .addOnFailureListener {
                    // Task failed with an exception
                    // ...
                }
        // [END run_detector_cloud]
    }
}
