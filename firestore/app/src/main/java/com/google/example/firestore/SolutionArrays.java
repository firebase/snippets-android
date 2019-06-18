package com.google.example.firestore;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://firebase.google.com/docs/firestore/solutions/arrays
 */
public class SolutionArrays {

    private FirebaseFirestore db;

    // [START array_post_class]
    public class ArrayPost {
        String title;
        List<String> categories;

        public ArrayPost(String title, List<String> categories) {
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

    // [START map_post_class_advanced]
    public class MapPostAdvanced {
        String title;
        Map<String,Long> categories;

        public MapPostAdvanced(String title, Map<String,Long> categories) {
            this.title = title;
            this.categories = categories;
        }
    }
    // [END map_post_class_advanced]


    public void examplePosts() {
        // [START example_array_post]
        ArrayPost myArrayPost = new ArrayPost("My great post", Arrays.asList(
                "technology", "opinion", "cats"
        ));
        // [END example_array_post]

        // [START example_map_post]
        Map<String, Boolean> categories = new HashMap<>();
        categories.put("technology", true);
        categories.put("opinion", true);
        categories.put("cats", true);
        MapPost myMapPost = new MapPost("My great post", categories);
        // [END example_map_post]
    }

    public void examplePosts_Advanced() {
        // [START example_map_post_advanced]
        Map<String, Long> categories = new HashMap<>();
        categories.put("technology", 1502144665L);
        categories.put("opinion", 1502144665L);
        categories.put("cats", 1502144665L);
        MapPostAdvanced myMapPostAdvanced = new MapPostAdvanced("My great post", categories);
        // [END example_map_post_advanced]
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

    public void queryForCatsTimestamp() {
        // [START query_for_cats_timestamp_invalid]
        db.collection("posts")
                .whereEqualTo("categories.cats", true)
                .orderBy("timestamp");
        // [END query_for_cats_timestamp_invalid]

        // [START query_for_cats_timestamp]
        db.collection("posts")
                .whereGreaterThan("categories.cats", 0)
                .orderBy("categories.cats");
        // [END query_for_cats_timestamp]
    }
}
