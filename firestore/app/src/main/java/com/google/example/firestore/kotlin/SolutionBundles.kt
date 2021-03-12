package com.google.example.firestore.kotlin

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


/**
 * https://firebase.google.com/docs/firestore/solutions/serve-bundles
 */
abstract class SolutionBundles(private val db: FirebaseFirestore) {

    // [START fs_bundle_load]
    @Throws(IOException::class)
    fun getBundleStream(urlString: String?): InputStream {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        return connection.inputStream
    }

    @Throws(IOException::class)
    fun fetchFromBundle() {
        val bundleStream = getBundleStream("https://example.com/createBundle")
        val loadTask = db.loadBundle(bundleStream)

        // Chain the following tasks
        // 1) Load the bundle
        // 2) Get the named query from the local cache
        // 3) Execute a get() on the named query
        loadTask.continueWithTask<Query> { task ->
            // Close the stream
            bundleStream.close()

            // Calling .result propagates errors
            val progress = task.getResult(Exception::class.java)

            // Get the named query from the bundle cache
            db.getNamedQuery("latest-stories-query")
        }.continueWithTask { task ->
            val query = task.getResult(Exception::class.java)!!

            // get() the query results from the cache
            query.get(Source.CACHE)
        }.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Bundle loading failed", task.exception)
                return@addOnCompleteListener
            }

            // Get the QuerySnapshot from the bundle
            val storiesSnap = task.result

            // Use the results
            // ...
        }
    }
    // [END fs_bundle_load]

    companion object {
        private const val TAG = "SolutionBundles"
    }
}
