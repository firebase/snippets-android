@file:Suppress("UNUSED_VARIABLE", "UNUSED_ANONYMOUS_PARAMETER")

package com.google.example.firestore.kotlin

import android.util.Log
import androidx.annotation.WorkerThread
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.ArrayList
import java.util.Date
import java.util.HashMap
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Kotlin version of doc snippets.
 *
 */
abstract class DocSnippets(val db: FirebaseFirestore) {

    companion object {

        private val TAG = "DocSnippets"

        private val EXECUTOR = ThreadPoolExecutor(2, 4,
                60, TimeUnit.SECONDS, LinkedBlockingQueue()
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
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settings
        // [END set_firestore_settings]
    }

    private fun setupCacheSize() {
        // [START fs_setup_cache]
        val settings = firestoreSettings {
            cacheSizeBytes = FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED
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
                "born" to 1815
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
                "born" to 1912
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
        val regions: List<String>? = null
    )
    // [END city_class]

    private fun setDocument() {
        // [START set_document]
        val city = hashMapOf(
                "name" to "Los Angeles",
                "state" to "CA",
                "country" to "USA"
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
                "nullExample" to null
        )

        val nestedData = hashMapOf(
                "a" to 5,
                "b" to true
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
        val city = City("Los Angeles", "CA", "USA",
                false, 5000000L, listOf("west_coast", "socal"))
        db.collection("cities").document("LA").set(city)
        // [END add_custom_class]
    }

    private fun addDocument() {
        // [START add_document]
        // Add a new document with a generated id.
        val data = hashMapOf(
                "name" to "Tokyo",
                "country" to "Japan"
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
                .update(mapOf(
                        "age" to 13,
                        "favorites.color" to "Red"
                ))
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
                throw FirebaseFirestoreException("Population too high",
                        FirebaseFirestoreException.Code.ABORTED)
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

            val source = if (snapshot != null && snapshot.metadata.hasPendingWrites())
                "Local"
            else
                "Server"

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
                "regions" to listOf("west_coast", "norcal")
        )
        cities.document("SF").set(data1)

        val data2 = hashMapOf(
                "name" to "Los Angeles",
                "state" to "CA",
                "country" to "USA",
                "capital" to false,
                "population" to 3900000,
                "regions" to listOf("west_coast", "socal")
        )
        cities.document("LA").set(data2)

        val data3 = hashMapOf(
                "name" to "Washington D.C.",
                "state" to null,
                "country" to "USA",
                "capital" to true,
                "population" to 680000,
                "regions" to listOf("east_coast")
        )
        cities.document("DC").set(data3)

        val data4 = hashMapOf(
                "name" to "Tokyo",
                "state" to null,
                "country" to "Japan",
                "capital" to true,
                "population" to 9000000,
                "regions" to listOf("kanto", "honshu")
        )
        cities.document("TOK").set(data4)

        val data5 = hashMapOf(
                "name" to "Beijing",
                "state" to null,
                "country" to "China",
                "capital" to true,
                "population" to 21500000,
                "regions" to listOf("jingjinji", "hebei")
        )
        cities.document("BJ").set(data5)
        // [END example_data]
    }

    fun exampleDataCollectionGroup() {
        // [START fs_collection_group_query_data_setup]
        val citiesRef = db.collection("cities")

        val ggbData = mapOf(
                "name" to "Golden Gate Bridge",
                "type" to "bridge"
        )
        citiesRef.document("SF").collection("landmarks").add(ggbData)

        val lohData = mapOf(
                "name" to "Legion of Honor",
                "type" to "museum"
        )
        citiesRef.document("SF").collection("landmarks").add(lohData)

        val gpData = mapOf(
                "name" to "Griffth Park",
                "type" to "park"
        )
        citiesRef.document("LA").collection("landmarks").add(gpData)

        val tgData = mapOf(
                "name" to "The Getty",
                "type" to "museum"
        )
        citiesRef.document("LA").collection("landmarks").add(tgData)

        val lmData = mapOf(
                "name" to "Lincoln Memorial",
                "type" to "memorial"
        )
        citiesRef.document("DC").collection("landmarks").add(lmData)

        val nasaData = mapOf(
                "name" to "National Air and Space Museum",
                "type" to "museum"
        )
        citiesRef.document("DC").collection("landmarks").add(nasaData)

        val upData = mapOf(
                "name" to "Ueno Park",
                "type" to "park"
        )
        citiesRef.document("TOK").collection("landmarks").add(upData)

        val nmData = mapOf(
                "name" to "National Musuem of Nature and Science",
                "type" to "museum"
        )
        citiesRef.document("TOK").collection("landmarks").add(nmData)

        val jpData = mapOf(
                "name" to "Jingshan Park",
                "type" to "park"
        )
        citiesRef.document("BJ").collection("landmarks").add(jpData)

        val baoData = mapOf(
                "name" to "Beijing Ancient Observatory",
                "type" to "musuem"
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
        citiesRef.whereEqualTo("state", "CA")
        citiesRef.whereLessThan("population", 100000)
        citiesRef.whereGreaterThanOrEqualTo("name", "San Francisco")
        // [END example_filters]

        // [START simple_query_not_equal]
        citiesRef.whereNotEqualTo("capital", false)
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

    // [START delete_collection]
    /**
     * Delete all documents in a collection. Uses an Executor to perform work on a background
     * thread. This does *not* automatically discover and delete subcollections.
     */
    private fun deleteCollection(
        collection: CollectionReference,
        batchSize: Int,
        executor: Executor
    ): Task<Void> {

        // Perform the delete operation on the provided Executor, which allows us to use
        // simpler synchronous logic without blocking the main thread.
        return Tasks.call(executor, Callable<Void> {
            // Get the first batch of documents in the collection
            var query = collection.orderBy(FieldPath.documentId()).limit(batchSize.toLong())

            // Get a list of deleted documents
            var deleted = deleteQueryBatch(query)

            // While the deleted documents in the last batch indicate that there
            // may still be more documents in the collection, page down to the
            // next batch and delete again
            while (deleted.size >= batchSize) {
                // Move the query cursor to start after the last doc in the batch
                val last = deleted[deleted.size - 1]
                query = collection.orderBy(FieldPath.documentId())
                        .startAfter(last.id)
                        .limit(batchSize.toLong())

                deleted = deleteQueryBatch(query)
            }

            null
        })
    }

    /**
     * Delete all results from a query in a single WriteBatch. Must be run on a worker thread
     * to avoid blocking/crashing the main thread.
     */
    @WorkerThread
    @Throws(Exception::class)
    private fun deleteQueryBatch(query: Query): List<DocumentSnapshot> {
        val querySnapshot = Tasks.await(query.get())

        val batch = query.firestore.batch()
        for (snapshot in querySnapshot) {
            batch.delete(snapshot.reference)
        }
        Tasks.await(batch.commit())

        return querySnapshot.documents
    }
    // [END delete_collection]

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

                        val source = if (querySnapshot.metadata.isFromCache)
                            "local cache"
                        else
                            "server"
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
                "timestamp" to FieldValue.serverTimestamp()
        )

        docRef.update(updates).addOnCompleteListener { }
        // [END update_with_server_timestamp]
    }

    fun updateDeleteField() {
        // [START update_delete_field]
        val docRef = db.collection("cities").document("BJ")

        // Remove the 'capital' field from the document
        val updates = hashMapOf<String, Any>(
                "capital" to FieldValue.delete()
        )

        docRef.update(updates).addOnCompleteListener { }
        // [END update_delete_field]
    }
}
