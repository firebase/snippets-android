package com.google.example.firestore.kotlin

import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.tasks.await

class SolutionDeletes {

    // [START call_delete_function]
    /**
     * Call the 'recursiveDelete' callable function with a path to initiate
     * a server-side delete.
     */
    suspend fun deleteAtPath(path: String) {
        val deleteFn = FirebaseFunctions.getInstance().getHttpsCallable("recursiveDelete")
        deleteFn.call(hashMapOf("path" to path)).await()
        // Delete Success
        // ...
    }
    // [END call_delete_function]
}