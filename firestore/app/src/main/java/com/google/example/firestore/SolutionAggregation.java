package com.google.example.firestore;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.Map;

/**
 * https://firebase.google.com/docs/firestore/solutions/aggregation
 */
public class SolutionAggregation {

    private FirebaseFirestore db;

    // [START restaurant_class]
    public class Restaurant {
        String name;
        double avgRating;
        int numRatings;

        public Restaurant(String name, double avgRating, int numRatings) {
            this.name = name;
            this.avgRating = avgRating;
            this.numRatings = numRatings;
        }
    }
    // [END restaurant_class]

    public void exampleRestaurant() {
        // [START example_restaurant]
        Restaurant arinell = new Restaurant("Arinell Pizza", 4.65, 683);
        // [END example_restaurant]
    }

    public void getAllRatings() {
        // [START get_all_ratings]
        db.collection("restaurants")
                .document("arinell-pizza")
                .collection("ratings")
                .get();
        // [END get_all_ratings]
    }

    // [START add_rating]
    private Task<Void> addRating(final DocumentReference restaurantRef, final float rating) {
        // Create reference for new rating, for use inside the transaction
        final DocumentReference ratingRef = restaurantRef.collection("ratings").document();

        // In a transaction, add the new rating and update the aggregate totals
        return db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                Restaurant restaurant = transaction.get(restaurantRef).toObject(Restaurant.class);

                // Compute new number of ratings
                int newNumRatings = restaurant.numRatings + 1;

                // Compute new average rating
                double oldRatingTotal = restaurant.avgRating * restaurant.numRatings;
                double newAvgRating = (oldRatingTotal + rating) / newNumRatings;

                // Set new restaurant info
                restaurant.numRatings = newNumRatings;
                restaurant.avgRating = newAvgRating;

                // Update restaurant
                transaction.set(restaurantRef, restaurant);

                // Update rating
                Map<String, Object> data = new HashMap<>();
                data.put("rating", rating);
                transaction.set(ratingRef, data, SetOptions.merge());

                return null;
            }
        });
    }
    // [END add_rating]

}
