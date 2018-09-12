package com.google.firebase.referencecode.database.kotlin

import com.google.firebase.database.DatabaseReference
import com.google.firebase.referencecode.database.interfaces.ReadAndWriteSnippetsInterface
import com.google.firebase.referencecode.database.models.User

class ReadAndWriteSnippets(private val mDatabase: DatabaseReference) : ReadAndWriteSnippetsInterface {

    // [START rtdb_write_new_user]
    override fun writeNewUser(userId: String, name: String, email: String) {
        val user = User(name, email)

        mDatabase.child("users").child(userId).setValue(user)
    }
    // [END rtdb_write_new_user]

    override fun writeNewUserWithTaskListeners(userId: String, name: String, email: String) {
        val user = User(name, email)

        // [START rtdb_write_new_user_task]
        mDatabase.child("users").child(userId).setValue(user)
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
