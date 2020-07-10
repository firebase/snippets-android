package com.google.firebase.example.mlkit.kotlin

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.document.FirebaseVisionCloudDocumentRecognizerOptions
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentTextRecognizer
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import com.google.firebase.ml.vision.text.FirebaseVisionText
import java.util.Arrays

class TextRecognitionActivity : AppCompatActivity() {

    private fun recognizeText(image: FirebaseVisionImage) {

        // [START get_detector_default]
        val detector = FirebaseVision.getInstance()
                .onDeviceTextRecognizer
        // [END get_detector_default]

        // [START fml_run_detector]
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
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    // ...
                }
        // [END fml_run_detector]
    }

    private fun recognizeTextCloud(image: FirebaseVisionImage) {
        // [START set_detector_options_cloud]
        val options = FirebaseVisionCloudTextRecognizerOptions.Builder()
                .setLanguageHints(listOf("en", "hi"))
                .build()
        // [END set_detector_options_cloud]

        // [START get_detector_cloud]
        val detector = FirebaseVision.getInstance().cloudTextRecognizer
        // Or, to change the default settings:
        // val detector = FirebaseVision.getInstance().getCloudTextRecognizer(options)
        // [END get_detector_cloud]

        // [START fml_run_detector_cloud]
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
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    // ...
                }
        // [END fml_run_detector_cloud]
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
        val detector = FirebaseVision.getInstance()
                .cloudDocumentTextRecognizer
        // [END mlkit_local_doc_recognizer]

        return detector
    }

    private fun getCloudDocumentRecognizer(): FirebaseVisionDocumentTextRecognizer {
        // [START mlkit_cloud_doc_recognizer]
        // Or, to provide language hints to assist with language detection:
        // See https://cloud.google.com/vision/docs/languages for supported languages
        val options = FirebaseVisionCloudDocumentRecognizerOptions.Builder()
                .setLanguageHints(listOf("en", "hi"))
                .build()
        val detector = FirebaseVision.getInstance()
                .getCloudDocumentTextRecognizer(options)
        // [END mlkit_cloud_doc_recognizer]

        return detector
    }

    private fun processDocumentImage() {
        // Dummy variables
        val detector = getLocalDocumentRecognizer()
        val myImage = FirebaseVisionImage.fromByteArray(byteArrayOf(),
                FirebaseVisionImageMetadata.Builder().build())

        // [START mlkit_process_doc_image]
        detector.processImage(myImage)
                .addOnSuccessListener { firebaseVisionDocumentText ->
                    // Task completed successfully
                    // ...
                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    // ...
                }
        // [END mlkit_process_doc_image]
    }

    private fun processDocumentTextBlock(result: FirebaseVisionDocumentText) {
        // [START mlkit_process_document_text_block]
        val resultText = result.text
        for (block in result.blocks) {
            val blockText = block.text
            val blockConfidence = block.confidence
            val blockRecognizedLanguages = block.recognizedLanguages
            val blockFrame = block.boundingBox
            for (paragraph in block.paragraphs) {
                val paragraphText = paragraph.text
                val paragraphConfidence = paragraph.confidence
                val paragraphRecognizedLanguages = paragraph.recognizedLanguages
                val paragraphFrame = paragraph.boundingBox
                for (word in paragraph.words) {
                    val wordText = word.text
                    val wordConfidence = word.confidence
                    val wordRecognizedLanguages = word.recognizedLanguages
                    val wordFrame = word.boundingBox
                    for (symbol in word.symbols) {
                        val symbolText = symbol.text
                        val symbolConfidence = symbol.confidence
                        val symbolRecognizedLanguages = symbol.recognizedLanguages
                        val symbolFrame = symbol.boundingBox
                    }
                }
            }
        }
        // [END mlkit_process_document_text_block]
    }
}
