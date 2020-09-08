package com.google.firebase.referencecode.storage.kotlin

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.referencecode.storage.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import com.google.firebase.storage.ktx.component3
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import kotlinx.android.synthetic.main.activity_storage.imageView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

abstract class StorageActivity : AppCompatActivity() {

    // [START storage_field_declaration]
    lateinit var storage: FirebaseStorage
    // [END storage_field_declaration]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage)

        // [START storage_field_initialization]
        storage = Firebase.storage
        // [END storage_field_initialization]

        includesForCreateReference()
    }

    private fun includesForCreateReference() {
        val storage = Firebase.storage

        // ## Create a Reference

        // [START create_storage_reference]
        // Create a storage reference from our app
        var storageRef = storage.reference
        // [END create_storage_reference]

        // [START create_child_reference]
        // Create a child reference
        // imagesRef now points to "images"
        var imagesRef: StorageReference? = storageRef.child("images")

        // Child references can also take paths
        // spaceRef now points to "images/space.jpg
        // imagesRef still points to "images"
        var spaceRef = storageRef.child("images/space.jpg")
        // [END create_child_reference]

        // ## Navigate with References

        // [START navigate_references]
        // parent allows us to move our reference to a parent node
        // imagesRef now points to 'images'
        imagesRef = spaceRef.parent

        // root allows us to move all the way back to the top of our bucket
        // rootRef now points to the root
        val rootRef = spaceRef.root
        // [END navigate_references]

        // [START chain_navigation]
        // References can be chained together multiple times
        // earthRef points to 'images/earth.jpg'
        val earthRef = spaceRef.parent?.child("earth.jpg")

        // nullRef is null, since the parent of root is null
        val nullRef = spaceRef.root.parent
        // [END chain_navigation]

        // ## Reference Properties

        // [START reference_properties]
        // Reference's path is: "images/space.jpg"
        // This is analogous to a file path on disk
        spaceRef.path

        // Reference's name is the last segment of the full path: "space.jpg"
        // This is analogous to the file name
        spaceRef.name

        // Reference's bucket is the name of the storage bucket that the files are stored in
        spaceRef.bucket
        // [END reference_properties]

        // ## Full Example

        // [START reference_full_example]
        // Points to the root reference
        storageRef = storage.reference

        // Points to "images"
        imagesRef = storageRef.child("images")

        // Points to "images/space.jpg"
        // Note that you can use variables to create child values
        val fileName = "space.jpg"
        spaceRef = imagesRef.child(fileName)

        // File path is "images/space.jpg"
        val path = spaceRef.path

        // File name is "space.jpg"
        val name = spaceRef.name

        // Points to "images"
        imagesRef = spaceRef.parent
        // [END reference_full_example]
    }

    fun includesForUploadFiles() {
        val storage = Firebase.storage

        // [START upload_create_reference]
        // Create a storage reference from our app
        val storageRef = storage.reference

        // Create a reference to "mountains.jpg"
        val mountainsRef = storageRef.child("mountains.jpg")

        // Create a reference to 'images/mountains.jpg'
        val mountainImagesRef = storageRef.child("images/mountains.jpg")

        // While the file names are the same, the references point to different files
        mountainsRef.name == mountainImagesRef.name // true
        mountainsRef.path == mountainImagesRef.path // false
        // [END upload_create_reference]

        // [START upload_memory]
        // Get the data from an ImageView as bytes
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
        // [END upload_memory]

        // [START upload_stream]
        val stream = FileInputStream(File("path/to/images/rivers.jpg"))

        uploadTask = mountainsRef.putStream(stream)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
        // [END upload_stream]

        // [START upload_file]
        var file = Uri.fromFile(File("path/to/images/rivers.jpg"))
        val riversRef = storageRef.child("images/${file.lastPathSegment}")
        uploadTask = riversRef.putFile(file)

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
        // [END upload_file]

        // [START upload_with_metadata]
        // Create file metadata including the content type
        var metadata = storageMetadata {
            contentType = "image/jpg"
        }

        // Upload the file and metadata
        uploadTask = storageRef.child("images/mountains.jpg").putFile(file, metadata)
        // [END upload_with_metadata]

        // [START manage_uploads]
        uploadTask = storageRef.child("images/mountains.jpg").putFile(file)

        // Pause the upload
        uploadTask.pause()

        // Resume the upload
        uploadTask.resume()

        // Cancel the upload
        uploadTask.cancel()
        // [END manage_uploads]

        // [START monitor_upload_progress]
        // Observe state change events such as progress, pause, and resume
        // You'll need to import com.google.firebase.storage.ktx.component1 and 
        // com.google.firebase.storage.ktx.component2
        uploadTask.addOnProgressListener { (bytesTransferred, totalByteCount) ->
            val progress = (100.0 * bytesTransferred) / totalByteCount
            Log.d(TAG, "Upload is $progress% done")
        }.addOnPausedListener {
            Log.d(TAG, "Upload is paused")
        }
        // [END monitor_upload_progress]

        // [START upload_complete_example]
        // File or Blob
        file = Uri.fromFile(File("path/to/mountains.jpg"))

        // Create the file metadata
        metadata = storageMetadata {
            contentType = "image/jpeg"
        }

        // Upload file and metadata to the path 'images/mountains.jpg'
        uploadTask = storageRef.child("images/${file.lastPathSegment}").putFile(file, metadata)

        // Listen for state changes, errors, and completion of the upload.
        // You'll need to import com.google.firebase.storage.ktx.component1 and 
        // com.google.firebase.storage.ktx.component2
        uploadTask.addOnProgressListener { (bytesTransferred, totalByteCount) ->
            val progress = (100.0 * bytesTransferred) / totalByteCount
            Log.d(TAG, "Upload is $progress% done")
        }.addOnPausedListener {
            Log.d(TAG, "Upload is paused")
        }.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener {
            // Handle successful uploads on complete
            // ...
        }
        // [END upload_complete_example]

        // [START upload_get_download_url]
        val ref = storageRef.child("images/mountains.jpg")
        uploadTask = ref.putFile(file)

        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
            } else {
                // Handle failures
                // ...
            }
        }
        // [END upload_get_download_url]
    }

    fun includesForDownloadFiles() {
        val storage = Firebase.storage

        // [START download_create_reference]
        // Create a storage reference from our app
        val storageRef = storage.reference

        // Create a reference with an initial file path and name
        val pathReference = storageRef.child("images/stars.jpg")

        // Create a reference to a file from a Google Cloud Storage URI
        val gsReference = storage.getReferenceFromUrl("gs://bucket/images/stars.jpg")

        // Create a reference from an HTTPS URL
        // Note that in the URL, characters are URL escaped!
        val httpsReference = storage.getReferenceFromUrl(
                "https://firebasestorage.googleapis.com/b/bucket/o/images%20stars.jpg")
        // [END download_create_reference]

        // [START download_to_memory]
        var islandRef = storageRef.child("images/island.jpg")

        val ONE_MEGABYTE: Long = 1024 * 1024
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            // Data for "images/island.jpg" is returned, use this as needed
        }.addOnFailureListener {
            // Handle any errors
        }
        // [END download_to_memory]

        // [START download_to_local_file]
        islandRef = storageRef.child("images/island.jpg")

        val localFile = File.createTempFile("images", "jpg")

        islandRef.getFile(localFile).addOnSuccessListener {
            // Local temp file has been created
        }.addOnFailureListener {
            // Handle any errors
        }
        // [END download_to_local_file]

        // [START download_via_url]
        storageRef.child("users/me/profile.png").downloadUrl.addOnSuccessListener {
            // Got the download URL for 'users/me/profile.png'
        }.addOnFailureListener {
            // Handle any errors
        }
        // [END download_via_url]

        // [START download_full_example]
        storageRef.child("users/me/profile.png").getBytes(Long.MAX_VALUE).addOnSuccessListener {
            // Use the bytes to display the image
        }.addOnFailureListener {
            // Handle any errors
        }
        // [END download_full_example]
    }

    fun includesForFileMetadata() {
        val storage = Firebase.storage

        // [START metadata_get_storage_reference]
        // Create a storage reference from our app
        val storageRef = storage.reference

        // Get reference to the file
        val forestRef = storageRef.child("images/forest.jpg")
        // [END metadata_get_storage_reference]

        // [START get_file_metadata]
        forestRef.metadata.addOnSuccessListener { metadata ->
            // Metadata now contains the metadata for 'images/forest.jpg'
        }.addOnFailureListener {
            // Uh-oh, an error occurred!
        }
        // [END get_file_metadata]

        // [START update_file_metadata]
        // Create file metadata including the content type
        val metadata = storageMetadata {
            contentType = "image/jpg"
            setCustomMetadata("myCustomProperty", "myValue")
        }

        // Update metadata properties
        forestRef.updateMetadata(metadata).addOnSuccessListener { updatedMetadata ->
            // Updated metadata is in updatedMetadata
        }.addOnFailureListener {
            // Uh-oh, an error occurred!
        }
        // [END update_file_metadata]
    }

    fun includesForMetadata_delete() {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val forestRef = storageRef.child("images/forest.jpg")

        // [START delete_file_metadata]
        // Create file metadata with property to delete
        val metadata = storageMetadata {
            contentType = null
        }

        // Delete the metadata property
        forestRef.updateMetadata(metadata).addOnSuccessListener { updatedMetadata ->
            // updatedMetadata.contentType should be null
        }.addOnFailureListener {
            // Uh-oh, an error occurred!
        }
        // [END delete_file_metadata]
    }

    fun includesForMetadata_custom() {
        // [START custom_metadata]
        val metadata = storageMetadata {
            setCustomMetadata("location", "Yosemite, CA, USA")
            setCustomMetadata("activity", "Hiking")
        }
        // [END custom_metadata]
    }

    fun includesForDeleteFiles() {
        val storage = Firebase.storage

        // [START delete_file]
        // Create a storage reference from our app
        val storageRef = storage.reference

        // Create a reference to the file to delete
        val desertRef = storageRef.child("images/desert.jpg")

        // Delete the file
        desertRef.delete().addOnSuccessListener {
            // File deleted successfully
        }.addOnFailureListener {
            // Uh-oh, an error occurred!
        }
        // [END delete_file]
    }

    fun nonDefaultBucket() {
        // [START storage_non_default_bucket]
        // Get a non-default Storage bucket
        val storage = Firebase.storage("gs://my-custom-bucket")
        // [END storage_non_default_bucket]
    }

    fun customApp() {
        val customApp = Firebase.initialize(this)

        // [START storage_custom_app]
        // Get the default bucket from a custom FirebaseApp
        val storage = Firebase.storage(customApp!!)

        // Get a non-default bucket from a custom FirebaseApp
        val customStorage = Firebase.storage(customApp, "gs://my-custom-bucket")
        // [END storage_custom_app]
    }

    fun listAllFiles() {
        // [START storage_list_all]
        val storage = Firebase.storage
        val listRef = storage.reference.child("files/uid")
                
        // You'll need to import com.google.firebase.storage.ktx.component1 and 
        // com.google.firebase.storage.ktx.component2
        listRef.listAll()
                .addOnSuccessListener { (items, prefixes) ->
                    prefixes.forEach { prefix ->
                        // All the prefixes under listRef.
                        // You may call listAll() recursively on them.
                    }

                    items.forEach { item ->
                        // All the items under listRef.
                    }
                }
                .addOnFailureListener {
                    // Uh-oh, an error occurred!
                }
        // [END storage_list_all]
    }

    private fun processResults(items: List<StorageReference>, prefixes: List<StorageReference>) {

    }

    // [START storage_list_paginated]
    fun listAllPaginated(pageToken: String?) {
        val storage = Firebase.storage
        val listRef = storage.reference.child("files/uid")

        // Fetch the next page of results, using the pageToken if we have one.
        val listPageTask = if (pageToken != null) {
            listRef.list(100, pageToken)
        } else {
            listRef.list(100)
        }

        // You'll need to import com.google.firebase.storage.ktx.component1 and 
        // com.google.firebase.storage.ktx.component2
        listPageTask
                .addOnSuccessListener { (items, prefixes, pageToken) ->
                    // Process page of results
                    processResults(items, prefixes)

                    // Recurse onto next page
                    pageToken?.let {
                        listAllPaginated(it)
                    }
                }.addOnFailureListener {
                    // Uh-oh, an error occurred.
                }
    }
    // [END storage_list_paginated]

    // [START storage_custom_failure_listener]
    internal inner class MyFailureListener : OnFailureListener {
        override fun onFailure(exception: Exception) {
            val errorCode = (exception as StorageException).errorCode
            val errorMessage = exception.message
            // test the errorCode and errorMessage, and handle accordingly
        }
    }
    // [END storage_custom_failure_listener]

    companion object {
        const val TAG = "kotlin.StorageActivity"
    }
}
