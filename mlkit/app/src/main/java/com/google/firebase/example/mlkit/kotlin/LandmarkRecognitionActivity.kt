package com.google.firebase.example.mlkit.kotlin

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage

class LandmarkRecognitionActivity : AppCompatActivity() {

    private fun recognizeLandmarksCloud(image: FirebaseVisionImage) {
        // [START set_detector_options_cloud]
        val options = FirebaseVisionCloudDetectorOptions.Builder()
                .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                .setMaxResults(30)
                .build()
        // [END set_detector_options_cloud]

        // [START get_detector_cloud]
        val detector = FirebaseVision.getInstance()
                .visionCloudLandmarkDetector
        // Or, to change the default settings:
        // val detector = FirebaseVision.getInstance()
        //         .getVisionCloudLandmarkDetector(options)
        // [END get_detector_cloud]

        // [START fml_run_detector_cloud]
        val result = detector.detectInImage(image)
                .addOnSuccessListener { firebaseVisionCloudLandmarks ->
                    // Task completed successfully
                    // [START_EXCLUDE]
                    // [START get_landmarks_cloud]
                    for (landmark in firebaseVisionCloudLandmarks) {

                        val bounds = landmark.boundingBox
                        val landmarkName = landmark.landmark
                        val entityId = landmark.entityId
                        val confidence = landmark.confidence

                        // Multiple locations are possible, e.g., the location of the depicted
                        // landmark and the location the picture was taken.
                        for (loc in landmark.locations) {
                            val latitude = loc.latitude
                            val longitude = loc.longitude
                        }
                    }
                    // [END get_landmarks_cloud]
                    // [END_EXCLUDE]
                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    // ...
                }
        // [END fml_run_detector_cloud]
    }
}
