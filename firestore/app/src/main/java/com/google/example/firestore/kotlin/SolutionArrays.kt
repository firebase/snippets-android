package com.google.example.firestore.kotlin

import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

/**
 * https://firebase.google.com/docs/firestore/solutions/arrays
 */
abstract class SolutionArrays(val db: FirebaseFirestore) {

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
        val myArrayPost = ArrayPost("My great post", Arrays.asList(
                "technology", "opinion", "cats"
        ))
        // [END example_array_post]

        // [START example_map_post]
        val categories = HashMap<String, Boolean>()
        categories["technology"] = true
        categories["opinion"] = true
        categories["cats"] = true
        val myMapPost = MapPost("My great post", categories)
        // [END example_map_post]
    }

    fun examplePosts_Advanced() {
        // [START example_map_post_advanced]
        val categories = HashMap<String, Long>()
        categories["technology"] = 1502144665L
        categories["opinion"] = 1502144665L
        categories["cats"] = 1502144665L

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
