package com.google.firebase.referencecode.database.kotlin

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.MutableData
import com.google.firebase.database.ServerValue
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.google.firebase.Firebase
import com.google.firebase.referencecode.database.kotlin.models.Post
import com.google.firebase.referencecode.database.models.User

abstract class ReadAndWriteSnippets {

    private val TAG = "ReadAndWriteSnippets"

    // [START declare_database_ref]
    private lateinit var database: DatabaseReference
    // [END declare_database_ref]

    fun initializeDbRef() {
        // [START initialize_database_ref]
        database = Firebase.database.reference
        // [END initialize_database_ref]
    }

    // [START rtdb_write_new_user]
    fun writeNewUser(userId: String, name: String, email: String) {
        val user = User(name, email)

        database.child("users").child(userId).setValue(user)
    }
    // [END rtdb_write_new_user]

    fun writeNewUserWithTaskListeners(userId: String, name: String, email: String) {
        val user = User(name, email)

        // [START rtdb_write_new_user_task]
        database.child("users").child(userId).setValue(user)
            .addOnSuccessListener {
                // Write was successful!
                // ...
            }
            .addOnFailureListener {
                // Write failed
                // ...
            }
        // [END rtdb_write_new_user_task]
    }

    private fun addPostEventListener(postReference: DatabaseReference) {
        // [START post_value_event_listener]
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.getValue<Post>()
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        postReference.addValueEventListener(postListener)
        // [END post_value_event_listener]
    }

    // [START write_fan_out]
    private fun writeNewPost(userId: String, username: String, title: String, body: String) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        val key = database.child("posts").push().key
        if (key == null) {
            Log.w(TAG, "Couldn't get push key for posts")
            return
        }

        val post = Post(userId, username, title, body)
        val postValues = post.toMap()

        val childUpdates = hashMapOf<String, Any>(
            "/posts/$key" to postValues,
            "/user-posts/$userId/$key" to postValues,
        )

        database.updateChildren(childUpdates)
    }
    // [END write_fan_out]

    // [START post_stars_transaction]
    private fun onStarClicked(postRef: DatabaseReference) {
        // [START_EXCLUDE]
        val uid = ""
        // [END_EXCLUDE]
        postRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                val p = mutableData.getValue(Post::class.java)
                    ?: return Transaction.success(mutableData)

                if (p.stars.containsKey(uid)) {
                    // Unstar the post and remove self from stars
                    p.starCount = p.starCount - 1
                    p.stars.remove(uid)
                } else {
                    // Star the post and add self to stars
                    p.starCount = p.starCount + 1
                    p.stars[uid] = true
                }

                // Set value and report transaction success
                mutableData.value = p
                return Transaction.success(mutableData)
            }

            override fun onComplete(
                databaseError: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?,
            ) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError!!)
            }
        })
    }
    // [END post_stars_transaction]

    // [START post_stars_increment]
    private fun onStarClicked(uid: String, key: String) {
        val updates: MutableMap<String, Any> = hashMapOf(
            "posts/$key/stars/$uid" to true,
            "posts/$key/starCount" to ServerValue.increment(1),
            "user-posts/$uid/$key/stars/$uid" to true,
            "user-posts/$uid/$key/starCount" to ServerValue.increment(1),
        )
        database.updateChildren(updates)
    }
    // [END post_stars_increment]
}
