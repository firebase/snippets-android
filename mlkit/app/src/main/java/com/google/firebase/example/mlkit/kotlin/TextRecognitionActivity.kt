package com.google.firebase.example.mlkit.kotlin

import android.support.v7.app.AppCompatActivity
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.document.FirebaseVisionCloudDocumentRecognizerOptions
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentTextRecognizer
import com.google.firebase.ml.vision.text.FirebaseVisionText
import java.util.Arrays

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
        //         .getVisionCloudTextDetector(options)
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

    private fun processTextBlock(result: FirebaseVisionText) {
        // [START mlkit_process_text_block]
        val resultText = result.text
        for (block in result.textBlocks) {
            val blockText = block.text
            val blockConfidence = block.confidence
            val blockLanguages = block.recognizedLanguages
            val blockCornerPoints = block.cornerPoints
            val blockFrame = block.boundingBox
            for (line in block.lines) {
                val lineText = line.text
                val lineConfidence = line.confidence
                val lineLanguages = line.recognizedLanguages
                val lineCornerPoints = line.cornerPoints
                val lineFrame = line.boundingBox
                for (element in line.elements) {
                    val elementText = element.text
                    val elementConfidence = element.confidence
                    val elementLanguages = element.recognizedLanguages
                    val elementCornerPoints = element.cornerPoints
                    val elementFrame = element.boundingBox
                }
            }
        }
        // [END mlkit_process_text_block]
    }

    private fun getLocalDocumentRecognizer(): FirebaseVisionDocumentTextRecognizer {
        // [START mlkit_local_doc_recognizer]
        val textRecognizer = FirebaseVision.getInstance()
                .cloudDocumentTextRecognizer
        // [END mlkit_local_doc_recognizer]

        return textRecognizer
    }

    private fun getCloudDocumentRecognizer(): FirebaseVisionDocumentTextRecognizer {
        // [START mlkit_cloud_doc_recognizer]
        // Or, to provide language hints to assist with language detection:
        // See https://cloud.google.com/vision/docs/languages for supported languages
        val options = FirebaseVisionCloudDocumentRecognizerOptions.Builder()
                .setLanguageHints(Arrays.asList("en", "hi"))
                .build()
        val textRecognizer = FirebaseVision.getInstance()
                .getCloudDocumentTextRecognizer(options)
        // [END mlkit_cloud_doc_recognizer]

        return textRecognizer
    }

    private fun processDocumentImage() {
        // Dummy variables
        val textRecognizer = getLocalDocumentRecognizer()
        val myImage = FirebaseVisionImage.fromByteArray(byteArrayOf(),
                FirebaseVisionImageMetadata.Builder().build())

        // [START mlkit_process_doc_image]
        textRecognizer.processImage(myImage)
                .addOnSuccessListener {
                    // Task completed successfully
                    // ...
                }
                .addOnFailureListener {
                    // Task failed with an exception
                    // ...
                }
        // [END mlkit_process_doc_image]
    }
}
