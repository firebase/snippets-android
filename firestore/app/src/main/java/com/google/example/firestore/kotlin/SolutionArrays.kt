package com.google.example.firestore.kotlin

import com.google.example.firestore.interfaces.SolutionArraysInterface
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

/**
 * https://firebase.google.com/docs/firestore/solutions/arrays
 */
class SolutionArrays : SolutionArraysInterface {

    private val db: FirebaseFirestore? = null

    // [START array_post_class]
    inner class ArrayPost(internal var title: String, internal var categories: List<String>)
    // [END array_post_class]


    // [START map_post_class]
    inner class MapPost(internal var title: String, internal var categories: Map<String, Boolean>)
    // [END map_post_class]

    // [START map_post_class_advanced]
    inner class MapPostAdvanced(internal var title: String, internal var categories: Map<String, Long>)
    // [END map_post_class_advanced]


    override fun examplePosts() {
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

    override fun examplePosts_Advanced() {
        // [START example_map_post_advanced]
        val categories = HashMap<String, Long>()
        categories["technology"] = 1502144665L
        categories["opinion"] = 1502144665L
        categories["cats"] = 1502144665L
        val myMapPostAdvanced = MapPostAdvanced("My great post", categories)
        // [END example_map_post_advanced]
    }

    override fun queryForCats() {
        // [START query_for_cats]
        db!!.collection("posts")
                .whereEqualTo("categories.cats", true)
                .get()
                .addOnCompleteListener { }
        // [END query_for_cats]
    }

    override fun queryForCatsTimestamp() {
        // [START query_for_cats_timestamp_invalid]
        db!!.collection("posts")
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
