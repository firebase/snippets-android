package com.google.firebase.example.vertexai.kotlin

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.example.vertexai.R
import com.google.firebase.vertexai.GenerativeModel
import com.google.firebase.vertexai.type.content
import com.google.firebase.vertexai.vertexAI
import kotlinx.coroutines.launch

@Suppress("JoinDeclarationAndAssignment") // for the generativeModel var
class GenerateContentViewModel : ViewModel() {
    private val TAG = "ContentViewModel"
    private var generativeModel: GenerativeModel

    // Only meant to separate the scope of the initialization snippet
    // so that it doesn't cause a naming clash with the top level generativeModel
    fun initialize() {
        // [START vertexai_init]
        val generativeModel = Firebase.vertexAI.generativeModel(
            // Specify a model that supports your use case
            // Gemini 1.5 Pro is versatile and can accept both text-only and multimodal prompt inputs
            modelName = "gemini-1.5-pro"
        )
        // [END vertexai_init]
    }

    init {
        generativeModel = Firebase.vertexAI.generativeModel("gemini-1.5-pro")
    }

    fun generateContentStream() {
        viewModelScope.launch {
            // [START vertexai_textonly_stream]
            // Provide a prompt that includes only text
            val prompt = "Write a story about a magic backpack."
            // To stream generated text output, call generateContentStream and pass in the prompt
            var fullResponse = ""
            generativeModel.generateContentStream(prompt).collect { chunk ->
                Log.d(TAG, chunk.text ?: "")
                fullResponse += chunk.text
            }
            // [END vertexai_textonly_stream]
        }
    }

    fun generateContent() {
        viewModelScope.launch {
            // [START vertexai_textonly]
            // Provide a prompt that includes only text
            val prompt = "Write a story about a magic backpack."

            // To generate text output, call generateContent and pass in the prompt
            val response = generativeModel.generateContent(prompt)
            Log.d(TAG, response.text ?: "")
            // [END vertexai_textonly]
        }
    }

    fun generateContentWithImageStream(resources: Resources) {
        viewModelScope.launch {
            // [START vertexai_text_and_image_stream]
            // Loads an image from the app/res/drawable/ directory
            val bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.sparky)

            val prompt = content {
                image(bitmap)
                text("What developer tool is this mascot from?")
            }

            var fullResponse = ""
            generativeModel.generateContentStream(prompt).collect { chunk ->
                Log.d(TAG, chunk.text ?: "")
                fullResponse += chunk.text
            }
            // [END vertexai_text_and_image_stream]
        }
    }

    fun generateContentWithImage(resources: Resources) {
        viewModelScope.launch {
            // [START vertexai_text_and_image]
            // Loads an image from the app/res/drawable/ directory
            val bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.sparky)

            val prompt = content {
                image(bitmap)
                text("What developer tool is this mascot from?")
            }

            val response = generativeModel.generateContent(prompt)
            Log.d(TAG, response.text ?: "")
            // [END vertexai_text_and_image]
        }
    }

    fun generateContentWithMultipleImagesStream(resources: Resources) {
        viewModelScope.launch {
            // [START vertexai_text_and_images_stream]
            // Loads an image from the app/res/drawable/ directory
            val bitmap1: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.sparky)
            val bitmap2: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.sparky_eats_pizza)

            val prompt = content {
                image(bitmap1)
                image(bitmap2)
                text("What is different between these pictures?")
            }

            var fullResponse = ""
            generativeModel.generateContentStream(prompt).collect { chunk ->
                Log.d(TAG, chunk.text ?: "")
                fullResponse += chunk.text
            }
            // [END vertexai_text_and_images_stream]
        }
    }

    fun generateContentWithMultipleImages(resources: Resources) {
        viewModelScope.launch {
            // [START vertexai_text_and_images]
            // Loads an image from the app/res/drawable/ directory
            val bitmap1: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.sparky)
            val bitmap2: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.sparky_eats_pizza)

            val prompt = content {
                image(bitmap1)
                image(bitmap2)
                text("What is different between these pictures?")
            }

            val response = generativeModel.generateContent(prompt)
            Log.d(TAG, response.text ?: "")
            // [END vertexai_text_and_images]
        }
    }

    fun generateContentWithVideoStream(
        applicationContext: Context,
        videoUri: Uri
    ) {
        viewModelScope.launch {
            // [START vertexai_text_and_video_stream]
            val contentResolver = applicationContext.contentResolver
            contentResolver.openInputStream(videoUri).use { stream ->
                stream?.let {
                    val bytes = stream.readBytes()

                    val prompt = content {
                        blob("video/mp4", bytes)
                        text("What is in the video?")
                    }

                    var fullResponse = ""
                    generativeModel.generateContentStream(prompt).collect { chunk ->
                        Log.d(TAG, chunk.text ?: "")
                        fullResponse += chunk.text
                    }
                }
            }
            // [END vertexai_text_and_video_stream]
        }
    }

    fun generateContentWithVideo(
        applicationContext: Context,
        videoUri: Uri
    ) {
        viewModelScope.launch {
            // [START vertexai_text_and_video]
            val contentResolver = applicationContext.contentResolver
            contentResolver.openInputStream(videoUri).use { stream ->
                stream?.let {
                    val bytes = stream.readBytes()

                    val prompt = content {
                        blob("video/mp4", bytes)
                        text("What is in the video?")
                    }

                    val response = generativeModel.generateContent(prompt)
                    Log.d(TAG, response.text ?: "")
                }
            }
            // [END vertexai_text_and_video]
        }
    }

    fun countTokensText() {
        viewModelScope.launch {
            // [START vertexai_count_tokens_text]
            val (tokens, billableChars) = generativeModel.countTokens("Write a story about a magic backpack.")
            // [END vertexai_count_tokens_text]
        }
    }

    fun countTokensMultimodal(bitmap: Bitmap) {
        viewModelScope.launch {
            // [START vertexai_count_tokens_multimodal]
            val prompt = content {
                image(bitmap)
                text("Where can I buy this?")
            }
            val (tokens, billableChars) = generativeModel.countTokens(prompt)
            // [END vertexai_count_tokens_multimodal]
        }
    }
}