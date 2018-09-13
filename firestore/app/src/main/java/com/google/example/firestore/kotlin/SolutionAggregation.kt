package com.google.example.firestore.kotlin

import com.google.android.gms.tasks.Task
import com.google.example.firestore.interfaces.SolutionAggregationInterface
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.*

/**
 * https://firebase.google.com/docs/firestore/solutions/aggregation
 */
class SolutionAggregation : SolutionAggregationInterface {

    private val db: FirebaseFirestore? = null

    // [START restaurant_class]
    inner class Restaurant(internal var name: String, internal var avgRating: Double, internal var numRatings: Int)
    // [END restaurant_class]

    override fun exampleRestaurant() {
        // [START example_restaurant]
        val arinell = Restaurant("Arinell Pizza", 4.65, 683)
        // [END example_restaurant]
    }

    override fun getAllRatings() {
        // [START get_all_ratings]
        db!!.collection("restaurants")
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
        return db!!.runTransaction { transaction ->
            val restaurant = transaction.get(restaurantRef).toObject(Restaurant::class.java)

            // Compute new number of ratings
            val newNumRatings = restaurant!!.numRatings + 1

            // Compute new average rating
            val oldRatingTotal = restaurant.avgRating * restaurant.numRatings
            val newAvgRating = (oldRatingTotal + rating) / newNumRatings

            // Set new restaurant info
            restaurant.numRatings = newNumRatings
            restaurant.avgRating = newAvgRating

            // Update restaurant
            transaction.set(restaurantRef, restaurant)

            // Update rating
            val data = HashMap<String, Any>()
            data["rating"] = rating
            transaction.set(ratingRef, data, SetOptions.merge())

            null
        }
    }
    // [END add_rating]

}
