package com.google.firebase.referencecode.storage.kotlin

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.referencecode.storage.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

abstract class UploadActivity : AppCompatActivity() {

    // storageRef was previously used to transfer data.
    private var storageRef: StorageReference? = null
    private var saved: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
    }

    // [START storage_upload_lifecycle]
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // If there's an upload in progress, save the reference so you can query it later
        storageRef?.let {
            outState.putString("reference", it.toString())
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // If there was an upload in progress, get its reference and create a new StorageReference
        val stringRef = savedInstanceState.getString("reference") ?: return

        storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(stringRef)

        // Find all UploadTasks under this StorageReference (in this example, there should be one)

        storageRef?.activeUploadTasks?.let { it ->
            if (it.size > 0) {
                // Get the task monitoring the upload
                val task = it[0]

                // Add new listeners to the task using an Activity scope
                task.addOnSuccessListener(this) {
                    // Success!
                    // ...
                }
            }
        }
    }
    // [END storage_upload_lifecycle]

    fun continueAcrossRestarts() {
        val localFile: Uri = Uri.parse("file://someLocalFile")
        var sessionUri: Uri? = null
        var uploadTask: UploadTask

        // [START save_before_restart]
        uploadTask = storageRef!!.putFile(localFile)
        uploadTask.addOnProgressListener { taskSnapshot ->
            sessionUri = taskSnapshot.uploadSessionUri
            if (sessionUri != null && !saved) {
                saved = true
                // A persisted session has begun with the server.
                // Save this to persistent storage in case the process dies.
            }
        }
        // [END save_before_restart]

        // [START restore_after_restart]
        // resume the upload task from where it left off when the process died.
        // to do this, pass the sessionUri as the last parameter
        uploadTask = storageRef!!.putFile(localFile,
                StorageMetadata.Builder().build(), sessionUri)
        // [END restore_after_restart]
    }
}
