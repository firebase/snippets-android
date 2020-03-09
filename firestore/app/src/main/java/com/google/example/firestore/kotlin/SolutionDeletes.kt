package com.google.example.firestore.kotlin

import com.google.firebase.functions.FirebaseFunctions

class SolutionDeletes {

    // [START call_delete_function]
    /**
     * Call the 'recursiveDelete' callable function with a path to initiate
     * a server-side delete.
     */
    fun deleteAtPath(path: String) {
        val deleteFn = FirebaseFunctions.getInstance().getHttpsCallable("recursiveDelete")
        deleteFn.call(hashMapOf("path" to path))
                .addOnSuccessListener {
                    // Delete Success
                    // ...
                }
                .addOnFailureListener {
                    // Delete Failed
                    // ...
                }
    }
    // [END call_delete_function]
}
