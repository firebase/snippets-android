package com.google.firebase.quickstart.mlkit;

import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.cloud.text.FirebaseVisionCloudText;
import com.google.firebase.ml.vision.cloud.text.FirebaseVisionCloudTextDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.util.List;

public class TextRecognitionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_recognition);
    }


    private void recognizeText(FirebaseVisionImage image) {

        // [START get_detector_default]
        FirebaseVisionTextDetector detector = FirebaseVision.getInstance()
                .getVisionTextDetector();
        // [END get_detector_default]

        // [START run_detector]
        Task<FirebaseVisionText> result =
                detector.detectInImage(image)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                // Task completed successfully
                                // [START_EXCLUDE]
                                // [START get_text]
                                for (FirebaseVisionText.Block block: firebaseVisionText.getBlocks()) {
                                    Rect boundingBox = block.getBoundingBox();
                                    Point[] cornerPoints = block.getCornerPoints();
                                    String text = block.getText();

                                    for (FirebaseVisionText.Line line: block.getLines()) {
                                        // ...
                                        for (FirebaseVisionText.Element element: line.getElements()) {
                                            // ...
                                        }
                                    }
                                }
                                // [END get_text]
                                // [END_EXCLUDE]
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });
        // [END run_detector]
    }

    private void recognizeTextCloud(FirebaseVisionImage image) {
        // [START set_detector_options_cloud]
        FirebaseVisionCloudDetectorOptions options = new FirebaseVisionCloudDetectorOptions.Builder()
                .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                .setMaxResults(30)
                .build();
        // [END set_detector_options_cloud]

        // [START get_detector_cloud]
        FirebaseVisionCloudTextDetector detector = FirebaseVision.getInstance()
                .getVisionCloudTextDetector();
        // Or, to change the default settings:
        // FirebaseVisionCloudTextDetector detector = FirebaseVision.getInstance()
        //         .getVisionCloudTextDetector(options);
        // [END get_detector_cloud]

        // [START run_detector_cloud]
        Task<FirebaseVisionCloudText> result = detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionCloudText>() {
                    @Override
                    public void onSuccess(FirebaseVisionCloudText firebaseVisionCloudText) {
                        // Task completed successfully
                        // [START_EXCLUDE]
                        // [START get_text_cloud]
                        String recognizedText = firebaseVisionCloudText.getText();

                        for (FirebaseVisionCloudText.Page page: firebaseVisionCloudText.getPages()) {
                            List<FirebaseVisionCloudText.DetectedLanguage> languages =
                                    page.getTextProperty().getDetectedLanguages();
                            int height = page.getHeight();
                            int width = page.getWidth();
                            float confidence = page.getConfidence();

                            for (FirebaseVisionCloudText.Block block: page.getBlocks()) {
                                Rect boundingBox = block.getBoundingBox();
                                List<FirebaseVisionCloudText.DetectedLanguage> blockLanguages =
                                        block.getTextProperty().getDetectedLanguages();
                                float blockConfidence = block.getConfidence();
                                // And so on: Paragraph, Word, Symbol
                            }
                        }
                        // [END get_text_cloud]
                        // [END_EXCLUDE]
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                    }
                });
        // [END run_detector_cloud]
    }
}
