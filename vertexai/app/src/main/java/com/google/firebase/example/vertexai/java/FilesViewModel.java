package com.google.firebase.example.vertexai.java;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.vertexai.FirebaseVertexAI;
import com.google.firebase.vertexai.GenerativeModel;
import com.google.firebase.vertexai.java.GenerativeModelFutures;
import com.google.firebase.vertexai.type.Content;
import com.google.firebase.vertexai.type.GenerateContentResponse;

import java.io.File;
import java.util.concurrent.Executor;

public class FilesViewModel extends ViewModel {
    GenerativeModel generativeModel = FirebaseVertexAI.getInstance()
            .generativeModel("gemini-1.5-pro-preview-0409");
    GenerativeModelFutures model = GenerativeModelFutures.from(generativeModel);

    void uploadAndUseReference(Executor executor) {
        // [START vertexai_storage_upload]
        // Upload raw data to storage reference
        StorageReference storage = FirebaseStorage.getInstance().getReference();
        Uri fileUri = Uri.fromFile(new File("images/image.jpg"));

        storage.putFile(fileUri).addOnSuccessListener(taskSnapshot -> {
            // Get the MIME type and Cloud Storage URL
            String mimeType = taskSnapshot.getMetadata().getContentType();
            String bucket = taskSnapshot.getMetadata().getBucket();
            String filePath = taskSnapshot.getMetadata().getPath();

            if (mimeType != null && bucket != null) {
                String storageUrl = "gs://" + bucket + "/" + filePath;
                Content prompt = new Content.Builder()
                        .addFilePart(mimeType, storageUrl)
                        .addText("What's in this picture?")
                        .build();

                // To generate text output, call generateContent with the prompt
                ListenableFuture<GenerateContentResponse> response = model.generateContent(prompt);
                Futures.addCallback(response, new FutureCallback<>() {
                    @Override
                    public void onSuccess(GenerateContentResponse result) {
                        String resultText = result.getText();
                        System.out.println(resultText);
                    }

                    @Override
                    public void onFailure(@NonNull Throwable t) {
                        t.printStackTrace();
                    }
                }, executor);
            }
        }).addOnFailureListener(e -> {
            // An error occurred while uploading the file
            e.printStackTrace();
        });
        // [END vertexai_storage_upload]
    }

    void useObjectReference(Executor executor) {
        // [START vertexai_storage_ref]
        // Explicitly include the MIME type and Cloud Storage URL
        Content prompt = new Content.Builder()
                .addFilePart("image/jpeg", "gs://bucket-name/path/image.jpg")
                .addText("What's in this picture?")
                .build();

        // To generate text output, call generateContent with the prompt
        ListenableFuture<GenerateContentResponse> response = model.generateContent(prompt);
        Futures.addCallback(response, new FutureCallback<>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();
                System.out.println(resultText);
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                t.printStackTrace();
            }
        }, executor);
        // [END vertexai_storage_ref]
    }
}
