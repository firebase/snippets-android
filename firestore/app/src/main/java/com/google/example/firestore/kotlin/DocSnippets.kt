@file:Suppress("UNUSED_VARIABLE", "UNUSED_ANONYMOUS_PARAMETER")

package com.google.example.firestore.kotlin

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.AggregateField
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.firestore.persistentCacheSettings
import com.google.firebase.firestore.toObject
import com.google.firebase.Firebase
import java.util.ArrayList
import java.util.Date
import java.util.HashMap
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Pipeline
import com.google.firebase.firestore.pipeline.AggregateFunction
import com.google.firebase.firestore.pipeline.AggregateStage
import com.google.firebase.firestore.pipeline.Expression
import com.google.firebase.firestore.pipeline.SampleStage
import com.google.firebase.firestore.pipeline.UnnestOptions

/**
 * Kotlin version of doc snippets.
 *
 */
abstract class DocSnippets(val db: FirebaseFirestore) {

    companion object {

        private val TAG = "DocSnippets"

        private val EXECUTOR = ThreadPoolExecutor(
            2,
            4,
            60,
            TimeUnit.SECONDS,
            LinkedBlockingQueue(),
        )
    }

    internal fun runAll() {
        Log.d(TAG, "================= BEGIN RUN ALL ===============")

        // Write example data
        exampleData()
        exampleDataCollectionGroup()

        // Run all other methods
        addAdaLovelace()
        addAlanTuring()
        getAllUsers()
        listenForUsers()
        docReference()
        collectionReference()
        subcollectionReference()
        setDocument()
        dataTypes()
        addDocument()
        newDocument()
        updateDocument()
        updateDocumentNested()
        setFieldWithMerge()
        deleteDocument()
        transactions()
        transactionPromise()
        getDocument()
        getDocumentWithOptions()
        listenToDocument()
        listenToDocumentLocal()
        getMultipleDocs()
        getAllDocs()
        listenToMultiple()
        listenToDiffs()
        listenState()
        detachListener()
        handleListenErrors()
        simpleQueries()
        compoundQueries()
        orderAndLimit()
        queryStartAtEndAt()
        collectionGroupQuery()

        // Run methods that should fail
        try {
            compoundQueriesInvalid()
        } catch (e: Exception) {
            Log.d(TAG, "compoundQueriesInvalid", e)
        }

        try {
            orderAndLimitInvalid()
        } catch (e: Exception) {
            Log.d(TAG, "orderAndLimitInvalid", e)
        }
    }

    private fun setup() {
        // [START get_firestore_instance]
        val db = Firebase.firestore
        // [END get_firestore_instance]

        // [START set_firestore_settings]
        val settings = firestoreSettings {
            // Use memory cache
            setLocalCacheSettings(memoryCacheSettings {})
            // Use persistent disk cache (default)
            setLocalCacheSettings(persistentCacheSettings {})
        }
        db.firestoreSettings = settings
        // [END set_firestore_settings]
    }

    private fun setupCacheSize() {
        // [START fs_setup_cache]
        val settings = firestoreSettings {
            setLocalCacheSettings(persistentCacheSettings {
                // Set size to 100 MB
                setSizeBytes(1024 * 1024 * 100)
            })
        }
        db.firestoreSettings = settings
        // [END fs_setup_cache]
    }

    private fun addAdaLovelace() {
        // [START add_ada_lovelace]
        // Create a new user with a first and last name
        val user = hashMapOf(
            "first" to "Ada",
            "last" to "Lovelace",
            "born" to 1815,
        )

        // Add a new document with a generated ID
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
        // [END add_ada_lovelace]
    }

    private fun addAlanTuring() {
        // [START add_alan_turing]
        // Create a new user with a first, middle, and last name
        val user = hashMapOf(
            "first" to "Alan",
            "middle" to "Mathison",
            "last" to "Turing",
            "born" to 1912,
        )

        // Add a new document with a generated ID
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
        // [END add_alan_turing]
    }

    private fun getAllUsers() {
        // [START get_all_users]
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
        // [END get_all_users]
    }

    private fun listenForUsers() {
        // [START listen_for_users]
        // Listen for users born before 1900.
        //
        // You will get a first snapshot with the initial results and a new
        // snapshot each time there is a change in the results.
        db.collection("users")
            .whereLessThan("born", 1900)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                Log.d(TAG, "Current users born before 1900: $snapshots")
            }
        // [END listen_for_users]
    }

    private fun docReference() {
        // [START doc_reference]
        val alovelaceDocumentRef = db.collection("users").document("alovelace")
        // [END doc_reference]
    }

    private fun collectionReference() {
        // [START collection_reference]
        val usersCollectionRef = db.collection("users")
        // [END collection_reference]
    }

    private fun subcollectionReference() {
        // [START subcollection_reference]
        val messageRef = db
            .collection("rooms").document("roomA")
            .collection("messages").document("message1")
        // [END subcollection_reference]
    }

    fun docReferenceAlternate() {
        // [START doc_reference_alternate]
        val alovelaceDocumentRef = db.document("users/alovelace")
        // [END doc_reference_alternate]
    }

    // [START city_class]
    data class City(
        val name: String? = null,
        val state: String? = null,
        val country: String? = null,
        @field:JvmField // use this annotation if your Boolean field is prefixed with 'is'
        val isCapital: Boolean? = null,
        val population: Long? = null,
        val regions: List<String>? = null,
    )
    // [END city_class]

    private fun setDocument() {
        // [START set_document]
        val city = hashMapOf(
            "name" to "Los Angeles",
            "state" to "CA",
            "country" to "USA",
        )

        db.collection("cities").document("LA")
            .set(city)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        // [END set_document]

        val data = HashMap<String, Any>()

        // [START set_with_id]
        db.collection("cities").document("new-city-id").set(data)
        // [END set_with_id]
    }

    private fun dataTypes() {
        // [START data_types]
        val docData = hashMapOf(
            "stringExample" to "Hello world!",
            "booleanExample" to true,
            "numberExample" to 3.14159265,
            "dateExample" to Timestamp(Date()),
            "listExample" to arrayListOf(1, 2, 3),
            "nullExample" to null,
        )

        val nestedData = hashMapOf(
            "a" to 5,
            "b" to true,
        )

        docData["objectExample"] = nestedData

        db.collection("data").document("one")
            .set(docData)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        // [END data_types]
    }

    fun addCustomClass() {
        // [START add_custom_class]
        val city = City(
            "Los Angeles",
            "CA",
            "USA",
            false,
            5000000L,
            listOf("west_coast", "socal"),
        )
        db.collection("cities").document("LA").set(city)
        // [END add_custom_class]
    }

    private fun addDocument() {
        // [START add_document]
        // Add a new document with a generated id.
        val data = hashMapOf(
            "name" to "Tokyo",
            "country" to "Japan",
        )

        db.collection("cities")
            .add(data)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
        // [END add_document]
    }

    private fun newDocument() {
        // [START new_document]
        val data = HashMap<String, Any>()

        val newCityRef = db.collection("cities").document()

        // Later...
        newCityRef.set(data)
        // [END new_document]
    }

    private fun updateDocument() {
        // [START update_document]
        val washingtonRef = db.collection("cities").document("DC")

        // Set the "isCapital" field of the city 'DC'
        washingtonRef
            .update("capital", true)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
        // [END update_document]
    }

    fun updateDocumentArray() {
        // [START update_document_array]
        val washingtonRef = db.collection("cities").document("DC")

        // Atomically add a new region to the "regions" array field.
        washingtonRef.update("regions", FieldValue.arrayUnion("greater_virginia"))

        // Atomically remove a region from the "regions" array field.
        washingtonRef.update("regions", FieldValue.arrayRemove("east_coast"))
        // [END update_document_array]
    }

    fun updateDocumentIncrement() {
        // [START update_document_increment]
        val washingtonRef = db.collection("cities").document("DC")

        // Atomically increment the population of the city by 50.
        washingtonRef.update("population", FieldValue.increment(50))
        // [END update_document_increment]
    }

    private fun updateDocumentNested() {
        // [START update_document_nested]
        // Assume the document contains:
        // {
        //   name: "Frank",
        //   favorites: { food: "Pizza", color: "Blue", subject: "recess" }
        //   age: 12
        // }
        //
        // To update age and favorite color:
        db.collection("users").document("frank")
            .update(
                mapOf(
                    "age" to 13,
                    "favorites.color" to "Red",
                ),
            )
        // [END update_document_nested]
    }

    private fun setFieldWithMerge() {
        // [START set_field_with_merge]
        // Update one field, creating the document if it does not already exist.
        val data = hashMapOf("capital" to true)

        db.collection("cities").document("BJ")
            .set(data, SetOptions.merge())
        // [END set_field_with_merge]
    }

    private fun deleteDocument() {
        // [START delete_document]
        db.collection("cities").document("DC")
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
        // [END delete_document]
    }

    private fun transactions() {
        // [START transactions]
        val sfDocRef = db.collection("cities").document("SF")

        db.runTransaction { transaction ->
            val snapshot = transaction.get(sfDocRef)

            // Note: this could be done without a transaction
            //       by updating the population using FieldValue.increment()
            val newPopulation = snapshot.getDouble("population")!! + 1
            transaction.update(sfDocRef, "population", newPopulation)

            // Success
            null
        }.addOnSuccessListener { Log.d(TAG, "Transaction success!") }
            .addOnFailureListener { e -> Log.w(TAG, "Transaction failure.", e) }
        // [END transactions]
    }

    private fun transactionPromise() {
        // [START transaction_with_result]
        val sfDocRef = db.collection("cities").document("SF")

        db.runTransaction { transaction ->
            val snapshot = transaction.get(sfDocRef)
            val newPopulation = snapshot.getDouble("population")!! + 1
            if (newPopulation <= 1000000) {
                transaction.update(sfDocRef, "population", newPopulation)
                newPopulation
            } else {
                throw FirebaseFirestoreException(
                    "Population too high",
                    FirebaseFirestoreException.Code.ABORTED,
                )
            }
        }.addOnSuccessListener { result ->
            Log.d(TAG, "Transaction success: $result")
        }.addOnFailureListener { e ->
            Log.w(TAG, "Transaction failure.", e)
        }
        // [END transaction_with_result]
    }

    fun writeBatch() {
        // [START write_batch]
        val nycRef = db.collection("cities").document("NYC")
        val sfRef = db.collection("cities").document("SF")
        val laRef = db.collection("cities").document("LA")

        // Get a new write batch and commit all write operations
        db.runBatch { batch ->
            // Set the value of 'NYC'
            batch.set(nycRef, City())

            // Update the population of 'SF'
            batch.update(sfRef, "population", 1000000L)

            // Delete the city 'LA'
            batch.delete(laRef)
        }.addOnCompleteListener {
            // ...
        }
        // [END write_batch]
    }

    private fun getDocument() {
        // [START get_document]
        val docRef = db.collection("cities").document("SF")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
        // [END get_document]
    }

    private fun getDocumentWithOptions() {
        // [START get_document_options]
        val docRef = db.collection("cities").document("SF")

        // Source can be CACHE, SERVER, or DEFAULT.
        val source = Source.CACHE

        // Get the document, forcing the SDK to use the offline cache
        docRef.get(source).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Document found in the offline cache
                val document = task.result
                Log.d(TAG, "Cached document data: ${document?.data}")
            } else {
                Log.d(TAG, "Cached get failed: ", task.exception)
            }
        }
        // [END get_document_options]
    }

    fun customObjects() {
        // [START custom_objects]
        val docRef = db.collection("cities").document("BJ")
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val city = documentSnapshot.toObject<City>()
        }
        // [END custom_objects]
    }

    private fun listenToDocument() {
        // [START listen_document]
        val docRef = db.collection("cities").document("SF")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current data: ${snapshot.data}")
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
        // [END listen_document]
    }

    private fun listenToDocumentLocal() {
        // [START listen_document_local]
        val docRef = db.collection("cities").document("SF")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            val source = if (snapshot != null && snapshot.metadata.hasPendingWrites()) {
                "Local"
            } else {
                "Server"
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "$source data: ${snapshot.data}")
            } else {
                Log.d(TAG, "$source data: null")
            }
        }
        // [END listen_document_local]
    }

    fun listenWithMetadata() {
        // [START listen_with_metadata]
        // Listen for metadata changes to the document.
        val docRef = db.collection("cities").document("SF")
        docRef.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->
            // ...
        }
        // [END listen_with_metadata]
    }

    private fun getMultipleDocs() {
        // [START get_multiple]
        db.collection("cities")
            .whereEqualTo("capital", true)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
        // [END get_multiple]
    }

    private fun getAllDocs() {
        // [START get_multiple_all]
        db.collection("cities")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
        // [END get_multiple_all]
    }

    private fun getAllDocsSubcollection() {
        // [START firestore_query_subcollection]
        db.collection("cities")
            .document("SF")
            .collection("landmarks")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
        // [END firestore_query_subcollection]
    }

    private fun listenToMultiple() {
        // [START listen_multiple]
        db.collection("cities")
            .whereEqualTo("state", "CA")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                val cities = ArrayList<String>()
                for (doc in value!!) {
                    doc.getString("name")?.let {
                        cities.add(it)
                    }
                }
                Log.d(TAG, "Current cites in CA: $cities")
            }
        // [END listen_multiple]
    }

    private fun listenToDiffs() {
        // [START listen_diffs]
        db.collection("cities")
            .whereEqualTo("state", "CA")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "listen:error", e)
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> Log.d(TAG, "New city: ${dc.document.data}")
                        DocumentChange.Type.MODIFIED -> Log.d(TAG, "Modified city: ${dc.document.data}")
                        DocumentChange.Type.REMOVED -> Log.d(TAG, "Removed city: ${dc.document.data}")
                    }
                }
            }
        // [END listen_diffs]
    }

    private fun listenState() {
        // [START listen_state]
        db.collection("cities")
            .whereEqualTo("state", "CA")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "listen:error", e)
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        Log.d(TAG, "New city: ${dc.document.data}")
                    }
                }

                if (!snapshots.metadata.isFromCache) {
                    Log.d(TAG, "Got initial state.")
                }
            }
        // [END listen_state]
    }

    private fun detachListener() {
        // [START detach_listener]
        val query = db.collection("cities")
        val registration = query.addSnapshotListener { snapshots, e ->
            // ...
        }

        // ...

        // Stop listening to changes
        registration.remove()
        // [END detach_listener]
    }

    private fun handleListenErrors() {
        // [START handle_listen_errors]
        db.collection("cities")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "listen:error", e)
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        Log.d(TAG, "New city: ${dc.document.data}")
                    }
                }
            }
        // [END handle_listen_errors]
    }

    private fun exampleData() {
        // [START example_data]
        val cities = db.collection("cities")

        val data1 = hashMapOf(
            "name" to "San Francisco",
            "state" to "CA",
            "country" to "USA",
            "capital" to false,
            "population" to 860000,
            "regions" to listOf("west_coast", "norcal"),
        )
        cities.document("SF").set(data1)

        val data2 = hashMapOf(
            "name" to "Los Angeles",
            "state" to "CA",
            "country" to "USA",
            "capital" to false,
            "population" to 3900000,
            "regions" to listOf("west_coast", "socal"),
        )
        cities.document("LA").set(data2)

        val data3 = hashMapOf(
            "name" to "Washington D.C.",
            "state" to null,
            "country" to "USA",
            "capital" to true,
            "population" to 680000,
            "regions" to listOf("east_coast"),
        )
        cities.document("DC").set(data3)

        val data4 = hashMapOf(
            "name" to "Tokyo",
            "state" to null,
            "country" to "Japan",
            "capital" to true,
            "population" to 9000000,
            "regions" to listOf("kanto", "honshu"),
        )
        cities.document("TOK").set(data4)

        val data5 = hashMapOf(
            "name" to "Beijing",
            "state" to null,
            "country" to "China",
            "capital" to true,
            "population" to 21500000,
            "regions" to listOf("jingjinji", "hebei"),
        )
        cities.document("BJ").set(data5)
        // [END example_data]
    }

    fun exampleDataCollectionGroup() {
        // [START fs_collection_group_query_data_setup]
        val citiesRef = db.collection("cities")

        val ggbData = mapOf(
            "name" to "Golden Gate Bridge",
            "type" to "bridge",
        )
        citiesRef.document("SF").collection("landmarks").add(ggbData)

        val lohData = mapOf(
            "name" to "Legion of Honor",
            "type" to "museum",
        )
        citiesRef.document("SF").collection("landmarks").add(lohData)

        val gpData = mapOf(
            "name" to "Griffth Park",
            "type" to "park",
        )
        citiesRef.document("LA").collection("landmarks").add(gpData)

        val tgData = mapOf(
            "name" to "The Getty",
            "type" to "museum",
        )
        citiesRef.document("LA").collection("landmarks").add(tgData)

        val lmData = mapOf(
            "name" to "Lincoln Memorial",
            "type" to "memorial",
        )
        citiesRef.document("DC").collection("landmarks").add(lmData)

        val nasaData = mapOf(
            "name" to "National Air and Space Museum",
            "type" to "museum",
        )
        citiesRef.document("DC").collection("landmarks").add(nasaData)

        val upData = mapOf(
            "name" to "Ueno Park",
            "type" to "park",
        )
        citiesRef.document("TOK").collection("landmarks").add(upData)

        val nmData = mapOf(
            "name" to "National Musuem of Nature and Science",
            "type" to "museum",
        )
        citiesRef.document("TOK").collection("landmarks").add(nmData)

        val jpData = mapOf(
            "name" to "Jingshan Park",
            "type" to "park",
        )
        citiesRef.document("BJ").collection("landmarks").add(jpData)

        val baoData = mapOf(
            "name" to "Beijing Ancient Observatory",
            "type" to "musuem",
        )
        citiesRef.document("BJ").collection("landmarks").add(baoData)
        // [END fs_collection_group_query_data_setup]
    }

    private fun simpleQueries() {
        // [START simple_queries]
        // Create a reference to the cities collection
        val citiesRef = db.collection("cities")

        // Create a query against the collection.
        val query = citiesRef.whereEqualTo("state", "CA")
        // [END simple_queries]

        // [START simple_query_capital]
        val capitalCities = db.collection("cities").whereEqualTo("capital", true)
        // [END simple_query_capital]

        // [START example_filters]
        val stateQuery = citiesRef.whereEqualTo("state", "CA")
        val populationQuery = citiesRef.whereLessThan("population", 100000)
        val nameQuery = citiesRef.whereGreaterThanOrEqualTo("name", "San Francisco")
        // [END example_filters]

        // [START simple_query_not_equal]
        val notCapitalQuery = citiesRef.whereNotEqualTo("capital", false)
        // [END simple_query_not_equal]
    }

    fun arrayContainsQueries() {
        // [START array_contains_filter]
        val citiesRef = db.collection("cities")

        citiesRef.whereArrayContains("regions", "west_coast")
        // [END array_contains_filter]
    }

    fun arrayContainsAnyQueries() {
        // [START array_contains_any_filter]
        val citiesRef = db.collection("cities")

        citiesRef.whereArrayContainsAny("regions", listOf("west_coast", "east_coast"))
        // [END array_contains_any_filter]
    }

    fun inQueries() {
        // [START in_filter]
        val citiesRef = db.collection("cities")

        citiesRef.whereIn("country", listOf("USA", "Japan"))
        // [END in_filter]

        // [START not_in_filter]
        citiesRef.whereNotIn("country", listOf("USA", "Japan"))
        // [END not_in_filter]

        // [START in_filter_with_array]
        citiesRef.whereIn("regions", listOf(arrayOf("west_coast"), arrayOf("east_coast")))
        // [END in_filter_with_array]
    }

    private fun compoundQueries() {
        val citiesRef = db.collection("cities")

        // [START chain_filters]
        citiesRef.whereEqualTo("state", "CO").whereEqualTo("name", "Denver")
        citiesRef.whereEqualTo("state", "CA").whereLessThan("population", 1000000)
        // [END chain_filters]

        // [START valid_range_filters]
        citiesRef.whereGreaterThanOrEqualTo("state", "CA")
            .whereLessThanOrEqualTo("state", "IN")
        citiesRef.whereEqualTo("state", "CA")
            .whereGreaterThan("population", 1000000)
        // [END valid_range_filters]
    }

    private fun compoundQueriesInvalid() {
        val citiesRef = db.collection("cities")

        // [START invalid_range_filters]
        citiesRef.whereGreaterThanOrEqualTo("state", "CA")
            .whereGreaterThan("population", 100000)
        // [END invalid_range_filters]
    }

    private fun orderAndLimit() {
        val citiesRef = db.collection("cities")

        // [START order_and_limit]
        citiesRef.orderBy("name").limit(3)
        // [END order_and_limit]

        // [START order_and_limit_desc]
        citiesRef.orderBy("name", Query.Direction.DESCENDING).limit(3)
        // [END order_and_limit_desc]

        // [START order_by_multiple]
        citiesRef.orderBy("state").orderBy("population", Query.Direction.DESCENDING)
        // [END order_by_multiple]

        // [START filter_and_order]
        citiesRef.whereGreaterThan("population", 100000).orderBy("population").limit(2)
        // [END filter_and_order]

        // [START valid_filter_and_order]
        citiesRef.whereGreaterThan("population", 100000).orderBy("population")
        // [END valid_filter_and_order]
    }

    private fun orderAndLimitInvalid() {
        val citiesRef = db.collection("cities")

        // [START invalid_filter_and_order]
        citiesRef.whereGreaterThan("population", 100000).orderBy("country")
        // [END invalid_filter_and_order]
    }

    private fun queryStartAtEndAt() {
        // [START query_start_at_single]
        // Get all cities with a population >= 1,000,000, ordered by population,
        db.collection("cities")
            .orderBy("population")
            .startAt(1000000)
        // [END query_start_at_single]

        // [START query_end_at_single]
        // Get all cities with a population <= 1,000,000, ordered by population,
        db.collection("cities")
            .orderBy("population")
            .endAt(1000000)
        // [END query_end_at_single]

        // [START query_start_at_doc_snapshot]
        // Get the data for "San Francisco"
        db.collection("cities").document("SF")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                // Get all cities with a population bigger than San Francisco.
                val biggerThanSf = db.collection("cities")
                    .orderBy("population")
                    .startAt(documentSnapshot)

                // ...
            }
        // [END query_start_at_doc_snapshot]

        // [START query_pagination]
        // Construct query for first 25 cities, ordered by population
        val first = db.collection("cities")
            .orderBy("population")
            .limit(25)

        first.get()
            .addOnSuccessListener { documentSnapshots ->
                // ...

                // Get the last visible document
                val lastVisible = documentSnapshots.documents[documentSnapshots.size() - 1]

                // Construct a new query starting at this document,
                // get the next 25 cities.
                val next = db.collection("cities")
                    .orderBy("population")
                    .startAfter(lastVisible)
                    .limit(25)

                // Use the query for pagination
                // ...
            }
        // [END query_pagination]

        // [START multi_cursor]
        // Will return all Springfields
        db.collection("cities")
            .orderBy("name")
            .orderBy("state")
            .startAt("Springfield")

        // Will return "Springfield, Missouri" and "Springfield, Wisconsin"
        db.collection("cities")
            .orderBy("name")
            .orderBy("state")
            .startAt("Springfield", "Missouri")
        // [END multi_cursor]
    }

    private fun collectionGroupQuery() {
        // [START fs_collection_group_query]
        db.collectionGroup("landmarks").whereEqualTo("type", "museum").get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                // [START_EXCLUDE]
                for (snap in queryDocumentSnapshots) {
                    Log.d(TAG, "${snap.id} => ${snap.data}")
                }
                // [END_EXCLUDE]
            }
        // [END fs_collection_group_query]
    }

    fun toggleOffline() {
        // [START disable_network]
        db.disableNetwork().addOnCompleteListener {
            // Do offline things
            // ...
        }
        // [END disable_network]

        // [START enable_network]
        db.enableNetwork().addOnCompleteListener {
            // Do online things
            // ...
        }
        // [END enable_network]
    }

    fun offlineListen(db: FirebaseFirestore) {
        // [START offline_listen]
        db.collection("cities").whereEqualTo("state", "CA")
            .addSnapshotListener(MetadataChanges.INCLUDE) { querySnapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen error", e)
                    return@addSnapshotListener
                }

                for (change in querySnapshot!!.documentChanges) {
                    if (change.type == DocumentChange.Type.ADDED) {
                        Log.d(TAG, "New city: ${change.document.data}")
                    }

                    val source = if (querySnapshot.metadata.isFromCache) {
                        "local cache"
                    } else {
                        "server"
                    }
                    Log.d(TAG, "Data fetched from $source")
                }
            }
        // [END offline_listen]
    }

    // [START server_timestamp_annotation]
    inner class MyObject {

        var name: String? = null

        @ServerTimestamp
        var timestamp: Date? = null
    }
    // [END server_timestamp_annotation]

    fun updateWithServerTimestamp() {
        // [START update_with_server_timestamp]
        val docRef = db.collection("objects").document("some-id")

        // Update the timestamp field with the value from the server
        val updates = hashMapOf<String, Any>(
            "timestamp" to FieldValue.serverTimestamp(),
        )

        docRef.update(updates).addOnCompleteListener { }
        // [END update_with_server_timestamp]
    }

    fun updateDeleteField() {
        // [START update_delete_field]
        val docRef = db.collection("cities").document("BJ")

        // Remove the 'capital' field from the document
        val updates = hashMapOf<String, Any>(
            "capital" to FieldValue.delete(),
        )

        docRef.update(updates).addOnCompleteListener { }
        // [END update_delete_field]
    }

    fun countAggregateCollection() {
        // [START count_aggregate_collection]
        val query = db.collection("cities")
        val countQuery = query.count()
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Count fetched successfully
                val snapshot = task.result
                Log.d(TAG, "Count: ${snapshot.count}")
            } else {
                Log.d(TAG, "Count failed: ", task.getException())
            }
        }
        // [END count_aggregate_collection]
    }

    fun countAggregateQuery() {
        // [START count_aggregate_query]
        val query = db.collection("cities").whereEqualTo("state", "CA")
        val countQuery = query.count()
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Count fetched successfully
                val snapshot = task.result
                Log.d(TAG, "Count: ${snapshot.count}")
            } else {
                Log.d(TAG, "Count failed: ", task.getException())
            }
        }
        // [END count_aggregate_query]
    }

    fun orQuery() {
        val collection = db.collection("cities")
        // [START or_queries]
        val query = collection.where(Filter.and(
            Filter.equalTo("state", "CA"),
            Filter.or(
                Filter.equalTo("capital", true),
                Filter.greaterThanOrEqualTo("population", 1000000)
            )
        ))
        // [END or_queries]
    }

    fun orQueryDisjunctions() {
        val collection = db.collection("cities")

        // [START one_disjunction]
        collection.whereEqualTo("a", 1)
        // [END one_disjunction]

        // [START two_disjunctions]
        collection.where(Filter.or(
            Filter.equalTo("a", 1),
            Filter.equalTo("b", 2)
        ))
        // [END two_disjunctions]

        // [START four_disjunctions]
        collection.where(Filter.or(
            Filter.and(
                Filter.equalTo("a", 1),
                Filter.equalTo("c", 3)
            ),
            Filter.and(
                Filter.equalTo("a", 1),
                Filter.equalTo("d", 4)
            ),
            Filter.and(
                Filter.equalTo("b", 2),
                Filter.equalTo("c", 3)
            ),
            Filter.and(
                Filter.equalTo("b", 2),
                Filter.equalTo("d", 4)
            )
        ))
        // [END four_disjunctions]

        // [START four_disjunctions_compact]
        collection.where(Filter.and(
            Filter.or(
                Filter.equalTo("a", 1),
                Filter.equalTo("b", 2)
            ),
            Filter.or(
                Filter.equalTo("c", 3),
                Filter.equalTo("d", 4)
            )
        ))
        // [END four_disjunctions_compact]

        // [START 20_disjunctions]
        collection.where(Filter.or(
            Filter.inArray("a", listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)),
            Filter.inArray("b", listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)),
        ))
        // [END 20_disjunctions]

        // [START 10_disjunctions]
        collection.where(Filter.and(
            Filter.inArray("a", listOf(1, 2, 3, 4, 5)),
            Filter.or(
                Filter.equalTo("b", 2),
                Filter.equalTo("c", 3)
            )
        ))
        // [END 10_disjunctions]
    }

    fun illegalDisjunctions() {
        val collection = db.collection("cities")
        // [START 50_disjunctions]
        collection.where(Filter.and(
            Filter.inArray("a", listOf(1, 2, 3, 4, 5)),
            Filter.inArray("b", listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)),
        ));
        // [END 50_disjunctions]
    }

    fun sumAggregateCollection() {
        // [START sum_aggregate_collection]
        val query = db.collection("cities")
        val aggregateQuery = query.aggregate(AggregateField.sum("population"))
        aggregateQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Aggregate fetched successfully
                val snapshot = task.result
                Log.d(TAG, "Sum: ${snapshot.get(AggregateField.sum("population"))}")
            } else {
                Log.d(TAG, "Aggregate failed: ", task.getException())
            }
        }
        // [END sum_aggregate_collection]
    }

    fun sumAggregateQuery() {
        // [START sum_aggregate_query]
        val query = db.collection("cities").whereEqualTo("capital", true)
        val aggregateQuery = query.aggregate(AggregateField.sum("population"))
        aggregateQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Aggregate fetched successfully
                val snapshot = task.result
                Log.d(TAG, "Sum: ${snapshot.get(AggregateField.sum("population"))}")
            } else {
                Log.d(TAG, "Aggregate failed: ", task.getException())
            }
        }
        // [END sum_aggregate_query]
    }

    fun averageAggregateCollection() {
        // [START average_aggregate_collection]
        val query = db.collection("cities")
        val aggregateQuery = query.aggregate(AggregateField.average("population"))
        aggregateQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Aggregate fetched successfully
                val snapshot = task.result
                Log.d(TAG, "Average: ${snapshot.get(AggregateField.average("population"))}")
            } else {
                Log.d(TAG, "Aggregate failed: ", task.getException())
            }
        }
        // [END average_aggregate_collection]
    }

    fun averageAggregateQuery() {
        // [START average_aggregate_query]
        val query = db.collection("cities").whereEqualTo("capital", true)
        val aggregateQuery = query.aggregate(AggregateField.average("population"))
        aggregateQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Aggregate fetched successfully
                val snapshot = task.result
                Log.d(TAG, "Average: ${snapshot.get(AggregateField.average("population"))}")
            } else {
                Log.d(TAG, "Aggregate failed: ", task.getException())
            }
        }
        // [END average_aggregate_query]
    }

    fun multiAggregateQuery() {
        // [START multi_aggregate_query]
        val query = db.collection("cities")
        val aggregateQuery = query.aggregate(
            AggregateField.count(),
            AggregateField.sum("population"),
            AggregateField.average("population")
        )
        aggregateQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Aggregate fetched successfully
                val snapshot = task.result
                Log.d(TAG, "Count: ${snapshot.get(AggregateField.count())}")
                Log.d(TAG, "Sum: ${snapshot.get(AggregateField.sum("population"))}")
                Log.d(TAG, "Average: ${snapshot.get(AggregateField.average("population"))}")
            } else {
                Log.d(TAG, "Aggregate failed: ", task.getException())
            }
        }
        // [END multi_aggregate_query]
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#concepts
    fun pipelineConcepts() {
        // [START pipeline_concepts]
        val pipeline = db.pipeline()
            // Step 1: Start a query with collection scope
            .collection("cities")
            // Step 2: Filter the collection
            .where(Expression.field("population").greaterThan(100000))
            // Step 3: Sort the remaining documents
            .sort(Expression.field("name").ascending())
            // Step 4: Return the top 10. Note applying the limit earlier in the pipeline would have
            // unintentional results.
            .limit(10)
        // [END pipeline_concepts]
        println(pipeline)
    }

    fun basicPipelineRead() {
        // [START basic_pipeline_read]
        val readDataPipeline = db.pipeline()
            .collection("users")

        // Execute the pipeline and handle the result
        readDataPipeline.execute()
            .addOnSuccessListener { result ->
                for (document in result) {
                    println("${document.getId()} => ${document.getData()}")
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
        // [END basic_pipeline_read]
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#initialization
    fun pipelineInitialization() {
        // [START pipeline_initialization]
        val firestore = Firebase.firestore("enterprise")
        val pipeline = firestore.pipeline()
        // [END pipeline_initialization]
        println(pipeline)
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#field_vs_constant_references/
    fun fieldVsConstants() {
        // [START field_or_constant]
        val pipeline = db.pipeline()
            .collection("cities")
            .where(Expression.field("name").equal(Expression.constant("Toronto")))
        // [END field_or_constant]
        println(pipeline)
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#input_stages
    fun inputStages() {
        // [START input_stages]
        var results: Task<Pipeline.Snapshot>

        // Return all restaurants in San Francisco
        results = db.pipeline().collection("cities/sf/restaurants").execute()

        // Return all restaurants
        results = db.pipeline().collectionGroup("restaurants").execute()

        // Return all documents across all collections in the database (the entire database)
        results = db.pipeline().database().execute()

        // Batch read of 3 documents
        results = db.pipeline().documents(
            db.collection("cities").document("SF"),
            db.collection("cities").document("DC"),
            db.collection("cities").document("NY")
        ).execute()
        // [END input_stages]
        println(results)
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#where
    fun wherePipeline() {
        // [START pipeline_where]
        var results: Task<Pipeline.Snapshot>

        results = db.pipeline().collection("books")
            .where(Expression.field("rating").equal(5))
            .where(Expression.field("published").lessThan(1900))
            .execute()

        results = db.pipeline().collection("books")
            .where(Expression.and(Expression.field("rating").equal(5),
              Expression.field("published").lessThan(1900)))
            .execute()
        // [END pipeline_where]
        println(results)
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#aggregate_distinct
    fun aggregateGroups() {
        // [START aggregate_groups]
        val results = db.pipeline()
            .collection("books")
            .aggregate(
                AggregateStage
                    .withAccumulators(AggregateFunction.average("rating").alias("avg_rating"))
                    .withGroups(Expression.field("genre"))
            )
            .execute()
        // [END aggregate_groups]
        println(results)
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#aggregate_distinct
    fun aggregateDistinct() {
        // [START aggregate_distinct]
        val results = db.pipeline()
            .collection("books")
            .distinct(
                Expression.field("author").toUpper().alias("author"),
                Expression.field("genre")
            )
            .execute()
        // [END aggregate_distinct]
        println(results)
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#sort
    fun sort() {
        // [START sort]
        val results = db.pipeline()
            .collection("books")
            .sort(
                Expression.field("release_date").descending(),
                Expression.field("author").ascending()
            )
            .execute()
        // [END sort]
        println(results)
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#sort
    fun sortComparison() {
        // [START sort_comparison]
        val query = db.collection("cities")
            .orderBy("state")
            .orderBy("population", Query.Direction.DESCENDING)

        val pipeline = db.pipeline()
            .collection("books")
            .sort(
                Expression.field("release_date").descending(),
                Expression.field("author").ascending()
            )
        // [END sort_comparison]
        println(query)
        println(pipeline)
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#functions
    fun functions() {
        // [START functions_example]
        var results: Task<Pipeline.Snapshot>

        // Type 1: Scalar (for use in non-aggregation stages)
        // Example: Return the min store price for each book.
        results = db.pipeline().collection("books")
            .select(
                Expression.field("current").logicalMinimum("updated").alias("price_min")
            )
            .execute()

        // Type 2: Aggregation (for use in aggregate stages)
        // Example: Return the min price of all books.
        results = db.pipeline().collection("books")
            .aggregate(AggregateFunction.minimum("price").alias("min_price"))
            .execute()
        // [END functions_example]
        println(results)
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#creating_indexes
    fun creatingIndexes() {
        // [START query_example]
        val results = db.pipeline()
            .collection("books")
            .where(Expression.field("published").lessThan(1900))
            .where(Expression.field("genre").equal("Science Fiction"))
            .where(Expression.field("rating").greaterThan(4.3))
            .sort(Expression.field("published").descending())
            .execute()
        // [END query_example]
        println(results)
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#existing_sparse_indexes
    fun sparseIndexes() {
        // [START sparse_index_example]
        val results = db.pipeline()
            .collection("books")
            .where(Expression.field("category").like("%fantasy%"))
            .execute()
        // [END sparse_index_example]
        println(results)
    }

    fun sparseIndexes2() {
        // [START sparse_index_example_2]
        val results = db.pipeline()
            .collection("books")
            .sort(Expression.field("release_date").ascending())
            .execute()
        // [END sparse_index_example_2]
        println(results)
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#covered_queries_secondary_indexes
    fun coveredQuery() {
        // [START covered_query]
        val results = db.pipeline()
            .collection("books")
            .where(Expression.field("category").like("%fantasy%"))
            .where(Expression.field("title").exists())
            .where(Expression.field("author").exists())
            .select(Expression.field("title"), Expression.field("author"))
            .execute()
        // [END covered_query]
        println(results)
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#pagination
    fun pagination() {
        // [START pagination_not_supported_preview]
        // Existing pagination via `startAt()`
        val query = db.collection("cities").orderBy("population").startAt(1000000)

        // Private preview workaround using pipelines
        val pipeline = db.pipeline()
            .collection("cities")
            .where(Expression.field("population").greaterThanOrEqual(1000000))
            .sort(Expression.field("population").descending())
        // [END pagination_not_supported_preview]
        println(query)
        println(pipeline)
    }

    // http://cloud.google.com/firestore/docs/pipeline/stages/input/collection#example
    fun collectionStage() {
        // [START collection_example]
        val results = db.pipeline()
            .collection("users/bob/games")
            .sort(Expression.field("name").ascending())
            .execute()
        // [END collection_example]
        println(results)
    }

    // https://cloud.google.com/firestore/docs/pipeline/stages/input/collection_group
    fun collectionGroupStage() {
        // [START collection_group_example]
        val results = db.pipeline()
            .collectionGroup("games")
            .sort(Expression.field("name").ascending())
            .execute()
        // [END collection_group_example]
        println(results)
    }

    // https://cloud.google.com/firestore/docs/pipeline/stages/input/database
    fun databaseStage() {
        // [START database_example]
        // Count all documents in the database
        val results = db.pipeline()
            .database()
            .aggregate(AggregateFunction.countAll().alias("total"))
            .execute()
        // [END database_example]
        println(results)
    }

    // https://cloud.google.com/firestore/docs/pipeline/stages/input/documents
    fun documentsStage() {
        // [START documents_example]
        val results = db.pipeline()
            .documents(
                db.collection("cities").document("SF"),
                db.collection("cities").document("DC"),
                db.collection("cities").document("NY")
            ).execute()
        // [END documents_example]
        println(results)
    }

    fun replaceWithStage() {
        // [START initial_data]
        db.collection("cities").document("SF").set(mapOf(
            "name" to "San Francisco",
            "population" to 800000,
            "location" to mapOf(
                "country" to "USA",
                "state" to "California"
            )
        ))
        db.collection("cities").document("TO").set(mapOf(
            "name" to "Toronto",
            "population" to 3000000,
            "province" to "ON",
            "location" to mapOf(
                "country" to "Canada",
                "province" to "Ontario"
            )
        ))
        db.collection("cities").document("NY").set(mapOf(
            "name" to "New York",
            "location" to mapOf(
                "country" to "USA",
                "state" to "New York"
            )
        ))
        db.collection("cities").document("AT").set(mapOf(
            "name" to "Atlantis"
        ))
        // [END initial_data]

        // [START full_replace]
        val names = db.pipeline()
            .collection("cities")
            .replaceWith("location")
            .execute()
        // [END full_replace]

        // [START map_merge_overwrite]
        // unsupported in client SDKs for now
        // [END map_merge_overwrite]
        println(names)
    }

    // https://cloud.google.com/firestore/docs/pipeline/stages/transformation/sample#examples
    fun sampleStage() {
        // [START sample_example]
        var results: Task<Pipeline.Snapshot>

        // Get a sample of 100 documents in a database
        results = db.pipeline()
            .database()
            .sample(100)
            .execute()

        // Randomly shuffle a list of 3 documents
        results = db.pipeline()
            .documents(
                db.collection("cities").document("SF"),
                db.collection("cities").document("NY"),
                db.collection("cities").document("DC")
            )
            .sample(3)
            .execute()
        // [END sample_example]
        println(results)
    }

    // https://cloud.google.com/firestore/docs/pipeline/stages/transformation/sample#examples_2
    fun samplePercent() {
        // [START sample_percent]
        // Get a sample of on average 50% of the documents in the database
        val results = db.pipeline()
            .database()
            .sample(SampleStage.withPercentage(0.5))
            .execute()
        // [END sample_percent]
        println(results)
    }

    // https://cloud.google.com/firestore/docs/pipeline/stages/transformation/union#examples
    fun unionStage() {
        // [START union_stage]
        val results = db.pipeline()
            .collection("cities/SF/restaurants")
            .where(Expression.field("type").equal("Chinese"))
            .union(db.pipeline()
                .collection("cities/NY/restaurants")
                .where(Expression.field("type").equal("Italian")))
            .where(Expression.field("rating").greaterThanOrEqual(4.5))
            .sort(Expression.field("__name__").descending())
            .execute()
        // [END union_stage]
        println(results)
    }

    // https://cloud.google.com/firestore/docs/pipeline/stages/transformation/union#examples
    fun unionStageStable() {
        // [START union_stage_stable]
        val results = db.pipeline()
            .collection("cities/SF/restaurants")
            .where(Expression.field("type").equal("Chinese"))
            .union(db.pipeline()
                .collection("cities/NY/restaurants")
                .where(Expression.field("type").equal("Italian")))
            .where(Expression.field("rating").greaterThanOrEqual(4.5))
            .sort(Expression.field("__name__").descending())
            .execute()
        // [END union_stage_stable]
        println(results)
    }

    // https://cloud.google.com/firestore/docs/pipeline/stages/transformation/unnest#examples
    fun unnestStage() {
        // [START unnest_stage]
        val results = db.pipeline()
            .database()
            .unnest(Expression.field("arrayField").alias("unnestedArrayField"), UnnestOptions().withIndexField("index"))
            .execute()
        // [END unnest_stage]
        println(results)
    }

    // https://cloud.google.com/firestore/docs/pipeline/stages/transformation/unnest#examples
    fun unnestStageEmptyOrNonArray() {
        // [START unnest_edge_cases]
        // Input
        // { identifier : 1, neighbors: [ "Alice", "Cathy" ] }
        // { identifier : 2, neighbors: []                   }
        // { identifier : 3, neighbors: "Bob"                }

        val results = db.pipeline()
            .database()
            .unnest(Expression.field("neighbors").alias("unnestedNeighbors"), UnnestOptions().withIndexField("index"))
            .execute()

        // Output
        // { identifier: 1, neighbors: [ "Alice", "Cathy" ], unnestedNeighbors: "Alice", index: 0 }
        // { identifier: 1, neighbors: [ "Alice", "Cathy" ], unnestedNeighbors: "Cathy", index: 1 }
        // { identifier: 3, neighbors: "Bob", index: null}
        // [END unnest_edge_cases]
        println(results)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/aggregate_functions#count
    fun countFunction() {
        // [START count_function]
        // Total number of books in the collection
        val countAll = db.pipeline()
            .collection("books")
            .aggregate(AggregateFunction.countAll().alias("count"))
            .execute()

        // Number of books with nonnull `ratings` field
        val countField = db.pipeline()
            .collection("books")
            .aggregate(AggregateFunction.count("ratings").alias("count"))
            .execute()
        // [END count_function]
        println(countAll)
        println(countField)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/aggregate_functions#count_if
    fun countIfFunction() {
        // [START count_if]
        val result = db.pipeline()
            .collection("books")
            .aggregate(
                AggregateFunction.countIf(Expression.field("rating").greaterThan(4)).alias("filteredCount")
            )
            .execute()
        // [END count_if]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/aggregate_functions#count_distinct
    fun countDistinctFunction() {
        // [START count_distinct]
        val result = db.pipeline()
            .collection("books")
            .aggregate(AggregateFunction.countDistinct("author").alias("unique_authors"))
            .execute()
        // [END count_distinct]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/aggregate_functions#sum
    fun sumFunction() {
        // [START sum_function]
        val result = db.pipeline()
            .collection("cities")
            .aggregate(AggregateFunction.sum("population").alias("totalPopulation"))
            .execute()
        // [END sum_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/aggregate_functions#avg
    fun avgFunction() {
        // [START avg_function]
        val result = db.pipeline()
            .collection("cities")
            .aggregate(AggregateFunction.average("population").alias("averagePopulation"))
            .execute()
        // [END avg_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/aggregate_functions#min
    fun minFunction() {
        // [START min_function]
        val result = db.pipeline()
            .collection("books")
            .aggregate(AggregateFunction.minimum("price").alias("minimumPrice"))
            .execute()
        // [END min_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/aggregate_functions#max
    fun maxFunction() {
        // [START max_function]
        val result = db.pipeline()
            .collection("books")
            .aggregate(AggregateFunction.maximum("price").alias("maximumPrice"))
            .execute()
        // [END max_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#add
    fun addFunction() {
        // [START add_function]
        val result = db.pipeline()
            .collection("books")
            .select(Expression.add(Expression.field("soldBooks"), Expression.field("unsoldBooks")).alias("totalBooks"))
            .execute()
        // [END add_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#subtract
    fun subtractFunction() {
        // [START subtract_function]
        val storeCredit = 7
        val result = db.pipeline()
            .collection("books")
            .select(Expression.subtract(Expression.field("price"), storeCredit).alias("totalCost"))
            .execute()
        // [END subtract_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#multiply
    fun multiplyFunction() {
        // [START multiply_function]
        val result = db.pipeline()
            .collection("books")
            .select(Expression.multiply(Expression.field("price"), Expression.field("soldBooks")).alias("revenue"))
            .execute()
        // [END multiply_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#divide
    fun divideFunction() {
        // [START divide_function]
        val result = db.pipeline()
            .collection("books")
            .select(Expression.divide(Expression.field("ratings"), Expression.field("soldBooks")).alias("reviewRate"))
            .execute()
        // [END divide_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#mod
    fun modFunction() {
        // [START mod_function]
        val displayCapacity = 1000
        val result = db.pipeline()
            .collection("books")
            .select(Expression.mod(Expression.field("unsoldBooks"), displayCapacity).alias("warehousedBooks"))
            .execute()
        // [END mod_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#ceil
    fun ceilFunction() {
        // [START ceil_function]
        val booksPerShelf = 100
        val result = db.pipeline()
            .collection("books")
            .select(
                Expression.divide(Expression.field("unsoldBooks"), booksPerShelf).ceil().alias("requiredShelves")
            )
            .execute()
        // [END ceil_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#floor
    fun floorFunction() {
        // [START floor_function]
        val result = db.pipeline()
            .collection("books")
            .addFields(
                Expression.divide(Expression.field("wordCount"), Expression.field("pages")).floor().alias("wordsPerPage")
            )
            .execute()
        // [END floor_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#round
    fun roundFunction() {
        // [START round_function]
        val result = db.pipeline()
            .collection("books")
            .select(Expression.multiply(Expression.field("soldBooks"), Expression.field("price")).round().alias("partialRevenue"))
            .aggregate(AggregateFunction.sum("partialRevenue").alias("totalRevenue"))
            .execute()
        // [END round_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#pow
    fun powFunction() {
        // [START pow_function]
        val googleplex = GeoPoint(37.4221, -122.0853)
        val result = db.pipeline()
            .collection("cities")
            .addFields(
                Expression.field("lat").subtract(googleplex.latitude)
                    .multiply(111 /* km per degree */)
                    .pow(2)
                    .alias("latitudeDifference"),
                Expression.field("lng").subtract(googleplex.longitude)
                    .multiply(111 /* km per degree */)
                    .pow(2)
                    .alias("longitudeDifference")
            )
            .select(
                Expression.field("latitudeDifference").add(Expression.field("longitudeDifference")).sqrt()
                    // Inaccurate for large distances or close to poles
                    .alias("approximateDistanceToGoogle")
            )
            .execute()
        // [END pow_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#sqrt
    fun sqrtFunction() {
        // [START sqrt_function]
        val googleplex = GeoPoint(37.4221, -122.0853)
        val result = db.pipeline()
            .collection("cities")
            .addFields(
                Expression.field("lat").subtract(googleplex.latitude)
                    .multiply(111 /* km per degree */)
                    .pow(2)
                    .alias("latitudeDifference"),
                Expression.field("lng").subtract(googleplex.longitude)
                    .multiply(111 /* km per degree */)
                    .pow(2)
                    .alias("longitudeDifference")
            )
            .select(
                Expression.field("latitudeDifference").add(Expression.field("longitudeDifference")).sqrt()
                    // Inaccurate for large distances or close to poles
                    .alias("approximateDistanceToGoogle")
            )
            .execute()
        // [END sqrt_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#exp
    fun expFunction() {
        // [START exp_function]
        val result = db.pipeline()
            .collection("books")
            .select(Expression.field("rating").exp().alias("expRating"))
            .execute()
        // [END exp_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#ln
    fun lnFunction() {
        // [START ln_function]
        val result = db.pipeline()
            .collection("books")
            .select(Expression.field("rating").ln().alias("lnRating"))
            .execute()
        // [END ln_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#log
    fun logFunction() {
        // [START log_function]
        // Not supported on Android
        // [END log_function]
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/array_functions#array_concat
    fun arrayConcat() {
        // [START array_concat]
        val result = db.pipeline()
            .collection("books")
            .select(Expression.field("genre").arrayConcat(Expression.field("subGenre")).alias("allGenres"))
            .execute()
        // [END array_concat]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/array_functions#array_contains
    fun arrayContains() {
        // [START array_contains]
        val result = db.pipeline()
            .collection("books")
            .select(Expression.field("genre").arrayContains("mystery").alias("isMystery"))
            .execute()
        // [END array_contains]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/array_functions#array_contains_all
    fun arrayContainsAll() {
        // [START array_contains_all]
        val result = db.pipeline()
            .collection("books")
            .select(
                Expression.field("genre")
                    .arrayContainsAll(listOf("fantasy", "adventure"))
                    .alias("isFantasyAdventure")
            )
            .execute()
        // [END array_contains_all]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/array_functions#array_contains_any
    fun arrayContainsAny() {
        // [START array_contains_any]
        val result = db.pipeline()
            .collection("books")
            .select(
                Expression.field("genre")
                    .arrayContainsAny(listOf("fantasy", "nonfiction"))
                    .alias("isMysteryOrFantasy")
            )
            .execute()
        // [END array_contains_any]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/array_functions#array_length
    fun arrayLength() {
        // [START array_length]
        val result = db.pipeline()
            .collection("books")
            .select(Expression.field("genre").arrayLength().alias("genreCount"))
            .execute()
        // [END array_length]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/array_functions#array_reverse
    fun arrayReverse() {
        // [START array_reverse]
        val result = db.pipeline()
            .collection("books")
            .select(Expression.field("genre").arrayReverse().alias("reversedGenres"))
            .execute()
        // [END array_reverse]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/comparison_functions#eq
    fun equalFunction() {
        // [START equal_function]
        val result = db.pipeline()
            .collection("books")
            .select(Expression.field("rating").equal(5).alias("hasPerfectRating"))
            .execute()
        // [END equal_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/comparison_functions#gt
    fun greaterThanFunction() {
        // [START greater_than]
        val result = db.pipeline()
            .collection("books")
            .select(Expression.field("rating").greaterThan(4).alias("hasHighRating"))
            .execute()
        // [END greater_than]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/comparison_functions#gte
    fun greaterThanOrEqualToFunction() {
        // [START greater_or_equal]
        val result = db.pipeline()
            .collection("books")
            .select(Expression.field("published").greaterThanOrEqual(1900).alias("publishedIn20thCentury"))
            .execute()
        // [END greater_or_equal]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/comparison_functions#lt
    fun lessThanFunction() {
        // [START less_than]
        val result = db.pipeline()
            .collection("books")
            .select(Expression.field("published").lessThan(1923).alias("isPublicDomainProbably"))
            .execute()
        // [END less_than]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/comparison_functions#lte
    fun lessThanOrEqualToFunction() {
        // [START less_or_equal]
        val result = db.pipeline()
            .collection("books")
            .select(Expression.field("rating").lessThanOrEqual(2).alias("hasBadRating"))
            .execute()
        // [END less_or_equal]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/comparison_functions#neq
    fun notEqualFunction() {
        // [START not_equal]
        val result = db.pipeline()
            .collection("books")
            .select(Expression.field("title").notEqual("1984").alias("not1984"))
            .execute()
        // [END not_equal]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/debugging_functions#exists
    fun existsFunction() {
        // [START exists_function]
        val result = db.pipeline()
            .collection("books")
            .select(Expression.field("rating").exists().alias("hasRating"))
            .execute()
        // [END exists_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/logical_functions#and
    fun andFunction() {
        // [START and_function]
        val result = db.pipeline()
            .collection("books")
            .select(
                Expression.and(Expression.field("rating").greaterThan(4),
                  Expression.field("price").lessThan(10))
                    .alias("under10Recommendation")
            )
            .execute()
        // [END and_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/logical_functions#or
    fun orFunction() {
        // [START or_function]
        val result = db.pipeline()
            .collection("books")
            .select(
                Expression.or(Expression.field("genre").equal("Fantasy"),
                  Expression.field("tags").arrayContains("adventure"))
                    .alias("matchesSearchFilters")
            )
            .execute()
        // [END or_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/logical_functions#xor
    fun xorFunction() {
        // [START xor_function]
        val result = db.pipeline()
            .collection("books")
            .select(
                Expression.xor(Expression.field("tags").arrayContains("magic"),
                  Expression.field("tags").arrayContains("nonfiction"))
                    .alias("matchesSearchFilters")
            )
            .execute()
        // [END xor_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/logical_functions#not
    fun notFunction() {
        // [START not_function]
        val result = db.pipeline()
            .collection("books")
            .select(
                Expression.not(
                    Expression.field("tags").arrayContains("nonfiction")
                ).alias("isFiction")
            )
            .execute()
        // [END not_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/logical_functions#cond
    fun condFunction() {
        // [START cond_function]
        val result = db.pipeline()
            .collection("books")
            .select(
                Expression.field("tags").arrayConcat(
                    Expression.conditional(
                        Expression.field("pages").greaterThan(100),
                        Expression.constant("longRead"),
                        Expression.constant("shortRead")
                    )
                ).alias("extendedTags")
            )
            .execute()
        // [END cond_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/logical_functions#eq_any
    fun equalAnyFunction() {
        // [START eq_any]
        val result = db.pipeline()
            .collection("books")
            .select(
                Expression.field("genre").equalAny(listOf("Science Fiction", "Psychological Thriller"))
                    .alias("matchesGenreFilters")
            )
            .execute()
        // [END eq_any]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/logical_functions#not_eq_any
    fun notEqualAnyFunction() {
        // [START not_eq_any]
        val result = db.pipeline()
            .collection("books")
            .select(
                Expression.field("author").notEqualAny(listOf("George Orwell", "F. Scott Fitzgerald"))
                    .alias("byExcludedAuthors")
            )
            .execute()
        // [END not_eq_any]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/logical_functions#is_nan
    fun isNaNFunction() {
        // [START is_nan]
        // removed
        // [END is_nan]
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/logical_functions#is_not_nan
    fun isNotNaNFunction() {
        // [START is_not_nan]
        // removed
        // [END is_not_nan]
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/logical_functions#max
    fun maxLogicalFunction() {
        // [START max_logical_function]
        val result = db.pipeline()
            .collection("books")
            .select(
                Expression.field("rating").logicalMaximum(1).alias("flooredRating")
            )
            .execute()
        // [END max_logical_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/logical_functions#min
    fun minLogicalFunction() {
        // [START min_logical_function]
        val result = db.pipeline()
            .collection("books")
            .select(
                Expression.field("rating").logicalMinimum(5).alias("cappedRating")
            )
            .execute()
        // [END min_logical_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/map_functions#map_get
    fun mapGetFunction() {
        // [START map_get]
        val result = db.pipeline()
            .collection("books")
            .select(
                Expression.field("awards").mapGet("pulitzer").alias("hasPulitzerAward")
            )
            .execute()
        // [END map_get]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#byte_length
    fun byteLengthFunction() {
        // [START byte_length]
        val result = db.pipeline()
            .collection("books")
            .select(
                Expression.field("title").byteLength().alias("titleByteLength")
            )
            .execute()
        // [END byte_length]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#char_length
    fun charLengthFunction() {
        // [START char_length]
        val result = db.pipeline()
            .collection("books")
            .select(
                Expression.field("title").charLength().alias("titleCharLength")
            )
            .execute()
        // [END char_length]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#starts_with
    fun startsWithFunction() {
        // [START starts_with]
        val result = db.pipeline()
            .collection("books")
            .select(
                Expression.field("title").startsWith("The")
                    .alias("needsSpecialAlphabeticalSort")
            )
            .execute()
        // [END starts_with]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#ends_with
    fun endsWithFunction() {
        // [START ends_with]
        val result = db.pipeline()
            .collection("inventory/devices/laptops")
            .select(
                Expression.field("name").endsWith("16 inch")
                    .alias("16InLaptops")
            )
            .execute()
        // [END ends_with]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#like
    fun likeFunction() {
        // [START like]
        val result = db.pipeline()
            .collection("books")
            .select(
                Expression.field("genre").like("%Fiction")
                    .alias("anyFiction")
            )
            .execute()
        // [END like]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#regex_contains
    fun regexContainsFunction() {
        // [START regex_contains]
        val result = db.pipeline()
            .collection("documents")
            .select(
                Expression.field("title").regexContains("Firestore (Enterprise|Standard)")
                    .alias("isFirestoreRelated")
            )
            .execute()
        // [END regex_contains]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#regex_match
    fun regexMatchFunction() {
        // [START regex_match]
        val result = db.pipeline()
            .collection("documents")
            .select(
                Expression.field("title").regexMatch("Firestore (Enterprise|Standard)")
                    .alias("isFirestoreExactly")
            )
            .execute()
        // [END regex_match]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#str_concat
    fun strConcatFunction() {
        // [START str_concat]
        val result = db.pipeline()
            .collection("books")
            .select(
                Expression.field("title").concat(" by ", Expression.field("author"))
                    .alias("fullyQualifiedTitle")
            )
            .execute()
        // [END str_concat]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#str_contains
    fun strContainsFunction() {
        // [START string_contains]
        val result = db.pipeline()
            .collection("articles")
            .select(
                Expression.field("body").stringContains("Firestore")
                    .alias("isFirestoreRelated")
            )
            .execute()
        // [END string_contains]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#to_upper
    fun toUpperFunction() {
        // [START to_upper]
        val result = db.pipeline()
            .collection("authors")
            .select(
                Expression.field("name").toUpper()
                    .alias("uppercaseName")
            )
            .execute()
        // [END to_upper]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#to_lower
    fun toLowerFunction() {
        // [START to_lower]
        val result = db.pipeline()
            .collection("authors")
            .select(
                Expression.field("genre").toLower().equal("fantasy")
                    .alias("isFantasy")
            )
            .execute()
        // [END to_lower]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#substr
    fun substrFunction() {
        // [START substr_function]
        val result = db.pipeline()
            .collection("books")
            .where(Expression.field("title").startsWith("The "))
            .select(
                Expression.field("title")
                  .substring(Expression.constant(4),
                    Expression.field("title").charLength().subtract(4))
                    .alias("titleWithoutLeadingThe")
            )
            .execute()
        // [END substr_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#str_reverse
    fun strReverseFunction() {
        // [START str_reverse]
        val result = db.pipeline()
            .collection("books")
            .select(
                Expression.field("name").reverse().alias("reversedName")
            )
            .execute()
        // [END str_reverse]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#str_trim
    fun strTrimFunction() {
        // [START trim_function]
        val result = db.pipeline()
            .collection("books")
            .select(
                Expression.field("name").trim().alias("whitespaceTrimmedName")
            )
            .execute()
        // [END trim_function]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#str_replace
    fun strReplaceFunction() {
        // not yet supported until GA
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#str_split
    fun strSplitFunction() {
        // not yet supported until GA
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/timestamp_functions#unix_micros_to_timestamp
    fun unixMicrosToTimestampFunction() {
        // [START unix_micros_timestamp]
        val result = db.pipeline()
            .collection("documents")
            .select(
                Expression.field("createdAtMicros").unixMicrosToTimestamp().alias("createdAtString")
            )
            .execute()
        // [END unix_micros_timestamp]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/timestamp_functions#unix_millis_to_timestamp
    fun unixMillisToTimestampFunction() {
        // [START unix_millis_timestamp]
        val result = db.pipeline()
            .collection("documents")
            .select(
                Expression.field("createdAtMillis").unixMillisToTimestamp().alias("createdAtString")
            )
            .execute()
        // [END unix_millis_timestamp]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/timestamp_functions#unix_seconds_to_timestamp
    fun unixSecondsToTimestampFunction() {
        // [START unix_seconds_timestamp]
        val result = db.pipeline()
            .collection("documents")
            .select(
                Expression.field("createdAtSeconds").unixSecondsToTimestamp().alias("createdAtString")
            )
            .execute()
        // [END unix_seconds_timestamp]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/timestamp_functions#timestamp_add
    fun timestampAddFunction() {
        // [START timestamp_add]
        val result = db.pipeline()
            .collection("documents")
            .select(
                Expression.field("createdAt")
                  .timestampAdd("day", 3653)
                  .alias("expiresAt")
            )
            .execute()
        // [END timestamp_add]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/timestamp_functions#timestamp_sub
    fun timestampSubFunction() {
        // [START timestamp_sub]
        val result = db.pipeline()
            .collection("documents")
            .select(
                Expression.field("expiresAt")
                  .timestampSubtract("day", 14)
                  .alias("sendWarningTimestamp")
            )
            .execute()
        // [END timestamp_sub]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/timestamp_functions#timestamp_to_unix_micros
    fun timestampToUnixMicrosFunction() {
        // [START timestamp_unix_micros]
        val result = db.pipeline()
            .collection("documents")
            .select(
                Expression.field("dateString").timestampToUnixMicros().alias("unixMicros")
            )
            .execute()
        // [END timestamp_unix_micros]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/timestamp_functions#timestamp_to_unix_millis
    fun timestampToUnixMillisFunction() {
        // [START timestamp_unix_millis]
        val result = db.pipeline()
            .collection("documents")
            .select(
                Expression.field("dateString").timestampToUnixMillis().alias("unixMillis")
            )
            .execute()
        // [END timestamp_unix_millis]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/timestamp_functions#timestamp_to_unix_seconds
    fun timestampToUnixSecondsFunction() {
        // [START timestamp_unix_seconds]
        val result = db.pipeline()
            .collection("documents")
            .select(
                Expression.field("dateString").timestampToUnixSeconds().alias("unixSeconds")
            )
            .execute()
        // [END timestamp_unix_seconds]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/vector_functions#cosine_distance
    fun cosineDistanceFunction() {
        // [START cosine_distance]
        val sampleVector = doubleArrayOf(0.0, 1.0, 2.0, 3.0, 4.0, 5.0)
        val result = db.pipeline()
            .collection("books")
            .select(
                Expression.field("embedding").cosineDistance(sampleVector).alias("cosineDistance")
            )
            .execute()
        // [END cosine_distance]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/vector_functions#dot_product
    fun dotProductFunction() {
        // [START dot_product]
        val sampleVector = doubleArrayOf(0.0, 1.0, 2.0, 3.0, 4.0, 5.0)
        val result = db.pipeline()
            .collection("books")
            .select(
                Expression.field("embedding").dotProduct(sampleVector).alias("dotProduct")
            )
            .execute()
        // [END dot_product]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/vector_functions#euclidean_distance
    fun euclideanDistanceFunction() {
        // [START euclidean_distance]
        val sampleVector = doubleArrayOf(0.0, 1.0, 2.0, 3.0, 4.0, 5.0)
        val result = db.pipeline()
            .collection("books")
            .select(
                Expression.field("embedding").euclideanDistance(sampleVector).alias("euclideanDistance")
            )
            .execute()
        // [END euclidean_distance]
        println(result)
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/vector_functions#vector_length
    fun vectorLengthFunction() {
        // [START vector_length]
        val result = db.pipeline()
            .collection("books")
            .select(
                Expression.field("embedding").vectorLength().alias("vectorLength")
            )
            .execute()
        // [END vector_length]
        println(result)
    }
}
