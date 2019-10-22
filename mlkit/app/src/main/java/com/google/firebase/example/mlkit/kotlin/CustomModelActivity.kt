@file:Suppress("UnnecessaryVariable")

package com.google.firebase.example.mlkit.kotlin

import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.ml.common.FirebaseMLException
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager
import com.google.firebase.ml.custom.FirebaseCustomLocalModel
import com.google.firebase.ml.custom.FirebaseCustomRemoteModel
import com.google.firebase.ml.custom.FirebaseModelInterpreterOptions
import com.google.firebase.ml.custom.FirebaseModelInterpreter
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions
import com.google.firebase.ml.custom.FirebaseModelInputs
import com.google.firebase.ml.custom.FirebaseModelDataType

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class CustomModelActivity : AppCompatActivity() {

    // This method is just for show
    private
    val yourInputImage: Bitmap
        get() = Bitmap.createBitmap(0, 0, Bitmap.Config.ALPHA_8)

    private fun configureHostedModelSource() {
        // [START mlkit_cloud_model_source]
        val remoteModel = FirebaseCustomRemoteModel.Builder("your_model").build()
        // [END mlkit_cloud_model_source]
    }

    private fun startModelDownloadTask(remoteModel: FirebaseCustomRemoteModel) {
        // [START mlkit_model_download_task]
        val conditions = FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build()
        FirebaseModelManager.getInstance().download(remoteModel, conditions)
                .addOnCompleteListener {
                    // Success.
                }
        // [END mlkit_model_download_task]
    }

    private fun configureLocalModelSource() {
        // [START mlkit_local_model_source]
        val localModel = FirebaseCustomLocalModel.Builder()
                .setAssetFilePath("your_model.tflite")
                .build()
        // [END mlkit_local_model_source]
    }

    @Throws(FirebaseMLException::class)
    private fun createInterpreter(localModel: FirebaseCustomLocalModel): FirebaseModelInterpreter? {
        // [START mlkit_create_interpreter]
        val options = FirebaseModelInterpreterOptions.Builder(localModel).build()
        val interpreter = FirebaseModelInterpreter.getInstance(options)
        // [END mlkit_create_interpreter]

        return interpreter
    }

    private fun checkModelDownloadStatus(remoteModel: FirebaseCustomRemoteModel, localModel: FirebaseCustomLocalModel) {
        // [START mlkit_check_download_status]
        FirebaseModelManager.getInstance().isModelDownloaded(remoteModel)
                .addOnSuccessListener { isDownloaded ->
                    val options =
                            if (isDownloaded) {
                                FirebaseModelInterpreterOptions.Builder(remoteModel).build()
                            } else {
                                FirebaseModelInterpreterOptions.Builder(localModel).build()
                            }
                    val interpreter = FirebaseModelInterpreter.getInstance(options)
                }
        // [END mlkit_check_download_status]
    }

    private fun addDownloadListener(
            remoteModel: FirebaseCustomRemoteModel,
            conditions: FirebaseModelDownloadConditions
    ) {
        // [START mlkit_remote_model_download_listener]
        FirebaseModelManager.getInstance().download(remoteModel, conditions)
                .addOnCompleteListener {
                    // Download complete. Depending on your app, you could enable the ML
                    // feature, or switch from the local model to the remote model, etc.
                }
        // [END mlkit_remote_model_download_listener]
    }

    @Throws(FirebaseMLException::class)
    private fun createInputOutputOptions(): FirebaseModelInputOutputOptions {
        // [START mlkit_create_io_options]
        val inputOutputOptions = FirebaseModelInputOutputOptions.Builder()
                .setInputFormat(0, FirebaseModelDataType.FLOAT32, intArrayOf(1, 224, 224, 3))
                .setOutputFormat(0, FirebaseModelDataType.FLOAT32, intArrayOf(1, 5))
                .build()
        // [END mlkit_create_io_options]
        return inputOutputOptions
    }

    private fun bitmapToInputArray(): Array<Array<Array<FloatArray>>> {
        // [START mlkit_bitmap_input]
        val bitmap = Bitmap.createScaledBitmap(yourInputImage, 224, 224, true)

        val batchNum = 0
        val input = Array(1) { Array(224) { Array(224) { FloatArray(3) } } }
        for (x in 0..223) {
            for (y in 0..223) {
                val pixel = bitmap.getPixel(x, y)
                // Normalize channel values to [-1.0, 1.0]. This requirement varies by
                // model. For example, some models might require values to be normalized
                // to the range [0.0, 1.0] instead.
                input[batchNum][x][y][0] = (Color.red(pixel) - 127) / 255.0f
                input[batchNum][x][y][1] = (Color.green(pixel) - 127) / 255.0f
                input[batchNum][x][y][2] = (Color.blue(pixel) - 127) / 255.0f
            }
        }
        // [END mlkit_bitmap_input]
        return input
    }

    @Throws(FirebaseMLException::class)
    private fun runInference() {
        val localModel = FirebaseCustomLocalModel.Builder().build()
        val firebaseInterpreter = createInterpreter(localModel)!!
        val input = bitmapToInputArray()
        val inputOutputOptions = createInputOutputOptions()

        // [START mlkit_run_inference]
        val inputs = FirebaseModelInputs.Builder()
                .add(input) // add() as many input arrays as your model requires
                .build()
        firebaseInterpreter.run(inputs, inputOutputOptions)
                .addOnSuccessListener { result ->
                    // [START_EXCLUDE]
                    // [START mlkit_read_result]
                    val output = result.getOutput<Array<FloatArray>>(0)
                    val probabilities = output[0]
                    // [END mlkit_read_result]
                    // [END_EXCLUDE]
                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    // ...
                }
        // [END mlkit_run_inference]
    }

    @Throws(IOException::class)
    private fun useInferenceResult(probabilities: FloatArray) {
        // [START mlkit_use_inference_result]
        val reader = BufferedReader(
                InputStreamReader(assets.open("retrained_labels.txt")))
        for (i in probabilities.indices) {
            val label = reader.readLine()
            Log.i("MLKit", String.format("%s: %1.4f", label, probabilities[i]))
        }
        // [END mlkit_use_inference_result]
    }
}
