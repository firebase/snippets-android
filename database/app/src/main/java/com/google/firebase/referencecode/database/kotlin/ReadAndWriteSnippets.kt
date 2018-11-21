package com.google.firebase.referencecode.database.kotlin

import com.google.firebase.database.DatabaseReference
import com.google.firebase.referencecode.database.models.User

abstract class ReadAndWriteSnippets(private val database: DatabaseReference) {

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
}
