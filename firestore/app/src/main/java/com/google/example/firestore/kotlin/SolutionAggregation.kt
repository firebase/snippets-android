package com.google.example.firestore.kotlin

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject

/**
 * https://firebase.google.com/docs/firestore/solutions/aggregation
 */
abstract class SolutionAggregation(private val db: FirebaseFirestore) {

    // [START restaurant_class]
    data class Restaurant(
        // default values required for use with "toObject"
        internal var name: String = "",
        internal var avgRating: Double = 0.0,
        internal var numRatings: Int = 0
    )
    // [END restaurant_class]

    fun exampleRestaurant() {
        // [START example_restaurant]
        val arinell = Restaurant("Arinell Pizza", 4.65, 683)
        // [END example_restaurant]
    }

    fun getAllRatings() {
        // [START get_all_ratings]
        db.collection("restaurants")
                .document("arinell-pizza")
                .collection("ratings")
                .get()
        // [END get_all_ratings]
    }

    // [START add_rating]
    private fun addRating(restaurantRef: DocumentReference, rating: Float): Task<Void> {
        // Create reference for new rating, for use inside the transaction
        val ratingRef = restaurantRef.collection("ratings").document()

        // In a transaction, add the new rating and update the aggregate totals
        return db.runTransaction { transaction ->
            val restaurant = transaction.get(restaurantRef).toObject<Restaurant>()!!

            // Compute new number of ratings
            val newNumRatings = restaurant.numRatings + 1

            // Compute new average rating
            val oldRatingTotal = restaurant.avgRating * restaurant.numRatings
            val newAvgRating = (oldRatingTotal + rating) / newNumRatings

            // Set new restaurant info
            restaurant.numRatings = newNumRatings
            restaurant.avgRating = newAvgRating

            // Update restaurant
            transaction.set(restaurantRef, restaurant)

            // Update rating
            val data = hashMapOf<String, Any>(
                    "rating" to rating
            )
            transaction.set(ratingRef, data, SetOptions.merge())

            null
        }
    }
    // [END add_rating]
}
