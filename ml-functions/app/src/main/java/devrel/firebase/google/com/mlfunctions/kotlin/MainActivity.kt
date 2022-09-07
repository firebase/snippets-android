package devrel.firebase.google.com.mlfunctions.kotlin

import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {
    val uri = null;
    // [START function_bitmap]
    var bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
    // [END function_bitmap]
    private lateinit var functions: FirebaseFunctions

    override fun onStart() {
        super.onStart()
        functions = Firebase.functions

        // [START function_scaleDown]
        // Scale down bitmap size
        bitmap = scaleBitmapDown(bitmap, 640)
        // [END function_scaleDown]

        // [START function_convertBitmap]
        // Convert bitmap to base64 encoded string
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
        val base64encoded = Base64.encodeToString(imageBytes, Base64.NO_WRAP)
        // [END function_convertBitmap]
    }

    // [START function_scaleBitmapDown]
    private fun scaleBitmapDown(bitmap: Bitmap, maxDimension: Int): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        var resizedWidth = maxDimension
        var resizedHeight = maxDimension
        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension
            resizedWidth =
                    (resizedHeight * originalWidth.toFloat() / originalHeight.toFloat()).toInt()
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension
            resizedHeight =
                    (resizedWidth * originalHeight.toFloat() / originalWidth.toFloat()).toInt()
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension
            resizedWidth = maxDimension
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false)
    }
    // [END function_scaleBitmapDown]

    // [START function_annotateImage]
    private fun annotateImage(requestJson: String): Task<JsonElement> {
        return functions
                .getHttpsCallable("annotateImage")
                .call(requestJson)
                .continueWith { task ->
                    // This continuation runs on either success or failure, but if the task
                    // has failed then result will throw an Exception which will be
                    // propagated down.
                    val result = task.result?.data
                    JsonParser.parseString(Gson().toJson(result))
                }
    }
    // [END function_annotateImage]

    private fun prepareImageRequest(base64encoded: String) {
        // [START function_imageRequest]
        // Create json request to cloud vision
        val request = JsonObject()
        // Add image to request
        val image = JsonObject()
        image.add("content", JsonPrimitive(base64encoded))
        request.add("image", image)
        //Add features to the request
        val feature = JsonObject()
        feature.add("maxResults", JsonPrimitive(5))
        feature.add("type", JsonPrimitive("LABEL_DETECTION"))
        val features = JsonArray()
        features.add(feature)
        request.add("features", features)
        // [END function_imageRequest]
    }

    private fun prepareLandmarkRequest(base64encoded: String) {
        // [START function_landmarkRequest]
        // Create json request to cloud vision
        val request = JsonObject()
        // Add image to request
        val image = JsonObject()
        image.add("content", JsonPrimitive(base64encoded))
        request.add("image", image)
        //Add features to the request
        val feature = JsonObject()
        feature.add("maxResults", JsonPrimitive(5))
        feature.add("type", JsonPrimitive("LANDMARK_DETECTION"))
        val features = JsonArray()
        features.add(feature)
        request.add("features", features)
        // [END function_landmarkRequest]
    }

    private fun prepareTextRequest(base64encoded: String) {
        // [START function_textRequest]
        // Create json request to cloud vision
        val request = JsonObject()
        // Add image to request
        val image = JsonObject()
        image.add("content", JsonPrimitive(base64encoded))
        request.add("image", image)
        //Add features to the request
        val feature = JsonObject()
        feature.add("type", JsonPrimitive("TEXT_DETECTION"))
        // Alternatively, for DOCUMENT_TEXT_DETECTION:
        // feature.add("type", JsonPrimitive("DOCUMENT_TEXT_DETECTION"))
        val features = JsonArray()
        features.add(feature)
        request.add("features", features)
        // [END function_textRequest]

        // [START function_languageHints]
        val imageContext = JsonObject()
        val languageHints = JsonArray()
        languageHints.add("en")
        imageContext.add("languageHints", languageHints)
        request.add("imageContext", imageContext)
        // [END function_languageHints]
    }

    private fun annotateImage(request: JsonObject) {
        // [START function_callAnnotate]
        annotateImage(request.toString())
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        // Task failed with an exception
                        // ...
                    } else {
                        // Task completed successfully
                        // ...
                    }
                }
        // [END function_callAnnotate]
    }

    private fun getLabeledObjects(task: Task<JsonElement?>) {
        // [START function_getLabeledObjects]
        for (label in task.result!!.asJsonArray[0].asJsonObject["labelAnnotations"].asJsonArray) {
            val labelObj = label.asJsonObject
            val text = labelObj["description"]
            val entityId = labelObj["mid"]
            val confidence = labelObj["score"]
        }
        // [END function_getLabeledObjects]
    }

    private fun getRecognizedLandmarks(task: Task<JsonElement?>) {
        // [START function_getRecognizedLandmarks]
        for (label in task.result!!.asJsonArray[0].asJsonObject["landmarkAnnotations"].asJsonArray) {
            val labelObj = label.asJsonObject
            val landmarkName = labelObj["description"]
            val entityId = labelObj["mid"]
            val score = labelObj["score"]
            val bounds = labelObj["boundingPoly"]
            // Multiple locations are possible, e.g., the location of the depicted
            // landmark and the location the picture was taken.
            for(loc in labelObj["locations"].asJsonArray) {
                val latitude = loc.asJsonObject["latLng"].asJsonObject["latitude"]
                val longitude = loc.asJsonObject["latLng"].asJsonObject["longitude"]
            }
        }
        // [END function_getRecognizedLandmarks]
    }

    private fun getRecognizedTexts(task: Task<JsonElement?>) {
        // [START function_getRecognizedTexts]
        val annotation = task.result!!.asJsonArray[0].asJsonObject["fullTextAnnotation"].asJsonObject
        System.out.format("%nComplete annotation:")
        System.out.format("%n%s", annotation["text"].asString)
        // [END function_getRecognizedTexts]

        // [START function_getRecognizedTexts_details]
        for (page in annotation["pages"].asJsonArray) {
            var pageText = ""
            for (block in page.asJsonObject["blocks"].asJsonArray) {
                var blockText = ""
                for (para in block.asJsonObject["paragraphs"].asJsonArray) {
                    var paraText = ""
                    for (word in para.asJsonObject["words"].asJsonArray) {
                        var wordText = ""
                        for (symbol in word.asJsonObject["symbols"].asJsonArray) {
                            wordText += symbol.asJsonObject["text"].asString
                            System.out.format("Symbol text: %s (confidence: %f)%n",
                                symbol.asJsonObject["text"].asString, symbol.asJsonObject["confidence"].asFloat)
                        }
                        System.out.format("Word text: %s (confidence: %f)%n%n", wordText,
                            word.asJsonObject["confidence"].asFloat)
                        System.out.format("Word bounding box: %s%n", word.asJsonObject["boundingBox"])
                        paraText = String.format("%s%s ", paraText, wordText)
                    }
                    System.out.format("%nParagraph: %n%s%n", paraText)
                    System.out.format("Paragraph bounding box: %s%n", para.asJsonObject["boundingBox"])
                    System.out.format("Paragraph Confidence: %f%n", para.asJsonObject["confidence"].asFloat)
                    blockText += paraText
                }
                pageText += blockText
            }
        }
        // [END function_getRecognizedTexts_details]
    }
}
