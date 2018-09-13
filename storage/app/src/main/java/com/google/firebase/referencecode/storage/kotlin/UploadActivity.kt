package com.google.firebase.referencecode.storage.kotlin

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.referencecode.storage.R
import com.google.firebase.referencecode.storage.interfaces.UploadActivityInterface
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class UploadActivity : AppCompatActivity(), UploadActivityInterface {

    private var mStorageRef: StorageReference? = null  //mStorageRef was previously used to transfer data.
    private var mSaved: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
    }

    // [START storage_upload_lifecycle]
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // If there's an upload in progress, save the reference so you can query it later
        if (mStorageRef != null) {
            outState.putString("reference", mStorageRef!!.toString())
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // If there was an upload in progress, get its reference and create a new StorageReference
        val stringRef = savedInstanceState.getString("reference") ?: return
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(stringRef)

        // Find all UploadTasks under this StorageReference (in this example, there should be one)
        val tasks = mStorageRef!!.activeUploadTasks
        if (tasks.size > 0) {
            // Get the task monitoring the upload
            val task = tasks[0]

            // Add new listeners to the task using an Activity scope
            task.addOnSuccessListener(this) {
                // Success!
                // ...
            }
        }
    }
    // [END storage_upload_lifecycle]

    override fun continueAcrossRestarts() {
        val localFile: Uri? = null
        var sessionUri: Uri? = null
        var uploadTask: UploadTask

        // [START save_before_restart]
        uploadTask = mStorageRef!!.putFile(localFile!!)
        uploadTask.addOnProgressListener { taskSnapshot ->
            sessionUri = taskSnapshot.uploadSessionUri
            if (sessionUri != null && !mSaved) {
                mSaved = true
                // A persisted session has begun with the server.
                // Save this to persistent storage in case the process dies.
            }
        }
        // [END save_before_restart]

        // [START restore_after_restart]
        //resume the upload task from where it left off when the process died.
        //to do this, pass the sessionUri as the last parameter
        uploadTask = mStorageRef!!.putFile(localFile,
                StorageMetadata.Builder().build(), sessionUri)
        // [END restore_after_restart]
    }
}
