package com.google.example.firestore;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * https://firebase.google.com/docs/firestore/solutions/counters
 */
public class SolutionCounters {

    private FirebaseFirestore db;

    // [START counter_classes]
    // counters/${ID}
    public class Counter {
        int numShards;

        public Counter(int numShards) {
            this.numShards = numShards;
        }
    }

    // counters/${ID}/shards/${NUM}
    public class Shard {
        int count;

        public Shard(int count) {
            this.count = count;
        }
    }
    // [END counter_classes]

    // [START create_counter]
    public Task<Void> createCounter(final DocumentReference ref, final int numShards) {
        // Initialize the counter document, then initialize each shard.
        return ref.set(new Counter(numShards))
                .continueWithTask(new Continuation<Void, Task<Void>>() {
                    @Override
                    public Task<Void> then(@NonNull Task<Void> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        List<Task<Void>> tasks = new ArrayList<>();

                        // Initialize each shard with count=0
                        for (int i = 0; i < numShards; i++) {
                            Task<Void> makeShard = ref.collection("shards")
                                    .document(String.valueOf(i))
                                    .set(new Shard(0));

                            tasks.add(makeShard);
                        }

                        return Tasks.whenAll(tasks);
                    }
                });
    }
    // [END create_counter]

    // [START increment_counter]
    public Task<Void> incrementCounter(final DocumentReference ref, final int numShards) {
        int shardId = (int) Math.floor(Math.random() * numShards);
        DocumentReference shardRef = ref.collection("shards").document(String.valueOf(shardId));
        
        return shardRef.update("count", FieldValue.increment(1));
    }
    // [END increment_counter]

    // [START get_count]
    public Task<Integer> getCount(final DocumentReference ref) {
        // Sum the count of each shard in the subcollection
        return ref.collection("shards").get()
                .continueWith(new Continuation<QuerySnapshot, Integer>() {
                    @Override
                    public Integer then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        int count = 0;
                        for (DocumentSnapshot snap : task.getResult()) {
                            Shard shard = snap.toObject(Shard.class);
                            count += shard.count;
                        }
                        return count;
                    }
                });
    }
    // [END get_count]

}
