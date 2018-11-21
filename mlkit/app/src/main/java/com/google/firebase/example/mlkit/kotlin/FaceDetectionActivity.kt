package com.google.firebase.example.mlkit.kotlin

import android.support.v7.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark

class FaceDetectionActivity : AppCompatActivity() {

    private fun detectFaces(image: FirebaseVisionImage) {
        // [START set_detector_options]
        val options = FirebaseVisionFaceDetectorOptions.Builder()
                .setClassificationMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .setMinFaceSize(0.15f)
                .enableTracking()
                .build()
        // [END set_detector_options]

        // [START get_detector]
        val detector = FirebaseVision.getInstance()
                .getVisionFaceDetector(options)
        // [END get_detector]

        // [START run_detector]
        val result = detector.detectInImage(image)
                .addOnSuccessListener { faces ->
                    // Task completed successfully
                    // [START_EXCLUDE]
                    // [START get_face_info]
                    for (face in faces) {
                        val bounds = face.boundingBox
                        val rotY = face.headEulerAngleY  // Head is rotated to the right rotY degrees
                        val rotZ = face.headEulerAngleZ  // Head is tilted sideways rotZ degrees

                        // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                        // nose available):
                        val leftEar = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR)
                        if (leftEar != null) {
                            val leftEarPos = leftEar!!.position
                        }

                        // If classification was enabled:
                        if (face.smilingProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                            val smileProb = face.smilingProbability
                        }
                        if (face.rightEyeOpenProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                            val rightEyeOpenProb = face.rightEyeOpenProbability
                        }

                        // If face tracking was enabled:
                        if (face.trackingId != FirebaseVisionFace.INVALID_ID) {
                            val id = face.trackingId
                        }
                    }
                    // [END get_face_info]
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
}
