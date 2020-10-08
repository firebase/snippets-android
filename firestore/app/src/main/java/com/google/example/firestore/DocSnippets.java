package com.google.example.firestore;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentChange.Type;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Snippets for inclusion in documentation.
 */
@SuppressWarnings({"unused", "Convert2Lambda"})
public class DocSnippets {

    private static final String TAG = "DocSnippets";

    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(2, 4,
            60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    private final FirebaseFirestore db;

    DocSnippets(FirebaseFirestore db) {
        this.db = db;
    }

    void runAll() {
        Log.d(TAG, "================= BEGIN RUN ALL ===============");

        // Write example data
        exampleData();
        exampleDataCollectionGroup();

        // Run all other methods
        addAdaLovelace();
        addAlanTuring();
        getAllUsers();
        listenForUsers();
        docReference();
        collectionReference();
        subcollectionReference();
        setDocument();
        dataTypes();
        addDocument();
        newDocument();
        updateDocument();
        updateDocumentNested();
        setFieldWithMerge();
        deleteDocument();
        transactions();
        transactionPromise();
        getDocument();
        getDocumentWithOptions();
        listenToDocument();
        listenToDocumentLocal();
        getMultipleDocs();
        getAllDocs();
        listenToMultiple();
        listenToDiffs();
        listenState();
        detachListener();
        handleListenErrors();
        simpleQueries();
        compoundQueries();
        orderAndLimit();
        queryStartAtEndAt();

        // Run methods that should fail
        try {
            compoundQueriesInvalid();
        } catch (Exception e) {
            Log.d(TAG, "compoundQueriesInvalid", e);
        }

        try {
            orderAndLimitInvalid();
        } catch (Exception e) {
            Log.d(TAG, "orderAndLimitInvalid", e);
        }
    }


    void deleteAll() {
        deleteCollection("cities");
        deleteCollection("users");
    }

    private void deleteCollection(final String path) {
        deleteCollection(db.collection(path), 50, EXECUTOR);
    }

    public void setup() {
        // [START get_firestore_instance]
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // [END get_firestore_instance]

        // [START set_firestore_settings]
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        // [END set_firestore_settings]
    }

    public void setupCacheSize() {
        // [START fs_setup_cache]
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();
        db.setFirestoreSettings(settings);
        // [END fs_setup_cache]
    }

    public void addAdaLovelace() {
        // [START add_ada_lovelace]
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        // [END add_ada_lovelace]
    }


    public void addAlanTuring() {
        // [START add_alan_turing]
        // Create a new user with a first, middle, and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Alan");
        user.put("middle", "Mathison");
        user.put("last", "Turing");
        user.put("born", 1912);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        // [END add_alan_turing]
    }

    public void getAllUsers() {
        // [START get_all_users]
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        // [END get_all_users]
    }

    public void listenForUsers() {
        // [START listen_for_users]
        // Listen for users born before 1900.
        //
        // You will get a first snapshot with the initial results and a new
        // snapshot each time there is a change in the results.
        db.collection("users")
                .whereLessThan("born", 1900)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        Log.d(TAG, "Current users born before 1900: " + snapshots);
                    }
                });
        // [END listen_for_users]
    }

    public void docReference() {
        // [START doc_reference]
        DocumentReference alovelaceDocumentRef = db.collection("users").document("alovelace");
        // [END doc_reference]
    }

    public void collectionReference() {
        // [START collection_reference]
        CollectionReference usersCollectionRef = db.collection("users");
        // [END collection_reference]
    }

    public void subcollectionReference() {
        // [START subcollection_reference]
        DocumentReference messageRef = db
                .collection("rooms").document("roomA")
                .collection("messages").document("message1");
        // [END subcollection_reference]
    }

    public void docReferenceAlternate() {
        // [START doc_reference_alternate]
        DocumentReference alovelaceDocumentRef = db.document("users/alovelace");
        // [END doc_reference_alternate]
    }

    // [START city_class]
    public class City {


        private String name;
        private String state;
        private String country;
        private boolean capital;
        private long population;
        private List<String> regions;

        public City() {}

        public City(String name, String state, String country, boolean capital, long population, List<String> regions) {
            // [START_EXCLUDE]
            this.name = name;
            this.state = state;
            this.country = country;
            this.capital = capital;
            this.population = population;
            this.regions = regions;
            // [END_EXCLUDE]
        }

        public String getName() {
            return name;
        }

        public String getState() {
            return state;
        }

        public String getCountry() {
            return country;
        }

        public boolean isCapital() {
            return capital;
        }

        public long getPopulation() {
            return population;
        }

        public List<String> getRegions() {
            return regions;
        }

    }
    // [END city_class]

    public void setDocument() {
        // [START set_document]
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Los Angeles");
        city.put("state", "CA");
        city.put("country", "USA");

        db.collection("cities").document("LA")
                .set(city)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
        // [END set_document]

        Map<String, Object> data = new HashMap<>();

        // [START set_with_id]
        db.collection("cities").document("new-city-id").set(data);
        // [END set_with_id]
    }

    public void dataTypes() {
        // [START data_types]
        Map<String, Object> docData = new HashMap<>();
        docData.put("stringExample", "Hello world!");
        docData.put("booleanExample", true);
        docData.put("numberExample", 3.14159265);
        docData.put("dateExample", new Timestamp(new Date()));
        docData.put("listExample", Arrays.asList(1, 2, 3));
        docData.put("nullExample", null);

        Map<String, Object> nestedData = new HashMap<>();
        nestedData.put("a", 5);
        nestedData.put("b", true);

        docData.put("objectExample", nestedData);

        db.collection("data").document("one")
                .set(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
        // [END data_types]
    }

    public void addCustomClass() {
        // [START add_custom_class]
        City city = new City("Los Angeles", "CA", "USA",
                false, 5000000L, Arrays.asList("west_coast", "sorcal"));
        db.collection("cities").document("LA").set(city);
        // [END add_custom_class]
    }

    public void addDocument() {
        // [START add_document]
        // Add a new document with a generated id.
        Map<String, Object> data = new HashMap<>();
        data.put("name", "Tokyo");
        data.put("country", "Japan");

        db.collection("cities")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        // [END add_document]
    }

    public void newDocument() {
        // [START new_document]
        Map<String, Object> data = new HashMap<>();

        DocumentReference newCityRef = db.collection("cities").document();

        // Later...
        newCityRef.set(data);
        // [END new_document]
    }

    public void updateDocument() {
        // [START update_document]
        DocumentReference washingtonRef = db.collection("cities").document("DC");

        // Set the "isCapital" field of the city 'DC'
        washingtonRef
                .update("capital", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
        // [END update_document]
    }

    public void updateDocumentArray() {
        // [START update_document_array]
        DocumentReference washingtonRef = db.collection("cities").document("DC");

        // Atomically add a new region to the "regions" array field.
        washingtonRef.update("regions", FieldValue.arrayUnion("greater_virginia"));

        // Atomically remove a region from the "regions" array field.
        washingtonRef.update("regions", FieldValue.arrayRemove("east_coast"));
        // [END update_document_array]
    }

    public void updateDocumentIncrement() {
        // [START update_document_increment]
        DocumentReference washingtonRef = db.collection("cities").document("DC");

        // Atomically increment the population of the city by 50.
        washingtonRef.update("population", FieldValue.increment(50));
        // [END update_document_increment]
    }

    public void updateDocumentNested() {
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
                        "age", 13,
                        "favorites.color", "Red"
                );
        // [END update_document_nested]
    }

    public void setFieldWithMerge() {
        // [START set_field_with_merge]
        // Update one field, creating the document if it does not already exist.
        Map<String, Object> data = new HashMap<>();
        data.put("capital", true);

        db.collection("cities").document("BJ")
                .set(data, SetOptions.merge());
        // [END set_field_with_merge]
    }

    public void deleteDocument() {
        // [START delete_document]
        db.collection("cities").document("DC")
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
        // [END delete_document]
    }

    public void transactions() {
        // [START transactions]
        final DocumentReference sfDocRef = db.collection("cities").document("SF");

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);

                // Note: this could be done without a transaction
                //       by updating the population using FieldValue.increment()
                double newPopulation = snapshot.getDouble("population") + 1;
                transaction.update(sfDocRef, "population", newPopulation);

                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Transaction success!");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Transaction failure.", e);
            }
        });
        // [END transactions]
    }

    public void transactionPromise() {
        // [START transaction_with_result]
        final DocumentReference sfDocRef = db.collection("cities").document("SF");

        db.runTransaction(new Transaction.Function<Double>() {
            @Override
            public Double apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);
                double newPopulation = snapshot.getDouble("population") + 1;
                if (newPopulation <= 1000000) {
                    transaction.update(sfDocRef, "population", newPopulation);
                    return newPopulation;
                } else {
                    throw new FirebaseFirestoreException("Population too high",
                            FirebaseFirestoreException.Code.ABORTED);
                }
            }
        }).addOnSuccessListener(new OnSuccessListener<Double>() {
            @Override
            public void onSuccess(Double result) {
                Log.d(TAG, "Transaction success: " + result);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Transaction failure.", e);
            }
        });
        // [END transaction_with_result]
    }

    public void writeBatch() {
        // [START write_batch]
        // Get a new write batch
        WriteBatch batch = db.batch();

        // Set the value of 'NYC'
        DocumentReference nycRef = db.collection("cities").document("NYC");
        batch.set(nycRef, new City());

        // Update the population of 'SF'
        DocumentReference sfRef = db.collection("cities").document("SF");
        batch.update(sfRef, "population", 1000000L);

        // Delete the city 'LA'
        DocumentReference laRef = db.collection("cities").document("LA");
        batch.delete(laRef);

        // Commit the batch
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // ...
            }
        });
        // [END write_batch]
    }

    public void getDocument() {
        // [START get_document]
        DocumentReference docRef = db.collection("cities").document("SF");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        // [END get_document]
    }

    public void getDocumentWithOptions() {
        // [START get_document_options]
        DocumentReference docRef = db.collection("cities").document("SF");

        // Source can be CACHE, SERVER, or DEFAULT.
        Source source = Source.CACHE;

        // Get the document, forcing the SDK to use the offline cache
        docRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();
                    Log.d(TAG, "Cached document data: " + document.getData());
                } else {
                    Log.d(TAG, "Cached get failed: ", task.getException());
                }
            }
        });
        // [END get_document_options]
    }

    public void customObjects() {
        // [START custom_objects]
        DocumentReference docRef = db.collection("cities").document("BJ");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                City city = documentSnapshot.toObject(City.class);
            }
        });
        // [END custom_objects]
    }

    public void listenToDocument() {
        // [START listen_document]
        final DocumentReference docRef = db.collection("cities").document("SF");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
        // [END listen_document]
    }

    public void listenToDocumentLocal() {
        // [START listen_document_local]
        final DocumentReference docRef = db.collection("cities").document("SF");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                String source = snapshot != null && snapshot.getMetadata().hasPendingWrites()
                        ? "Local" : "Server";

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, source + " data: " + snapshot.getData());
                } else {
                    Log.d(TAG, source + " data: null");
                }
            }
        });
        // [END listen_document_local]
    }

    public void listenWithMetadata() {
        // [START listen_with_metadata]
        // Listen for metadata changes to the document.
        DocumentReference docRef = db.collection("cities").document("SF");
        docRef.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                // ...
            }
        });
        // [END listen_with_metadata]
    }

    public void getMultipleDocs() {
        // [START get_multiple]
        db.collection("cities")
                .whereEqualTo("capital", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        // [END get_multiple]
    }

    public void getAllDocs() {
        // [START get_multiple_all]
        db.collection("cities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        // [END get_multiple_all]
    }

    public void listenToMultiple() {
        // [START listen_multiple]
        db.collection("cities")
                .whereEqualTo("state", "CA")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        List<String> cities = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("name") != null) {
                                cities.add(doc.getString("name"));
                            }
                        }
                        Log.d(TAG, "Current cites in CA: " + cities);
                    }
                });
        // [END listen_multiple]
    }

    public void listenToDiffs() {
        // [START listen_diffs]
        db.collection("cities")
                .whereEqualTo("state", "CA")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Log.d(TAG, "New city: " + dc.getDocument().getData());
                                    break;
                                case MODIFIED:
                                    Log.d(TAG, "Modified city: " + dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                                    break;
                            }
                        }

                    }
                });
        // [END listen_diffs]
    }

    public void listenState() {
        // [START listen_state]
        db.collection("cities")
                .whereEqualTo("state", "CA")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            Log.w(TAG, "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            if (dc.getType() == Type.ADDED) {
                                Log.d(TAG, "New city: " + dc.getDocument().getData());
                            }
                        }

                        if (!snapshots.getMetadata().isFromCache()) {
                            Log.d(TAG, "Got initial state.");
                        }
                    }
                });
        // [END listen_state]
    }

    public void detachListener() {
        // [START detach_listener]
        Query query = db.collection("cities");
        ListenerRegistration registration = query.addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                    // [START_EXCLUDE]
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        // ...
                    }
                    // [END_EXCLUDE]
                });

        // ...

        // Stop listening to changes
        registration.remove();
        // [END detach_listener]
    }

    public void handleListenErrors() {
        // [START handle_listen_errors]
        db.collection("cities")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            if (dc.getType() == Type.ADDED) {
                                Log.d(TAG, "New city: " + dc.getDocument().getData());
                            }
                        }

                    }
                });
        // [END handle_listen_errors]
    }

    public void exampleData() {
        // [START example_data]
        CollectionReference cities = db.collection("cities");

        Map<String, Object> data1 = new HashMap<>();
        data1.put("name", "San Francisco");
        data1.put("state", "CA");
        data1.put("country", "USA");
        data1.put("capital", false);
        data1.put("population", 860000);
        data1.put("regions", Arrays.asList("west_coast", "norcal"));
        cities.document("SF").set(data1);

        Map<String, Object> data2 = new HashMap<>();
        data2.put("name", "Los Angeles");
        data2.put("state", "CA");
        data2.put("country", "USA");
        data2.put("capital", false);
        data2.put("population", 3900000);
        data2.put("regions", Arrays.asList("west_coast", "socal"));
        cities.document("LA").set(data2);

        Map<String, Object> data3 = new HashMap<>();
        data3.put("name", "Washington D.C.");
        data3.put("state", null);
        data3.put("country", "USA");
        data3.put("capital", true);
        data3.put("population", 680000);
        data3.put("regions", Arrays.asList("east_coast"));
        cities.document("DC").set(data3);

        Map<String, Object> data4 = new HashMap<>();
        data4.put("name", "Tokyo");
        data4.put("state", null);
        data4.put("country", "Japan");
        data4.put("capital", true);
        data4.put("population", 9000000);
        data4.put("regions", Arrays.asList("kanto", "honshu"));
        cities.document("TOK").set(data4);

        Map<String, Object> data5 = new HashMap<>();
        data5.put("name", "Beijing");
        data5.put("state", null);
        data5.put("country", "China");
        data5.put("capital", true);
        data5.put("population", 21500000);
        data5.put("regions", Arrays.asList("jingjinji", "hebei"));
        cities.document("BJ").set(data5);
        // [END example_data]
    }

    public void exampleDataCollectionGroup() {
        // [START fs_collection_group_query_data_setup]
        CollectionReference citiesRef = db.collection("cities");

        Map<String, Object> ggbData = new HashMap<>();
        ggbData.put("name", "Golden Gate Bridge");
        ggbData.put("type", "bridge");
        citiesRef.document("SF").collection("landmarks").add(ggbData);

        Map<String, Object> lohData = new HashMap<>();
        lohData.put("name", "Legion of Honor");
        lohData.put("type", "museum");
        citiesRef.document("SF").collection("landmarks").add(lohData);

        Map<String, Object> gpData = new HashMap<>();
        gpData.put("name", "Griffith Park");
        gpData.put("type", "park");
        citiesRef.document("LA").collection("landmarks").add(gpData);

        Map<String, Object> tgData = new HashMap<>();
        tgData.put("name", "The Getty");
        tgData.put("type", "museum");
        citiesRef.document("LA").collection("landmarks").add(tgData);

        Map<String, Object> lmData = new HashMap<>();
        lmData.put("name", "Lincoln Memorial");
        lmData.put("type", "memorial");
        citiesRef.document("DC").collection("landmarks").add(lmData);

        Map<String, Object> nasaData = new HashMap<>();
        nasaData.put("name", "National Air and Space Museum");
        nasaData.put("type", "museum");
        citiesRef.document("DC").collection("landmarks").add(nasaData);

        Map<String, Object> upData = new HashMap<>();
        upData.put("name", "Ueno Park");
        upData.put("type", "park");
        citiesRef.document("TOK").collection("landmarks").add(upData);

        Map<String, Object> nmData = new HashMap<>();
        nmData.put("name", "National Museum of Nature and Science");
        nmData.put("type", "museum");
        citiesRef.document("TOK").collection("landmarks").add(nmData);

        Map<String, Object> jpData = new HashMap<>();
        jpData.put("name", "Jingshan Park");
        jpData.put("type", "park");
        citiesRef.document("BJ").collection("landmarks").add(jpData);

        Map<String, Object> baoData = new HashMap<>();
        baoData.put("name", "Beijing Ancient Observatory");
        baoData.put("type", "museum");
        citiesRef.document("BJ").collection("landmarks").add(baoData);
        // [END fs_collection_group_query_data_setup]
    }

    public void simpleQueries() {
        // [START simple_queries]
        // Create a reference to the cities collection
        CollectionReference citiesRef = db.collection("cities");

        // Create a query against the collection.
        Query query = citiesRef.whereEqualTo("state", "CA");
        // [END simple_queries]

        // [START simple_query_capital]
        Query capitalCities = db.collection("cities").whereEqualTo("capital", true);
        // [END simple_query_capital]

        // [START example_filters]
        citiesRef.whereEqualTo("state", "CA");
        citiesRef.whereLessThan("population", 100000);
        citiesRef.whereGreaterThanOrEqualTo("name", "San Francisco");
        // [END example_filters]

        // [START simple_query_not_equal]
        citiesRef.whereNotEqualTo("capital", false);
        // [END simple_query_not_equal]
    }

    public void arrayContainsQueries() {
        // [START array_contains_filter]
        CollectionReference citiesRef = db.collection("cities");

        citiesRef.whereArrayContains("regions", "west_coast");
        // [END array_contains_filter]
    }

    public void arrayContainsAnyQueries() {
        // [START array_contains_any_filter]
        CollectionReference citiesRef = db.collection("cities");

        citiesRef.whereArrayContainsAny("regions", Arrays.asList("west_coast", "east_coast"));
        // [END array_contains_any_filter]
    }

    public void inQueries() {
        // [START in_filter]
        CollectionReference citiesRef = db.collection("cities");

        citiesRef.whereIn("country", Arrays.asList("USA", "Japan"));
        // [END in_filter]

        // [START not_in_filter]
        citiesRef.whereNotIn("country", Arrays.asList("USA", "Japan"));
        // [END not_in_filter]

        // [START in_filter_with_array]
        citiesRef.whereIn("regions", Arrays.asList(new String[]{"west_coast"}, new String[]{"east_coast"}));
        // [END in_filter_with_array]
    }

    public void compoundQueries() {
        CollectionReference citiesRef = db.collection("cities");

        // [START chain_filters]
        citiesRef.whereEqualTo("state", "CO").whereEqualTo("name", "Denver");
        citiesRef.whereEqualTo("state", "CA").whereLessThan("population", 1000000);
        // [END chain_filters]

        // [START valid_range_filters]
        citiesRef.whereGreaterThanOrEqualTo("state", "CA")
                .whereLessThanOrEqualTo("state", "IN");
        citiesRef.whereEqualTo("state", "CA")
                .whereGreaterThan("population", 1000000);
        // [END valid_range_filters]
    }

    public void compoundQueriesInvalid() {
        CollectionReference citiesRef = db.collection("cities");

        // [START invalid_range_filters]
        citiesRef.whereGreaterThanOrEqualTo("state", "CA").whereGreaterThan("population", 100000);
        // [END invalid_range_filters]
    }

    public void orderAndLimit() {
        CollectionReference citiesRef = db.collection("cities");

        // [START order_and_limit]
        citiesRef.orderBy("name").limit(3);
        // [END order_and_limit]

        // [START order_and_limit_desc]
        citiesRef.orderBy("name", Direction.DESCENDING).limit(3);
        // [END order_and_limit_desc]

        // [START order_by_multiple]
        citiesRef.orderBy("state").orderBy("population", Direction.DESCENDING);
        // [END order_by_multiple]

        // [START filter_and_order]
        citiesRef.whereGreaterThan("population", 100000).orderBy("population").limit(2);
        // [END filter_and_order]

        // [START valid_filter_and_order]
        citiesRef.whereGreaterThan("population", 100000).orderBy("population");
        // [END valid_filter_and_order]
    }

    public void orderAndLimitInvalid() {
        CollectionReference citiesRef = db.collection("cities");

        // [START invalid_filter_and_order]
        citiesRef.whereGreaterThan("population", 100000).orderBy("country");
        // [END invalid_filter_and_order]
    }

    public void queryStartAtEndAt() {
        // [START query_start_at_single]
        // Get all cities with a population >= 1,000,000, ordered by population,
        db.collection("cities")
                .orderBy("population")
                .startAt(1000000);
        // [END query_start_at_single]

        // [START query_end_at_single]
        // Get all cities with a population <= 1,000,000, ordered by population,
        db.collection("cities")
                .orderBy("population")
                .endAt(1000000);
        // [END query_end_at_single]

        // [START query_start_at_doc_snapshot]
        // Get the data for "San Francisco"
        db.collection("cities").document("SF")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // Get all cities with a population bigger than San Francisco.
                        Query biggerThanSf = db.collection("cities")
                                .orderBy("population")
                                .startAt(documentSnapshot);

                        // ...
                    }
                });
        // [END query_start_at_doc_snapshot]

        // [START query_pagination]
        // Construct query for first 25 cities, ordered by population
        Query first = db.collection("cities")
                .orderBy("population")
                .limit(25);

        first.get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {
                    // ...

                    // Get the last visible document
                    DocumentSnapshot lastVisible = documentSnapshots.getDocuments()
                            .get(documentSnapshots.size() -1);

                    // Construct a new query starting at this document,
                    // get the next 25 cities.
                    Query next = db.collection("cities")
                            .orderBy("population")
                            .startAfter(lastVisible)
                            .limit(25);

                    // Use the query for pagination
                    // ...
                }
            });
        // [END query_pagination]

        // [START multi_cursor]
        // Will return all Springfields
        db.collection("cities")
                .orderBy("name")
                .orderBy("state")
                .startAt("Springfield");

        // Will return "Springfield, Missouri" and "Springfield, Wisconsin"
        db.collection("cities")
                .orderBy("name")
                .orderBy("state")
                .startAt("Springfield", "Missouri");
        // [END multi_cursor]
    }

    public void collectionGroupQuery() {
        // [START fs_collection_group_query]
        db.collectionGroup("landmarks").whereEqualTo("type", "museum").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // [START_EXCLUDE]
                        for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                            Log.d(TAG, snap.getId() + " => " + snap.getData());
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END fs_collection_group_query]
    }

    // [START delete_collection]
    /**
     * Delete all documents in a collection. Uses an Executor to perform work on a background
     * thread. This does *not* automatically discover and delete subcollections.
     */
    private Task<Void> deleteCollection(final CollectionReference collection,
                                        final int batchSize,
                                        Executor executor) {

        // Perform the delete operation on the provided Executor, which allows us to use
        // simpler synchronous logic without blocking the main thread.
        return Tasks.call(executor, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                // Get the first batch of documents in the collection
                Query query = collection.orderBy(FieldPath.documentId()).limit(batchSize);

                // Get a list of deleted documents
                List<DocumentSnapshot> deleted = deleteQueryBatch(query);

                // While the deleted documents in the last batch indicate that there
                // may still be more documents in the collection, page down to the
                // next batch and delete again
                while (deleted.size() >= batchSize) {
                    // Move the query cursor to start after the last doc in the batch
                    DocumentSnapshot last = deleted.get(deleted.size() - 1);
                    query = collection.orderBy(FieldPath.documentId())
                            .startAfter(last.getId())
                            .limit(batchSize);

                    deleted = deleteQueryBatch(query);
                }

                return null;
            }
        });

    }

    /**
     * Delete all results from a query in a single WriteBatch. Must be run on a worker thread
     * to avoid blocking/crashing the main thread.
     */
    @WorkerThread
    private List<DocumentSnapshot> deleteQueryBatch(final Query query) throws Exception {
        QuerySnapshot querySnapshot = Tasks.await(query.get());

        WriteBatch batch = query.getFirestore().batch();
        for (QueryDocumentSnapshot snapshot : querySnapshot) {
            batch.delete(snapshot.getReference());
        }
        Tasks.await(batch.commit());

        return querySnapshot.getDocuments();
    }
    // [END delete_collection]

    public void toggleOffline() {
        // [START disable_network]
        db.disableNetwork()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Do offline things
                        // ...
                    }
                });
        // [END disable_network]

        // [START enable_network]
        db.enableNetwork()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Do online things
                        // ...
                    }
                });
        // [END enable_network]
    }

    public void offlineListen(FirebaseFirestore db) {
        // [START offline_listen]
        db.collection("cities").whereEqualTo("state", "CA")
                .addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen error", e);
                            return;
                        }

                        for (DocumentChange change : querySnapshot.getDocumentChanges()) {
                            if (change.getType() == Type.ADDED) {
                                Log.d(TAG, "New city:" + change.getDocument().getData());
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(TAG, "Data fetched from " + source);
                        }

                    }
                });
        // [END offline_listen]
    }


    // [START server_timestamp_annotation]
    public class MyObject {

        public String name;
        public @ServerTimestamp Date timestamp;

        public MyObject() {}
    }
    // [END server_timestamp_annotation]

    public void updateWithServerTimestamp() {
        // [START update_with_server_timestamp]
        DocumentReference docRef = db.collection("objects").document("some-id");

        // Update the timestamp field with the value from the server
        Map<String,Object> updates = new HashMap<>();
        updates.put("timestamp", FieldValue.serverTimestamp());

        docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            // [START_EXCLUDE]
            @Override
            public void onComplete(@NonNull Task<Void> task) {}
            // [START_EXCLUDE]
        });
        // [END update_with_server_timestamp]
    }

    public void updateDeleteField() {
        // [START update_delete_field]
        DocumentReference docRef = db.collection("cities").document("BJ");

        // Remove the 'capital' field from the document
        Map<String,Object> updates = new HashMap<>();
        updates.put("capital", FieldValue.delete());

        docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            // [START_EXCLUDE]
            @Override
            public void onComplete(@NonNull Task<Void> task) {}
            // [START_EXCLUDE]
        });
        // [END update_delete_field]
    }
}
