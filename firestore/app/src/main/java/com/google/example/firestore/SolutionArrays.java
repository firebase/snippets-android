package com.google.example.firestore;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * https://firebase.google.com/docs/firestore/solutions/arrays
 */
public class SolutionArrays {

    private FirebaseFirestore db;

    // [START array_post_class]
    public class ArrayPost {
        String title;
        String[] categories;

        public ArrayPost(String title, String[] categories) {
            this.title = title;
            this.categories = categories;
        }
    }
    // [END array_post_class]


    // [START map_post_class]
    public class MapPost {
        String title;
        Map<String,Boolean> categories;

        public MapPost(String title, Map<String,Boolean> categories) {
            this.title = title;
            this.categories = categories;
        }
    }
    // [END map_post_class]


    public void examplePosts() {
        // [START example_array_post]
        ArrayPost myArrayPost = new ArrayPost("My great post", new String[]{
                "technology", "opinion", "cats"
        });
        // [END example_array_post]

        // [START example_map_post]
        Map<String, Boolean> categories = new HashMap<>();
        categories.put("technology", true);
        categories.put("opinion", true);
        categories.put("cats", true);
        MapPost myMapPost = new MapPost("My great post", categories);
        // [END example_map_post]
    }

    public void queryForCats() {
        // [START query_for_cats]
        db.collection("posts")
                .whereEqualTo("categories.cats", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    // [START_EXCLUDE]
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {}
                    // [END_EXCLUDE]
                });
        // [END query_for_cats]
    }

}
