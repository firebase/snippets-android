package com.google.firebase.example.mlkit;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceImageLabelerOptions;

import java.util.List;

public class ImageLabelingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void labelImages(FirebaseVisionImage image) {
        // [START set_detector_options]
        FirebaseVisionOnDeviceImageLabelerOptions options =
                new FirebaseVisionOnDeviceImageLabelerOptions.Builder()
                        .setConfidenceThreshold(0.8f)
                        .build();
        // [END set_detector_options]

        // [START get_detector_default]
        FirebaseVisionImageLabeler detector = FirebaseVision.getInstance()
                .getOnDeviceImageLabeler();
        // [END get_detector_default]

        /*
        // [START get_detector_options]
        // Or, to set the minimum confidence required:
        FirebaseVisionImageLabeled detector = FirebaseVision.getInstance()
                .getOnDeviceImageLabeler(options);
        // [END get_detector_options]
        */

        // [START fml_run_detector]
        Task<List<FirebaseVisionImageLabel>> result =
                detector.processImage(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                                        // Task completed successfully
                                        // [START_EXCLUDE]
                                        // [START get_labels]
                                        for (FirebaseVisionImageLabel label: labels) {
                                            String text = label.getText();
                                            String entityId = label.getEntityId();
                                            float confidence = label.getConfidence();
                                        }
                                        // [END get_labels]
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
        // [END fml_run_detector]
    }

    private void labelImagesCloud(FirebaseVisionImage image) {
        // [START set_detector_options_cloud]
        FirebaseVisionCloudDetectorOptions options = new FirebaseVisionCloudDetectorOptions.Builder()
                .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                .setMaxResults(30)
                .build();
        // [END set_detector_options_cloud]

        // [START get_detector_cloud]
        FirebaseVisionImageLabeler detector = FirebaseVision.getInstance()
                .getCloudImageLabeler();

        // Or, to change the default settings:
        // FirebaseVisionImageLabeler detector = FirebaseVision.getInstance()
        //                .getCloudImageLabeler(options);
        // [END get_detector_cloud]

        // [START fml_run_detector_cloud]
        Task<List<FirebaseVisionImageLabel>> result =
                detector.processImage(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                                        // Task completed successfully
                                        // [START_EXCLUDE]
                                        // [START get_labels_cloud]
                                        for (FirebaseVisionImageLabel label : labels) {
                                            String text = label.getText();
                                            String entityId = label.getEntityId();
                                            float confidence = label.getConfidence();
                                        }
                                        // [END get_labels_cloud]
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
        // [END fml_run_detector_cloud]
    }
}
