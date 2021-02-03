/*
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package devrel.firebase.google.com.mlfunctions;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

public class MainActivity extends AppCompatActivity {

    private FirebaseFunctions mFunctions;
    Uri uri = null;
    // [START function_bitmap]
    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
    // [END function_bitmap]

    public MainActivity() throws IOException {
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFunctions = FirebaseFunctions.getInstance();

        // [START function_scaleDown]
        // Scale down bitmap size
        bitmap = scaleBitmapDown(bitmap, 640);
        // [END function_scaleDown]

        // [START function_convertBitmap]
        // Convert bitmap to base64 encoded string
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String base64encoded = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        // [END function_convertBitmap]
    }

    // [START function_scaleBitmapDown]
    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }
    // [END function_scaleBitmapDown]

    // [START function_annotateImage]
    private Task<JsonElement> annotateImage(String requestJson) {
        return mFunctions
                .getHttpsCallable("annotateImage")
                .call(requestJson)
                .continueWith(new Continuation<HttpsCallableResult, JsonElement>() {
                    @Override
                    public JsonElement then(@NonNull Task<HttpsCallableResult> task) {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        return JsonParser.parseString(new Gson().toJson(task.getResult().getData()));
                    }
                });
    }
    // [END function_annotateImage]


    private void prepareImageRequest(String base64encoded) {
        // [START function_imageRequest]
        // Create json request to cloud vision
        JsonObject request = new JsonObject();
        // Add image to request
        JsonObject image = new JsonObject();
        image.add("content", new JsonPrimitive(base64encoded));
        request.add("image", image);
        //Add features to the request
        JsonObject feature = new JsonObject();
        feature.add("maxResults", new JsonPrimitive(5));
        feature.add("type", new JsonPrimitive("LABEL_DETECTION"));
        JsonArray features = new JsonArray();
        features.add(feature);
        request.add("features", features);
        // [END function_imageRequest]
    }

    private void prepareLandmarkRequest(String base64encoded) {
        // [START function_landmarkRequest]
        // Create json request to cloud vision
        JsonObject request = new JsonObject();
        // Add image to request
        JsonObject image = new JsonObject();
        image.add("content", new JsonPrimitive(base64encoded));
        request.add("image", image);
        //Add features to the request
        JsonObject feature = new JsonObject();
        feature.add("maxResults", new JsonPrimitive(5));
        feature.add("type", new JsonPrimitive("LANDMARK_DETECTION"));
        JsonArray features = new JsonArray();
        features.add(feature);
        request.add("features", features);
        // [END function_landmarkRequest]
    }

    private void prepareTextRequest(String base64encoded) {
        // [START function_textRequest]
        // Create json request to cloud vision
        JsonObject request = new JsonObject();
        // Add image to request
        JsonObject image = new JsonObject();
        image.add("content", new JsonPrimitive(base64encoded));
        request.add("image", image);
        //Add features to the request
        JsonObject feature = new JsonObject();
        feature.add("type", new JsonPrimitive("TEXT_DETECTION"));
        // Alternatively, for DOCUMENT_TEXT_DETECTION:
        //feature.add("type", new JsonPrimitive("DOCUMENT_TEXT_DETECTION"));
        JsonArray features = new JsonArray();
        features.add(feature);
        request.add("features", features);
        // [END function_textRequest]

        // [START function_languageHints]
        JsonObject imageContext = new JsonObject();
        JsonArray languageHints = new JsonArray();
        languageHints.add("en");
        imageContext.add("languageHints", languageHints);
        request.add("imageContext", imageContext);
        // [END function_languageHints]
    }

    private void callAnnotate(JsonObject request) {
        // [START function_callAnnotate]
        annotateImage(request.toString())
                .addOnCompleteListener(new OnCompleteListener<JsonElement>() {
                    @Override
                    public void onComplete(@NonNull Task<JsonElement> task) {
                        if (!task.isSuccessful()) {
                            // Task failed with an exception
                            // ...
                        } else {
                            // Task completed successfully
                            // ...
                        }
                    }
                });
        // [END function_callAnnotate]
    }

    private void getLabeledObjects(Task<JsonElement> task) {
        // [START function_getLabeledObjects]
        for (JsonElement label : task.getResult().getAsJsonArray().get(0).getAsJsonObject().get("labelAnnotations").getAsJsonArray()) {
            JsonObject labelObj = label.getAsJsonObject();
            String text = labelObj.get("description").getAsString();
            String entityId = labelObj.get("mid").getAsString();
            float score = labelObj.get("score").getAsFloat();
        }
        // [END function_getLabeledObjects]
    }

    private void getRecognizedLandmarks(Task<JsonElement> task) {
        // [START function_getRecognizedLandmarks]
        for (JsonElement label : task.getResult().getAsJsonArray().get(0).getAsJsonObject().get("landmarkAnnotations").getAsJsonArray()) {
            JsonObject labelObj = label.getAsJsonObject();
            String landmarkName = labelObj.get("description").getAsString();
            String entityId = labelObj.get("mid").getAsString();
            float score = labelObj.get("score").getAsFloat();
            JsonObject bounds = labelObj.get("boundingPoly").getAsJsonObject();
            // Multiple locations are possible, e.g., the location of the depicted
            // landmark and the location the picture was taken.
            for (JsonElement loc : labelObj.get("locations").getAsJsonArray()) {
                JsonObject latLng = loc.getAsJsonObject().get("latLng").getAsJsonObject();
                double latitude = latLng.get("latitude").getAsDouble();
                double longitude = latLng.get("longitude").getAsDouble();
            }
        }
        // [END function_getRecognizedLandmarks]
    }

    private void getRecognizedTexts(Task<JsonElement> task) {
        // [START function_getRecognizedTexts]
        JsonObject annotation = task.getResult().getAsJsonArray().get(0).getAsJsonObject().get("fullTextAnnotation").getAsJsonObject();
        System.out.format("%nComplete annotation:%n");
        System.out.format("%s%n", annotation.get("text").getAsString());
        // [END function_getRecognizedTexts]

        // [START function_getRecognizedTexts_details]
        for (JsonElement page : annotation.get("pages").getAsJsonArray()) {
            StringBuilder pageText = new StringBuilder();
            for (JsonElement block : page.getAsJsonObject().get("blocks").getAsJsonArray()) {
                StringBuilder blockText = new StringBuilder();
                for (JsonElement para : block.getAsJsonObject().get("paragraphs").getAsJsonArray()) {
                    StringBuilder paraText = new StringBuilder();
                    for (JsonElement word : para.getAsJsonObject().get("words").getAsJsonArray()) {
                        StringBuilder wordText = new StringBuilder();
                        for (JsonElement symbol : word.getAsJsonObject().get("symbols").getAsJsonArray()) {
                            wordText.append(symbol.getAsJsonObject().get("text").getAsString());
                            System.out.format("Symbol text: %s (confidence: %f)%n", symbol.getAsJsonObject().get("text").getAsString(), symbol.getAsJsonObject().get("confidence").getAsFloat());
                        }
                        System.out.format("Word text: %s (confidence: %f)%n%n", wordText.toString(), word.getAsJsonObject().get("confidence").getAsFloat());
                        System.out.format("Word bounding box: %s%n", word.getAsJsonObject().get("boundingBox"));
                        paraText.append(wordText.toString()).append(" ");
                    }
                    System.out.format("%nParagraph:%n%s%n", paraText);
                    System.out.format("Paragraph bounding box: %s%n", para.getAsJsonObject().get("boundingBox"));
                    System.out.format("Paragraph Confidence: %f%n", para.getAsJsonObject().get("confidence").getAsFloat());
                    blockText.append(paraText);
                }
                pageText.append(blockText);
            }
        }
        // [END function_getRecognizedTexts_details]
    }
}
