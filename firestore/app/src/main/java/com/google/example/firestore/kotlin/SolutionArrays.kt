package com.google.example.firestore.kotlin

import com.google.firebase.firestore.FirebaseFirestore

/**
 * https://firebase.google.com/docs/firestore/solutions/arrays
 */
abstract class SolutionArrays(private val db: FirebaseFirestore) {

    // [START array_post_class]
    data class ArrayPost(var title: String, var categories: List<String>)
    // [END array_post_class]

    // [START map_post_class]
    data class MapPost(var title: String, var categories: Map<String, Boolean>)
    // [END map_post_class]

    // [START map_post_class_advanced]
    data class MapPostAdvanced(var title: String, var categories: Map<String, Long>)
    // [END map_post_class_advanced]

    fun examplePosts() {
        // [START example_array_post]
        val myArrayPost = ArrayPost("My great post", arrayListOf(
                "technology", "opinion", "cats"
        ))
        // [END example_array_post]

        // [START example_map_post]
        val categories = hashMapOf(
                "technology" to true,
                "opinion" to true,
                "cats" to true
        )
        val myMapPost = MapPost("My great post", categories)
        // [END example_map_post]
    }

    fun examplePosts_Advanced() {
        // [START example_map_post_advanced]
        val categories = hashMapOf(
                "technology" to 1502144665L,
                "opinion" to 1502144665L,
                "cats" to 1502144665L
        )

        val myMapPostAdvanced = MapPostAdvanced("My great post", categories)
        // [END example_map_post_advanced]
    }

    fun queryForCats() {
        // [START query_for_cats]
        db.collection("posts")
                .whereEqualTo("categories.cats", true)
                .get()
                .addOnCompleteListener { }
        // [END query_for_cats]
    }

    fun queryForCatsTimestamp() {
        // [START query_for_cats_timestamp_invalid]
        db.collection("posts")
                .whereEqualTo("categories.cats", true)
                .orderBy("timestamp")
        // [END query_for_cats_timestamp_invalid]

        // [START query_for_cats_timestamp]
        db.collection("posts")
                .whereGreaterThan("categories.cats", 0)
                .orderBy("categories.cats")
        // [END query_for_cats_timestamp]
    }
}
