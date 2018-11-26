package com.google.firebase.example.mlkit;

import android.support.v7.app.AppCompatActivity;

import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;

import devrel.firebase.google.com.firebaseoptions.BuildConfig;

public class MainActivity extends AppCompatActivity {

    public void buildCloudVisionOptions() {
        // [START ml_build_cloud_vision_options]
        FirebaseVisionCloudDetectorOptions options =
                new FirebaseVisionCloudDetectorOptions.Builder()
                        .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                        .setMaxResults(15)
                        .build();
        // [END ml_build_cloud_vision_options]
    }

    public void enforceCertificateMatching() {
        // Dummy variable
        FirebaseVisionImage myImage = FirebaseVisionImage.fromByteArray(new byte[]{},
                new FirebaseVisionImageMetadata.Builder().build());

        // [START mlkit_certificate_matching]
        FirebaseVisionCloudDetectorOptions.Builder optionsBuilder =
                new FirebaseVisionCloudDetectorOptions.Builder();
        if (!BuildConfig.DEBUG) {
            // Requires physical, non-rooted device:
            optionsBuilder.enforceCertFingerprintMatch();
        }

        // Set other options. For example:
        optionsBuilder.setModelType(FirebaseVisionCloudDetectorOptions.STABLE_MODEL);
        // ...

        // And lastly:
        FirebaseVisionCloudDetectorOptions options = optionsBuilder.build();
        FirebaseVision.getInstance().getVisionCloudLabelDetector(options).detectInImage(myImage);
        // [END mlkit_certificate_matching]
    }

}
