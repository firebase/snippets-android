package com.google.firebase.example.vertexai.kotlin

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.storage
import com.google.firebase.vertexai.type.GoogleGenerativeAIException
import com.google.firebase.vertexai.type.content
import com.google.firebase.vertexai.vertexAI
import java.io.File
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FilesViewModel : ViewModel() {
    private val TAG = "FilesViewModel"
    private val generativeModel =
        Firebase.vertexAI.generativeModel("gemini-1.5-pro-preview-0409")

    fun uploadAndUseReference() {
        viewModelScope.launch {
            // [START vertexai_storage_upload]
            // Upload raw data to storage reference
            val storageRef = Firebase.storage.reference
            val fileUri = Uri.fromFile(File("images/image.jpg"))
            try {
                val taskSnapshot = storageRef.putFile(fileUri).await()

                // Get the MIME type and Cloud Storage URL
                val mimeType = taskSnapshot.metadata?.contentType
                val bucket = taskSnapshot.metadata?.bucket
                val filePath = taskSnapshot.metadata?.path

                if (mimeType != null && bucket != null) {
                    val storageUrl = "gs://$bucket/$filePath"
                    val prompt = content {
                        fileData(mimeType, storageUrl)
                        text("What's in this picture?")
                    }
                    val response = generativeModel.generateContent(prompt)
                    Log.d(TAG, response.text)
                }
            } catch (e: StorageException) {
                // An error occurred while uploading the file
            } catch (e: GoogleGenerativeAIException) {
                // An error occurred while generating text
            }
            // [END vertexai_storage_upload]
        }
    }

    fun useObjectReference() {
        viewModelScope.launch {
            // [START vertexai_storage_ref]
            val prompt = content {
                fileData("image/jpeg", "gs://bucket-name/path/image.jpg")
                text("What's in this picture?")
            }
            val response = generativeModel.generateContent(prompt)
            Log.d(TAG, response.text)
            // [END vertexai_storage_ref]
        }
    }
}