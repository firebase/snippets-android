package com.google.example.firestore.kotlin

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import kotlin.math.floor

/**
 * https://firebase.google.com/docs/firestore/solutions/counters
 */
class SolutionCounters(val db: FirebaseFirestore) {

    // [START counter_classes]
    // counters/${ID}
    data class Counter(var numShards: Int)

    // counters/${ID}/shards/${NUM}
    data class Shard(var count: Int)
    // [END counter_classes]

    // [START create_counter]
    suspend fun createCounter(ref: DocumentReference, numShards: Int) {
        // Initialize the counter document, then initialize each shard.
        ref.set(Counter(numShards)).await()

        (0 until numShards).map {
            // Initialize each shard with count=0
            ref.collection("shards")
                    .document(it.toString())
                    .set(Shard(0))
                    .asDeferred()

        }.awaitAll()
    }
    // [END create_counter]

    // [START increment_counter]
    suspend fun incrementCounter(ref: DocumentReference, numShards: Int) {
        val shardId = floor(Math.random() * numShards).toInt()
        val shardRef = ref.collection("shards").document(shardId.toString())

        shardRef.update("count", FieldValue.increment(1)).await()
    }
    // [END increment_counter]

    // [START get_count]
    suspend fun getCount(ref: DocumentReference): Int {
        // Sum the count of each shard in the subcollection
        val result = ref.collection("shards").get().await()
        var count = 0
        for (snap in result!!) {
            val shard = snap.toObject<Shard>()
            count += shard.count
        }
        return count
    }
    // [END get_count]
}
