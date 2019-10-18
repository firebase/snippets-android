package com.google.firebase.example.mlkit;

import android.graphics.Bitmap;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.custom.FirebaseCustomLocalModel;
import com.google.firebase.ml.custom.FirebaseCustomRemoteModel;
import com.google.firebase.ml.custom.FirebaseModelDataType;
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions;
import com.google.firebase.ml.custom.FirebaseModelInputs;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.google.firebase.ml.custom.FirebaseModelInterpreterOptions;
import com.google.firebase.ml.custom.FirebaseModelOutputs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CustomModelActivity extends AppCompatActivity {

    private void configureHostedModelSource() {
        // [START mlkit_cloud_model_source]
        FirebaseCustomRemoteModel remoteModel =
                new FirebaseCustomRemoteModel.Builder("your_model").build();
        // [END mlkit_cloud_model_source]
    }

    private void startModelDownloadTask(FirebaseCustomRemoteModel remoteModel) {
        // [START mlkit_model_download_task]
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        FirebaseModelManager.getInstance().download(remoteModel, conditions)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Success.
                    }
                });
        // [END mlkit_model_download_task]
    }

    private void configureLocalModelSource() {
        // [START mlkit_local_model_source]
        FirebaseCustomLocalModel localModel = new FirebaseCustomLocalModel.Builder()
                .setAssetFilePath("your_model.tflite")
                .build();
        // [END mlkit_local_model_source]
    }

    private FirebaseModelInterpreter createInterpreter(FirebaseCustomLocalModel localModel) throws FirebaseMLException {
        // [START mlkit_create_interpreter]
        FirebaseModelInterpreter interpreter = null;
        try {
            FirebaseModelInterpreterOptions options =
                    new FirebaseModelInterpreterOptions.Builder(localModel).build();
            interpreter = FirebaseModelInterpreter.getInstance(options);
        } catch (FirebaseMLException e) {
            // ...
        }
        // [END mlkit_create_interpreter]

        return interpreter;
    }

    private void checkModelDownloadStatus(
            final FirebaseCustomRemoteModel remoteModel,
            final FirebaseCustomLocalModel localModel) {
        // [START mlkit_check_download_status]
        FirebaseModelManager.getInstance().isModelDownloaded(remoteModel)
                .addOnSuccessListener(new OnSuccessListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean isDownloaded) {
                        FirebaseModelInterpreterOptions options;
                        if (isDownloaded) {
                            options = new FirebaseModelInterpreterOptions.Builder(remoteModel).build();
                        } else {
                            options = new FirebaseModelInterpreterOptions.Builder(localModel).build();
                        }
                        try {
                            FirebaseModelInterpreter interpreter = FirebaseModelInterpreter.getInstance(options);
                            // ...
                        } catch (FirebaseMLException e) {

                        }
                    }
                });
        // [END mlkit_check_download_status]
    }

    private void addDownloadListener(
            FirebaseCustomRemoteModel remoteModel,
            FirebaseModelDownloadConditions conditions) {
        // [START mlkit_remote_model_download_listener]
        FirebaseModelManager.getInstance().download(remoteModel, conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Download complete. Depending on your app, you could enable
                        // the ML feature, or switch from the local model to the remote
                        // model, etc.
                    }
                });
        // [END mlkit_remote_model_download_listener]
    }

    private FirebaseModelInputOutputOptions createInputOutputOptions() throws FirebaseMLException {
        // [START mlkit_create_io_options]
        FirebaseModelInputOutputOptions inputOutputOptions =
                new FirebaseModelInputOutputOptions.Builder()
                        .setInputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 224, 224, 3})
                        .setOutputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 5})
                        .build();
        // [END mlkit_create_io_options]

        return inputOutputOptions;
    }

    private float[][][][] bitmapToInputArray() {
        // [START mlkit_bitmap_input]
        Bitmap bitmap = getYourInputImage();
        bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);

        int batchNum = 0;
        float[][][][] input = new float[1][224][224][3];
        for (int x = 0; x < 224; x++) {
            for (int y = 0; y < 224; y++) {
                int pixel = bitmap.getPixel(x, y);
                // Normalize channel values to [-1.0, 1.0]. This requirement varies by
                // model. For example, some models might require values to be normalized
                // to the range [0.0, 1.0] instead.
                input[batchNum][x][y][0] = (Color.red(pixel) - 127) / 128.0f;
                input[batchNum][x][y][1] = (Color.green(pixel) - 127) / 128.0f;
                input[batchNum][x][y][2] = (Color.blue(pixel) - 127) / 128.0f;
            }
        }
        // [END mlkit_bitmap_input]

        return input;
    }

    private void runInference() throws FirebaseMLException {
        FirebaseCustomLocalModel localModel = new FirebaseCustomLocalModel.Builder().build();
        FirebaseModelInterpreter firebaseInterpreter = createInterpreter(localModel);
        float[][][][] input = bitmapToInputArray();
        FirebaseModelInputOutputOptions inputOutputOptions = createInputOutputOptions();

        // [START mlkit_run_inference]
        FirebaseModelInputs inputs = new FirebaseModelInputs.Builder()
                .add(input)  // add() as many input arrays as your model requires
                .build();
        firebaseInterpreter.run(inputs, inputOutputOptions)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseModelOutputs>() {
                            @Override
                            public void onSuccess(FirebaseModelOutputs result) {
                                // [START_EXCLUDE]
                                // [START mlkit_read_result]
                                float[][] output = result.getOutput(0);
                                float[] probabilities = output[0];
                                // [END mlkit_read_result]
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
        // [END mlkit_run_inference]
    }

    private void useInferenceResult(float[] probabilities) throws IOException {
        // [START mlkit_use_inference_result]
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(getAssets().open("retrained_labels.txt")));
        for (int i = 0; i < probabilities.length; i++) {
            String label = reader.readLine();
            Log.i("MLKit", String.format("%s: %1.4f", label, probabilities[i]));
        }
        // [END mlkit_use_inference_result]
    }

    private Bitmap getYourInputImage() {
        // This method is just for show
        return Bitmap.createBitmap(0, 0, Bitmap.Config.ALPHA_8);
    }
}

