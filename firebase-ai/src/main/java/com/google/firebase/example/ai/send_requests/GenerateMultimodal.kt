package com.google.firebase.example.ai.send_requests

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.type.content
import com.google.firebase.example.ai.R

class GenerateMultimodal(
  private val applicationContext: Context,
  private val resources: Resources,
  private val model: GenerativeModel,
) {

  private val TAG = "GenerateMultimodal"

  private suspend fun audioNonStreaming(audioUri: Uri) {
    // [START multimodal_audio_non_streaming]
    val contentResolver = applicationContext.contentResolver

    val inputStream = contentResolver.openInputStream(audioUri)

    if (inputStream != null) {  // Check if the audio loaded successfully
      inputStream.use { stream ->
        val bytes = stream.readBytes()

        // Provide a prompt that includes the audio specified above and text
        val prompt = content {
          inlineData(bytes, "audio/mpeg")  // Specify the appropriate audio MIME type
          text("Transcribe what's said in this audio recording.")
        }

        // To generate text output, call `generateContent` with the prompt
        val response = model.generateContent(prompt)

        // Log the generated text, handling the case where it might be null
        Log.d(TAG, response.text ?: "")
      }
    } else {
      Log.e(TAG, "Error getting input stream for audio.")
      // Handle the error appropriately
    }
    // [END multimodal_audio_non_streaming]
  }

  private suspend fun audioStreaming(audioUri: Uri) {
    // [START multimodal_audio_streaming]
    val contentResolver = applicationContext.contentResolver

    val inputStream = contentResolver.openInputStream(audioUri)

    if (inputStream != null) {  // Check if the audio loaded successfully
      inputStream.use { stream ->
        val bytes = stream.readBytes()

        // Provide a prompt that includes the audio specified above and text
        val prompt = content {
          inlineData(bytes, "audio/mpeg")  // Specify the appropriate audio MIME type
          text("Transcribe what's said in this audio recording.")
        }

        // To stream generated text output, call `generateContentStream` with the prompt
        var fullResponse = ""
        model.generateContentStream(prompt).collect { chunk ->
          // Log the generated text, handling the case where it might be null
          Log.d(TAG, chunk.text ?: "")
          fullResponse += chunk.text ?: ""
        }
      }
    } else {
      Log.e(TAG, "Error getting input stream for audio.")
      // Handle the error appropriately
    }
    // [END multimodal_audio_streaming]
  }

  private suspend fun multiImagesNonStreaming() {
    // [START multimodal_images_non_streaming]
    // Loads an image from the app/res/drawable/ directory
    val bitmap1: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.sparky)
    val bitmap2: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.sparky_eats_pizza)

    // Provide a prompt that includes the images specified above and text
    val prompt = content {
      image(bitmap1)
      image(bitmap2)
      text("What is different between these pictures?")
    }

    // To generate text output, call generateContent with the prompt
    val response = model.generateContent(prompt)
    print(response.text)
    // [END multimodal_images_non_streaming]
  }

  private suspend fun multiImagesStreaming() {
    // [START multimodal_images_streaming]
    // Loads an image from the app/res/drawable/ directory
    val bitmap1: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.sparky)
    val bitmap2: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.sparky_eats_pizza)

    // Provide a prompt that includes the images specified above and text
    val prompt = content {
      image(bitmap1)
      image(bitmap2)
      text("What's different between these pictures?")
    }

    // To stream generated text output, call generateContentStream with the prompt
    var fullResponse = ""
    model.generateContentStream(prompt).collect { chunk ->
      print(chunk.text)
      fullResponse += chunk.text
    }
    // [END multimodal_images_streaming]
  }

  private suspend fun oneImageNonStreaming() {
    // [START multimodal_one_image_non_streaming]
    // Loads an image from the app/res/drawable/ directory
    val bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.sparky)

    // Provide a prompt that includes the image specified above and text
    val prompt = content {
      image(bitmap)
      text("What developer tool is this mascot from?")
    }

    // To generate text output, call generateContent with the prompt
    val response = model.generateContent(prompt)
    print(response.text)
    // [END multimodal_one_image_non_streaming]
  }

  private suspend fun oneImageStreaming() {
    // [START multimodal_one_image_streaming]
    // Loads an image from the app/res/drawable/ directory
    val bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.sparky)

    // Provide a prompt that includes the image specified above and text
    val prompt = content {
      image(bitmap)
      text("What developer tool is this mascot from?")
    }

    // To stream generated text output, call generateContentStream with the prompt
    var fullResponse = ""
    model.generateContentStream(prompt).collect { chunk ->
      print(chunk.text)
      fullResponse += chunk.text
    }
    // [END multimodal_one_image_streaming]
  }

  private suspend fun onePdfNonStreaming(pdfUri: Uri) {
    // [START multimodal_one_pdf_non_streaming]
    val contentResolver = applicationContext.contentResolver

    // Provide the URI for the PDF file you want to send to the model
    val inputStream = contentResolver.openInputStream(pdfUri)

    if (inputStream != null) {  // Check if the PDF file loaded successfully
      inputStream.use { stream ->
        // Provide a prompt that includes the PDF file specified above and text
        val prompt = content {
          inlineData(
            bytes = stream.readBytes(),
            mimeType = "application/pdf" // Specify the appropriate PDF file MIME type
          )
          text("Summarize the important results in this report.")
        }

        // To generate text output, call `generateContent` with the prompt
        val response = model.generateContent(prompt)

        // Log the generated text, handling the case where it might be null
        Log.d(TAG, response.text ?: "")
      }
    } else {
      Log.e(TAG, "Error getting input stream for file.")
      // Handle the error appropriately
    }
    // [END multimodal_one_pdf_non_streaming]
  }

  private suspend fun onePdfStreaming(pdfUri: Uri) {
    // [START multimodal_one_pdf_streaming]
    val contentResolver = applicationContext.contentResolver

    // Provide the URI for the PDF you want to send to the model
    val inputStream = contentResolver.openInputStream(pdfUri)

    if (inputStream != null) {  // Check if the PDF file loaded successfully
      inputStream.use { stream ->
        // Provide a prompt that includes the PDF file specified above and text
        val prompt = content {
          inlineData(
            bytes = stream.readBytes(),
            mimeType = "application/pdf" // Specify the appropriate PDF file MIME type
          )
          text("Summarize the important results in this report.")
        }

        // To stream generated text output, call `generateContentStream` with the prompt
        var fullResponse = ""
        model.generateContentStream(prompt).collect { chunk ->
          // Log the generated text, handling the case where it might be null
          val chunkText = chunk.text ?: ""
          Log.d(TAG, chunkText)
          fullResponse += chunkText
        }
      }
    } else {
      Log.e(TAG, "Error getting input stream for file.")
      // Handle the error appropriately
    }
    // [END multimodal_one_pdf_streaming]
  }

  private suspend fun videoNonStreaming(videoUri: Uri) {
    // [START multimodal_video_non_streaming]
    val contentResolver = applicationContext.contentResolver
    contentResolver.openInputStream(videoUri).use { stream ->
      stream?.let {
        val bytes = stream.readBytes()

        // Provide a prompt that includes the video specified above and text
        val prompt = content {
          inlineData(bytes, "video/mp4")
          text("What is in the video?")
        }

        // To generate text output, call generateContent with the prompt
        val response = model.generateContent(prompt)
        Log.d(TAG, response.text ?: "")
      }
    }
    // [END multimodal_video_non_streaming]
  }

  private suspend fun videoStreaming(videoUri: Uri) {
    // [START multimodal_video_streaming]
    val contentResolver = applicationContext.contentResolver
    contentResolver.openInputStream(videoUri).use { stream ->
      stream?.let {
        val bytes = stream.readBytes()

        // Provide a prompt that includes the video specified above and text
        val prompt = content {
          inlineData(bytes, "video/mp4")
          text("What is in the video?")
        }

        // To stream generated text output, call generateContentStream with the prompt
        var fullResponse = ""
        model.generateContentStream(prompt).collect { chunk ->
          Log.d(TAG, chunk.text ?: "")
          fullResponse += chunk.text
        }
      }
    }
    // [END multimodal_video_streaming]
  }


}