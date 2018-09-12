package com.google.firebase.referencecode.storage.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.google.firebase.referencecode.storage.GlideApp
import com.google.firebase.referencecode.storage.R
import com.google.firebase.referencecode.storage.interfaces.FirebaseUIActivityInterface
import com.google.firebase.storage.FirebaseStorage

class FirebaseUIActivity : AppCompatActivity(), FirebaseUIActivityInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_ui)
    }

    override fun loadWithGlide() {
        // [START storage_load_with_glide]
        // Reference to an image file in Cloud Storage
        val storageReference = FirebaseStorage.getInstance().reference

        // ImageView in your Activity
        val imageView = findViewById<ImageView>(R.id.imageView)

        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        GlideApp.with(this /* context */)
                .load(storageReference)
                .into(imageView)
        // [END storage_load_with_glide]
    }
}
