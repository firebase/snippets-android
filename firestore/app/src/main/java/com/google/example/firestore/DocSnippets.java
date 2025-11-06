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
import com.google.firebase.firestore.AggregateField;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentChange.Type;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MemoryCacheSettings;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.PersistentCacheSettings;
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
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Pipeline;
import com.google.firebase.firestore.PipelineResult;
import com.google.firebase.firestore.PipelineSource;
import com.google.firebase.firestore.pipeline.AggregateFunction;
import com.google.firebase.firestore.pipeline.AggregateStage;
import com.google.firebase.firestore.pipeline.Expression;
import com.google.firebase.firestore.pipeline.SampleStage;
import com.google.firebase.firestore.pipeline.UnnestOptions;

import static com.google.firebase.firestore.pipeline.Expression.field;
import static com.google.firebase.firestore.pipeline.Expression.constant;

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

    public void setup() {
        // [START get_firestore_instance]
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // [END get_firestore_instance]

        // [START set_firestore_settings]
        FirebaseFirestoreSettings settings = 
        new FirebaseFirestoreSettings.Builder(db.getFirestoreSettings())
            // Use memory-only cache
            .setLocalCacheSettings(MemoryCacheSettings.newBuilder().build())
            // Use persistent disk cache (default)
            .setLocalCacheSettings(PersistentCacheSettings.newBuilder()
                                    .build())
            .build();
        db.setFirestoreSettings(settings);
        // [END set_firestore_settings]
    }

    public void setupCacheSize() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // [START fs_setup_cache]
        FirebaseFirestoreSettings settings = 
        new FirebaseFirestoreSettings.Builder(db.getFirestoreSettings())
            .setLocalCacheSettings(PersistentCacheSettings.newBuilder()
                                    // Set size to 100 MB
                                    .setSizeBytes(1024 * 1024 * 100)
                                    .build())
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
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
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
            public Double apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
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

    public void getAllDocsSubcollection() {
        // [START firestore_query_subcollection]
        db.collection("cities")
                .document("SF")
                .collection("landmarks")
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
        // [END firestore_query_subcollection]
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
        Query stateQuery = citiesRef.whereEqualTo("state", "CA");
        Query populationQuery = citiesRef.whereLessThan("population", 100000);
        Query nameQuery = citiesRef.whereGreaterThanOrEqualTo("name", "San Francisco");
        // [END example_filters]

        // [START simple_query_not_equal]
        Query notCapitalQuery = citiesRef.whereNotEqualTo("capital", false);
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

    public void countAggregateCollection() {
        // [START count_aggregate_collection]
        Query query = db.collection("cities");
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Count fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    Log.d(TAG, "Count: " + snapshot.getCount());
                } else {
                    Log.d(TAG, "Count failed: ", task.getException());
                }
            }
        });
        // [END count_aggregate_collection]
    }

    public void countAggregateQuery() {
        // [START count_aggregate_query]
        Query query = db.collection("cities").whereEqualTo("state", "CA");
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Count fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    Log.d(TAG, "Count: " + snapshot.getCount());
                } else {
                    Log.d(TAG, "Count failed: ", task.getException());
                }
            }
        });
        // [END count_aggregate_query]
    }

    public void orQuery() {
        CollectionReference collection = db.collection("cities");
        // [START or_queries]
        Query query = collection.where(Filter.and(
            Filter.equalTo("state", "CA"),
            Filter.or(
                Filter.equalTo("capital", true),
                Filter.greaterThanOrEqualTo("population", 1000000)
            )
        ));
        // [END or_queries]
    }

    public void orQueryDisjunctions() {
        CollectionReference collection = db.collection("cities");

        // [START one_disjunction]
        collection.whereEqualTo("a", 1);
        // [END one_disjunction]

        // [START two_disjunctions]
        collection.where(Filter.or(
            Filter.equalTo("a", 1),
            Filter.equalTo("b", 2)
        ));
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
        ));
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
        ));
        // [END four_disjunctions_compact]

        // [START 20_disjunctions]
        collection.where(Filter.or(
            Filter.inArray("a", Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)),
            Filter.inArray("b", Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        ));
        // [END 20_disjunctions]

        // [START 10_disjunctions]
        collection.where(Filter.and(
            Filter.inArray("a", Arrays.asList(1, 2, 3, 4, 5)),
            Filter.or(
                Filter.equalTo("b", 2),
                Filter.equalTo("c", 3)
            )
        ));
        // [END 10_disjunctions]
    }

    public void illegalDisjunctions() {
        CollectionReference collection = db.collection("cities");
        // [START 50_disjunctions]
        collection.where(Filter.and(
            Filter.inArray("a", Arrays.asList(1, 2, 3, 4, 5)),
            Filter.inArray("b", Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        ));
        // [END 50_disjunctions]
    }

    public void sumAggregateCollection() {
        // [START sum_aggregate_collection]
        Query query = db.collection("cities");
        AggregateQuery aggregateQuery = query.aggregate(AggregateField.sum("population"));
        aggregateQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Aggregate fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    Log.d(TAG, "Sum: " + snapshot.get(AggregateField.sum("population")));
                } else {
                    Log.d(TAG, "Aggregation failed: ", task.getException());
                }
            }
        });
        // [END sum_aggregate_collection]
    }

    public void sumAggregateQuery() {
        // [START sum_aggregate_query]
        Query query = db.collection("cities").whereEqualTo("capital", true);
        AggregateQuery aggregateQuery = query.aggregate(AggregateField.sum("population"));
        aggregateQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Aggregate fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    Log.d(TAG, "Sum: " + snapshot.get(AggregateField.sum("population")));
                } else {
                    Log.d(TAG, "Aggregation failed: ", task.getException());
                }
            }
        });
        // [END sum_aggregate_query]
    }

    public void averageAggregateCollection() {
        // [START average_aggregate_collection]
        Query query = db.collection("cities");
        AggregateQuery aggregateQuery = query.aggregate(AggregateField.average("population"));
        aggregateQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Aggregate fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    Log.d(TAG, "Average: " + snapshot.get(AggregateField.average("population")));
                } else {
                    Log.d(TAG, "Aggregation failed: ", task.getException());
                }
            }
        });
        // [END average_aggregate_collection]
    }

    public void averageAggregateQuery() {
        // [START average_aggregate_query]
        Query query = db.collection("cities").whereEqualTo("capital", true);
        AggregateQuery aggregateQuery = query.aggregate(AggregateField.average("population"));
        aggregateQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Aggregate fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    Log.d(TAG, "Average: " + snapshot.get(AggregateField.average("population")));
                } else {
                    Log.d(TAG, "Aggregation failed: ", task.getException());
                }
            }
        });
        // [END average_aggregate_query]
    }

    public void multiAggregateQuery() {
        // [START multi_aggregate_query]
        Query query = db.collection("cities");
        AggregateQuery aggregateQuery = query.aggregate(
                AggregateField.count(),
                AggregateField.sum("population"),
                AggregateField.average("population"));
        aggregateQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Aggregate fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    Log.d(TAG, "Count: " + snapshot.get(AggregateField.count()));
                    Log.d(TAG, "Sum: " + snapshot.get(AggregateField.sum("population")));
                    Log.d(TAG, "Average: " + snapshot.get(AggregateField.average("population")));
                } else {
                    Log.d(TAG, "Aggregation failed: ", task.getException());
                }
            }
        });
        // [END multi_aggregate_query]
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#concepts
    void pipelineConcepts() {
        // [START pipeline_concepts]
        Pipeline pipeline = db.pipeline()
            // Step 1: Start a query with collection scope
            .collection("cities")
            // Step 2: Filter the collection
            .where(field("population").greaterThan(100000))
            // Step 3: Sort the remaining documents
            .sort(field("name").ascending())
            // Step 4: Return the top 10. Note applying the limit earlier in the pipeline would have
            // unintentional results.
            .limit(10);
        // [END pipeline_concepts]
        System.out.println(pipeline);
    }

    void basicPipelineRead() {
        // [START basic_pipeline_read]
        Pipeline readDataPipeline = db.pipeline()
            .collection("users");

        readDataPipeline.execute()
            .addOnSuccessListener(new OnSuccessListener<Pipeline.Snapshot>() {
                @Override
                public void onSuccess(Pipeline.Snapshot snapshot) {
                    for (PipelineResult result : snapshot.getResults()) {
                        System.out.println(result.getId() + " => " + result.getData());
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("Error getting documents: " + e);
                }
            });
        // [END basic_pipeline_read]
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#initialization
    void pipelineInitialization() {
        // [START pipeline_initialization]
        FirebaseFirestore firestore = FirebaseFirestore.getInstance("enterprise");
        PipelineSource pipeline = firestore.pipeline();
        // [END pipeline_initialization]
        System.out.println(pipeline);
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#field_vs_constant_references/
    void fieldVsConstants() {
        // [START field_or_constant]
        Pipeline pipeline = db.pipeline()
            .collection("cities")
            .where(field("name").equal(constant("Toronto")));
        // [END field_or_constant]
        System.out.println(pipeline);
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#input_stages
    void inputStages() {
        // [START input_stages]
        Task<Pipeline.Snapshot> results;

        // Return all restaurants in San Francisco
        results = db.pipeline().collection("cities/sf/restaurants").execute();

        // Return all restaurants
        results = db.pipeline().collectionGroup("restaurants").execute();

        // Return all documents across all collections in the database (the entire database)
        results = db.pipeline().database().execute();

        // Batch read of 3 documents
        results = db.pipeline().documents(
            db.collection("cities").document("SF"),
            db.collection("cities").document("DC"),
            db.collection("cities").document("NY")
        ).execute();
        // [END input_stages]
        System.out.println(results);
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#where
    void wherePipeline() {
        // [START pipeline_where]
        Task<Pipeline.Snapshot> results;

        results = db.pipeline().collection("books")
            .where(field("rating").equal(5))
            .where(field("published").lessThan(1900))
            .execute();

        results = db.pipeline().collection("books")
            .where(Expression.and(
                field("rating").equal(5),
                field("published").lessThan(1900)
            ))
            .execute();
        // [END pipeline_where]
        System.out.println(results);
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#aggregate_distinct
    void aggregateGroups() {
        // [START aggregate_groups]
        Task<Pipeline.Snapshot> results = db.pipeline()
            .collection("books")
            .aggregate(AggregateStage
                .withAccumulators(
                    AggregateFunction.average("rating").alias("avg_rating"))
                .withGroups(field("genre")))
            .execute();
        // [END aggregate_groups]
        System.out.println(results);
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#aggregate_distinct
    void aggregateDistinct() {
        // [START aggregate_distinct]
        Task<Pipeline.Snapshot> results = db.pipeline()
            .collection("books")
            .distinct(
                field("author").toUpper().alias("author"),
                field("genre")
            )
            .execute();
        // [END aggregate_distinct]
        System.out.println(results);
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#sort
    void sort() {
        // [START sort]
        Task<Pipeline.Snapshot> results = db.pipeline()
            .collection("books")
            .sort(
                field("release_date").descending(),
                field("author").ascending()
            )
            .execute();
        // [END sort]
        System.out.println(results);
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#sort
    void sortComparison() {
        // [START sort_comparison]
        Query query = db.collection("cities")
            .orderBy("state")
            .orderBy("population", Query.Direction.DESCENDING);

        Pipeline pipeline = db.pipeline()
            .collection("books")
            .sort(
                field("release_date").descending(),
                field("author").ascending()
            );
        // [END sort_comparison]
        System.out.println(query);
        System.out.println(pipeline);
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#functions
    void functions() {
        // [START functions_example]
        Task<Pipeline.Snapshot> results;

        // Type 1: Scalar (for use in non-aggregation stages)
        // Example: Return the min store price for each book.
        results = db.pipeline().collection("books")
            .select(
                field("current").logicalMinimum("updated").alias("price_min")
            )
            .execute();

        // Type 2: Aggregation (for use in aggregate stages)
        // Example: Return the min price of all books.
        results = db.pipeline().collection("books")
            .aggregate(AggregateFunction.minimum("price").alias("min_price"))
            .execute();
        // [END functions_example]
        System.out.println(results);
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#creating_indexes
    void creatingIndexes() {
        // [START query_example]
        Task<Pipeline.Snapshot> results = db.pipeline()
            .collection("books")
            .where(field("published").lessThan(1900))
            .where(field("genre").equal("Science Fiction"))
            .where(field("rating").greaterThan(4.3))
            .sort(field("published").descending())
            .execute();
        // [END query_example]
        System.out.println(results);
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#existing_sparse_indexes
    void sparseIndexes() {
        // [START sparse_index_example]
        Task<Pipeline.Snapshot> results = db.pipeline()
            .collection("books")
            .where(field("category").like("%fantasy%"))
            .execute();
        // [END sparse_index_example]
        System.out.println(results);
    }

    void sparseIndexes2() {
        // [START sparse_index_example_2]
        Task<Pipeline.Snapshot> results = db.pipeline()
            .collection("books")
            .sort(field("release_date").ascending())
            .execute();
        // [END sparse_index_example_2]
        System.out.println(results);
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#covered_queries_secondary_indexes
    void coveredQuery() {
        // [START covered_query]
        Task<Pipeline.Snapshot> results = db.pipeline()
            .collection("books")
            .where(field("category").like("%fantasy%"))
            .where(field("title").exists())
            .where(field("author").exists())
            .select(field("title"), field("author"))
            .execute();
        // [END covered_query]
        System.out.println(results);
    }

    // https://cloud.google.com/firestore/docs/pipeline/overview#pagination
    void pagination() {
        // [START pagination_not_supported_preview]
        // Existing pagination via `startAt()`
        Query query = db.collection("cities").orderBy("population").startAt(1000000);

        // Private preview workaround using pipelines
        Pipeline pipeline = db.pipeline()
            .collection("cities")
            .where(field("population").greaterThanOrEqual(1000000))
            .sort(field("population").descending());
        // [END pagination_not_supported_preview]
        System.out.println(query);
        System.out.println(pipeline);
    }

    // http://cloud.google.com/firestore/docs/pipeline/stages/input/collection#example
    void collectionStage() {
        // [START collection_example]
        Task<Pipeline.Snapshot> results = db.pipeline()
            .collection("users/bob/games")
            .sort(field("name").ascending())
            .execute();
        // [END collection_example]
        System.out.println(results);
    }

    // https://cloud.google.com/firestore/docs/pipeline/stages/input/collection_group
    void collectionGroupStage() {
        // [START collection_group_example]
        Task<Pipeline.Snapshot> results = db.pipeline()
            .collectionGroup("games")
            .sort(field("name").ascending())
            .execute();
        // [END collection_group_example]
        System.out.println(results);
    }

    // https://cloud.google.com/firestore/docs/pipeline/stages/input/database
    void databaseStage() {
        // [START database_example]
        // Count all documents in the database
        Task<Pipeline.Snapshot> results = db.pipeline()
            .database()
            .aggregate(AggregateFunction.countAll().alias("total"))
            .execute();
        // [END database_example]
        System.out.println(results);
    }

    // https://cloud.google.com/firestore/docs/pipeline/stages/input/documents
    void documentsStage() {
        // [START documents_example]
        Task<Pipeline.Snapshot> results = db.pipeline()
            .documents(
                db.collection("cities").document("SF"),
                db.collection("cities").document("DC"),
                db.collection("cities").document("NY")
            ).execute();
        // [END documents_example]
        System.out.println(results);
    }

    void replaceWithStage() {
        // [START initial_data]
        db.collection("cities").document("SF").set(new HashMap<String, Object>() {{
            put("name", "San Francisco");
            put("population", 800000);
            put("location", new HashMap<String, Object>() {{
                put("country", "USA");
                put("state", "California");
            }});
        }});
        db.collection("cities").document("TO").set(new HashMap<String, Object>() {{
            put("name", "Toronto");
            put("population", 3000000);
            put("province", "ON");
            put("location", new HashMap<String, Object>() {{
                put("country", "Canada");
                put("province", "Ontario");
            }});
        }});
        db.collection("cities").document("NY").set(new HashMap<String, Object>() {{
            put("name", "New York");
            put("location", new HashMap<String, Object>() {{
                put("country", "USA");
                put("state", "New York");
            }});
        }});
        db.collection("cities").document("AT").set(new HashMap<String, Object>() {{
            put("name", "Atlantis");
        }});
        // [END initial_data]

        // [START full_replace]
        Task<Pipeline.Snapshot> names = db.pipeline()
            .collection("cities")
            .replaceWith("location")
            .execute();
        // [END full_replace]

        // [START map_merge_overwrite]
        // unsupported in client SDKs for now
        // [END map_merge_overwrite]
        System.out.println(names);
    }

    // https://cloud.google.com/firestore/docs/pipeline/stages/transformation/sample#examples
    void sampleStage() {
        // [START sample_example]
        Task<Pipeline.Snapshot> results;

        // Get a sample of 100 documents in a database
        results = db.pipeline()
            .database()
            .sample(100)
            .execute();

        // Randomly shuffle a list of 3 documents
        results = db.pipeline()
            .documents(
                db.collection("cities").document("SF"),
                db.collection("cities").document("NY"),
                db.collection("cities").document("DC")
            )
            .sample(3)
            .execute();
        // [END sample_example]
        System.out.println(results);
    }

    // https://cloud.google.com/firestore/docs/pipeline/stages/transformation/sample#examples_2
    void samplePercent() {
        // [START sample_percent]
        // Get a sample of on average 50% of the documents in the database
        Task<Pipeline.Snapshot> results = db.pipeline()
            .database()
            .sample(SampleStage.withPercentage(0.5))
            .execute();
        // [END sample_percent]
        System.out.println(results);
    }

    // https://cloud.google.com/firestore/docs/pipeline/stages/transformation/union#examples
    void unionStage() {
        // [START union_stage]
        Task<Pipeline.Snapshot> results = db.pipeline()
            .collection("cities/SF/restaurants")
            .where(field("type").equal("Chinese"))
            .union(db.pipeline()
                .collection("cities/NY/restaurants")
                .where(field("type").equal("Italian")))
            .where(field("rating").greaterThanOrEqual(4.5))
            .sort(field("__name__").descending())
            .execute();
        // [END union_stage]
        System.out.println(results);
    }

    // https://cloud.google.com/firestore/docs/pipeline/stages/transformation/union#examples
    void unionStageStable() {
        // [START union_stage_stable]
        Task<Pipeline.Snapshot> results = db.pipeline()
            .collection("cities/SF/restaurants")
            .where(field("type").equal("Chinese"))
            .union(db.pipeline()
                .collection("cities/NY/restaurants")
                .where(field("type").equal("Italian")))
            .where(field("rating").greaterThanOrEqual(4.5))
            .sort(field("__name__").descending())
            .execute();
        // [END union_stage_stable]
        System.out.println(results);
    }

    // https://cloud.google.com/firestore/docs/pipeline/stages/transformation/unnest#examples
    void unnestStage() {
        // [START unnest_stage]
        Task<Pipeline.Snapshot> results = db.pipeline()
            .database()
            .unnest(field("arrayField").alias("unnestedArrayField"), new UnnestOptions().withIndexField("index"))
            .execute();
        // [END unnest_stage]
        System.out.println(results);
    }

    // https://cloud.google.com/firestore/docs/pipeline/stages/transformation/unnest#examples
    void unnestStageEmptyOrNonArray() {
        // [START unnest_edge_cases]
        // Input
        // { identifier : 1, neighbors: [ "Alice", "Cathy" ] }
        // { identifier : 2, neighbors: []                   }
        // { identifier : 3, neighbors: "Bob"                }

        Task<Pipeline.Snapshot> results = db.pipeline()
            .database()
            .unnest(field("neighbors").alias("unnestedNeighbors"), new UnnestOptions().withIndexField("index"))
            .execute();

        // Output
        // { identifier: 1, neighbors: [ "Alice", "Cathy" ], unnestedNeighbors: "Alice", index: 0 }
        // { identifier: 1, neighbors: [ "Alice", "Cathy" ], unnestedNeighbors: "Cathy", index: 1 }
        // { identifier: 3, neighbors: "Bob", index: null}
        // [END unnest_edge_cases]
        System.out.println(results);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/aggregate_functions#count
    void countFunction() {
        // [START count_function]
        // Total number of books in the collection
        Task<Pipeline.Snapshot> countAll = db.pipeline()
            .collection("books")
            .aggregate(AggregateFunction.countAll().alias("count"))
            .execute();

        // Number of books with nonnull `ratings` field
        Task<Pipeline.Snapshot> countField = db.pipeline()
            .collection("books")
            .aggregate(AggregateFunction.count("ratings").alias("count"))
            .execute();
        // [END count_function]
        System.out.println(countAll);
        System.out.println(countField);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/aggregate_functions#count_if
    void countIfFunction() {
        // [START count_if]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .aggregate(
                AggregateFunction.countIf(field("rating").greaterThan(4)).alias("filteredCount")
            )
            .execute();
        // [END count_if]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/aggregate_functions#count_distinct
    void countDistinctFunction() {
        // [START count_distinct]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .aggregate(AggregateFunction.countDistinct("author").alias("unique_authors"))
            .execute();
        // [END count_distinct]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/aggregate_functions#sum
    void sumFunction() {
        // [START sum_function]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("cities")
            .aggregate(AggregateFunction.sum("population").alias("totalPopulation"))
            .execute();
        // [END sum_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/aggregate_functions#avg
    void avgFunction() {
        // [START avg_function]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("cities")
            .aggregate(AggregateFunction.average("population").alias("averagePopulation"))
            .execute();
        // [END avg_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/aggregate_functions#min
    void minFunction() {
        // [START min_function]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .aggregate(AggregateFunction.minimum("price").alias("minimumPrice"))
            .execute();
        // [END min_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/aggregate_functions#max
    void maxFunction() {
        // [START max_function]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .aggregate(AggregateFunction.maximum("price").alias("maximumPrice"))
            .execute();
        // [END max_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#add
    void addFunction() {
        // [START add_function]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(Expression.add(field("soldBooks"), field("unsoldBooks")).alias("totalBooks"))
            .execute();
        // [END add_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#subtract
    void subtractFunction() {
        // [START subtract_function]
        int storeCredit = 7;
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(Expression.subtract(field("price"), storeCredit).alias("totalCost"))
            .execute();
        // [END subtract_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#multiply
    void multiplyFunction() {
        // [START multiply_function]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(Expression.multiply(field("price"), field("soldBooks")).alias("revenue"))
            .execute();
        // [END multiply_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#divide
    void divideFunction() {
        // [START divide_function]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(Expression.divide(field("ratings"), field("soldBooks")).alias("reviewRate"))
            .execute();
        // [END divide_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#mod
    void modFunction() {
        // [START mod_function]
        int displayCapacity = 1000;
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(Expression.mod(field("unsoldBooks"), displayCapacity).alias("warehousedBooks"))
            .execute();
        // [END mod_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#ceil
    void ceilFunction() {
        // [START ceil_function]
        int booksPerShelf = 100;
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(
                Expression.divide(field("unsoldBooks"), booksPerShelf).ceil().alias("requiredShelves")
            )
            .execute();
        // [END ceil_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#floor
    void floorFunction() {
        // [START floor_function]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .addFields(
                Expression.divide(field("wordCount"), field("pages")).floor().alias("wordsPerPage")
            )
            .execute();
        // [END floor_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#round
    void roundFunction() {
        // [START round_function]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(Expression.multiply(field("soldBooks"), field("price")).round().alias("partialRevenue"))
            .aggregate(AggregateFunction.sum("partialRevenue").alias("totalRevenue"))
            .execute();
        // [END round_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#pow
    void powFunction() {
        // [START pow_function]
        GeoPoint googleplex = new GeoPoint(37.4221, -122.0853);
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("cities")
            .addFields(
                field("lat").subtract(googleplex.getLatitude())
                    .multiply(111 /* km per degree */)
                    .pow(2)
                    .alias("latitudeDifference"),
                field("lng").subtract(googleplex.getLongitude())
                    .multiply(111 /* km per degree */)
                    .pow(2)
                    .alias("longitudeDifference")
            )
            .select(
                field("latitudeDifference").add(field("longitudeDifference")).sqrt()
                    // Inaccurate for large distances or close to poles
                    .alias("approximateDistanceToGoogle")
            )
            .execute();
        // [END pow_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#sqrt
    void sqrtFunction() {
        // [START sqrt_function]
        GeoPoint googleplex = new GeoPoint(37.4221, -122.0853);
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("cities")
            .addFields(
                field("lat").subtract(googleplex.getLatitude())
                    .multiply(111 /* km per degree */)
                    .pow(2)
                    .alias("latitudeDifference"),
                field("lng").subtract(googleplex.getLongitude())
                    .multiply(111 /* km per degree */)
                    .pow(2)
                    .alias("longitudeDifference")
            )
            .select(
                field("latitudeDifference").add(field("longitudeDifference")).sqrt()
                    // Inaccurate for large distances or close to poles
                    .alias("approximateDistanceToGoogle")
            )
            .execute();
        // [END sqrt_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#exp
    void expFunction() {
        // [START exp_function]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(field("rating").exp().alias("expRating"))
            .execute();
        // [END exp_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#ln
    void lnFunction() {
        // [START ln_function]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(field("rating").ln().alias("lnRating"))
            .execute();
        // [END ln_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/arithmetic_functions#log
    void logFunction() {
        // [START log_function]
        // Not supported on Android
        // [END log_function]
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/array_functions#array_concat
    void arrayConcat() {
        // [START array_concat]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(field("genre").arrayConcat(field("subGenre")).alias("allGenres"))
            .execute();
        // [END array_concat]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/array_functions#array_contains
    void arrayContains() {
        // [START array_contains]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(field("genre").arrayContains("mystery").alias("isMystery"))
            .execute();
        // [END array_contains]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/array_functions#array_contains_all
    void arrayContainsAll() {
        // [START array_contains_all]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(
                field("genre")
                    .arrayContainsAll(Arrays.asList("fantasy", "adventure"))
                    .alias("isFantasyAdventure")
            )
            .execute();
        // [END array_contains_all]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/array_functions#array_contains_any
    void arrayContainsAny() {
        // [START array_contains_any]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(
                field("genre")
                    .arrayContainsAny(Arrays.asList("fantasy", "nonfiction"))
                    .alias("isMysteryOrFantasy")
            )
            .execute();
        // [END array_contains_any]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/array_functions#array_length
    void arrayLength() {
        // [START array_length]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(field("genre").arrayLength().alias("genreCount"))
            .execute();
        // [END array_length]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/array_functions#array_reverse
    void arrayReverse() {
        // [START array_reverse]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(field("genre").arrayReverse().alias("reversedGenres"))
            .execute();
        // [END array_reverse]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/comparison_functions#eq
    void equalFunction() {
        // [START equal_function]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(field("rating").equal(5).alias("hasPerfectRating"))
            .execute();
        // [END equal_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/comparison_functions#gt
    void greaterThanFunction() {
        // [START greater_than]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(field("rating").greaterThan(4).alias("hasHighRating"))
            .execute();
        // [END greater_than]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/comparison_functions#gte
    void greaterThanOrEqualToFunction() {
        // [START greater_or_equal]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(field("published").greaterThanOrEqual(1900).alias("publishedIn20thCentury"))
            .execute();
        // [END greater_or_equal]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/comparison_functions#lt
    void lessThanFunction() {
        // [START less_than]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(field("published").lessThan(1923).alias("isPublicDomainProbably"))
            .execute();
        // [END less_than]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/comparison_functions#lte
    void lessThanOrEqualToFunction() {
        // [START less_or_equal]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(field("rating").lessThanOrEqual(2).alias("hasBadRating"))
            .execute();
        // [END less_or_equal]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/comparison_functions#neq
    void notEqualFunction() {
        // [START not_equal]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(field("title").notEqual("1984").alias("not1984"))
            .execute();
        // [END not_equal]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/debugging_functions#exists
    void existsFunction() {
        // [START exists_function]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(field("rating").exists().alias("hasRating"))
            .execute();
        // [END exists_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/logical_functions#and
    void andFunction() {
        // [START and_function]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(
                Expression.and(
                    field("rating").greaterThan(4),
                    field("price").lessThan(10)
                ).alias("under10Recommendation")
            )
            .execute();
        // [END and_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/logical_functions#or
    void orFunction() {
        // [START or_function]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(
                Expression.or(
                    field("genre").equal("Fantasy"),
                    field("tags").arrayContains("adventure")
                ).alias("matchesSearchFilters")
            )
            .execute();
        // [END or_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/logical_functions#xor
    void xorFunction() {
        // [START xor_function]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(
                Expression.xor(
                    field("tags").arrayContains("magic"),
                    field("tags").arrayContains("nonfiction")
                ).alias("matchesSearchFilters")
            )
            .execute();
        // [END xor_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/logical_functions#not
    void notFunction() {
        // [START not_function]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(
                Expression.not(
                    field("tags").arrayContains("nonfiction")
                ).alias("isFiction")
            )
            .execute();
        // [END not_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/logical_functions#cond
    void condFunction() {
        // [START cond_function]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(
                field("tags").arrayConcat(
                    Expression.conditional(
                        field("pages").greaterThan(100),
                        constant("longRead"),
                        constant("shortRead")
                    )
                ).alias("extendedTags")
            )
            .execute();
        // [END cond_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/logical_functions#eq_any
    void equalAnyFunction() {
        // [START eq_any]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(
                field("genre").equalAny(Arrays.asList("Science Fiction", "Psychological Thriller"))
                    .alias("matchesGenreFilters")
            )
            .execute();
        // [END eq_any]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/logical_functions#not_eq_any
    void notEqualAnyFunction() {
        // [START not_eq_any]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(
                field("author").notEqualAny(Arrays.asList("George Orwell", "F. Scott Fitzgerald"))
                    .alias("byExcludedAuthors")
            )
            .execute();
        // [END not_eq_any]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/logical_functions#is_nan
    void isNaNFunction() {
        // [START is_nan]
        // removed
        // [END is_nan]
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/logical_functions#is_not_nan
    void isNotNaNFunction() {
        // [START is_not_nan]
        // removed
        // [END is_not_nan]
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/logical_functions#max
    void maxLogicalFunction() {
        // [START max_logical_function]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(
                field("rating").logicalMaximum(1).alias("flooredRating")
            )
            .execute();
        // [END max_logical_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/logical_functions#min
    void minLogicalFunction() {
        // [START min_logical_function]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(
                field("rating").logicalMinimum(5).alias("cappedRating")
            )
            .execute();
        // [END min_logical_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/map_functions#map_get
    void mapGetFunction() {
        // [START map_get]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(
                field("awards").mapGet("pulitzer").alias("hasPulitzerAward")
            )
            .execute();
        // [END map_get]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#byte_length
    void byteLengthFunction() {
        // [START byte_length]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(
                field("title").byteLength().alias("titleByteLength")
            )
            .execute();
        // [END byte_length]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#char_length
    void charLengthFunction() {
        // [START char_length]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(
                field("title").charLength().alias("titleCharLength")
            )
            .execute();
        // [END char_length]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#starts_with
    void startsWithFunction() {
        // [START starts_with]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(
                field("title").startsWith("The")
                    .alias("needsSpecialAlphabeticalSort")
            )
            .execute();
        // [END starts_with]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#ends_with
    void endsWithFunction() {
        // [START ends_with]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("inventory/devices/laptops")
            .select(
                field("name").endsWith("16 inch")
                    .alias("16InLaptops")
            )
            .execute();
        // [END ends_with]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#like
    void likeFunction() {
        // [START like]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(
                field("genre").like("%Fiction")
                    .alias("anyFiction")
            )
            .execute();
        // [END like]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#regex_contains
    void regexContainsFunction() {
        // [START regex_contains]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("documents")
            .select(
                field("title").regexContains("Firestore (Enterprise|Standard)")
                    .alias("isFirestoreRelated")
            )
            .execute();
        // [END regex_contains]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#regex_match
    void regexMatchFunction() {
        // [START regex_match]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("documents")
            .select(
                field("title").regexMatch("Firestore (Enterprise|Standard)")
                    .alias("isFirestoreExactly")
            )
            .execute();
        // [END regex_match]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#str_concat
    void strConcatFunction() {
        // [START str_concat]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(
                field("title").concat(" by ", field("author"))
                    .alias("fullyQualifiedTitle")
            )
            .execute();
        // [END str_concat]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#str_contains
    void strContainsFunction() {
        // [START string_contains]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("articles")
            .select(
                field("body").stringContains("Firestore")
                    .alias("isFirestoreRelated")
            )
            .execute();
        // [END string_contains]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#to_upper
    void toUpperFunction() {
        // [START to_upper]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("authors")
            .select(
                field("name").toUpper()
                    .alias("uppercaseName")
            )
            .execute();
        // [END to_upper]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#to_lower
    void toLowerFunction() {
        // [START to_lower]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("authors")
            .select(
                field("genre").toLower().equal("fantasy")
                    .alias("isFantasy")
            )
            .execute();
        // [END to_lower]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#substr
    void substrFunction() {
        // [START substr_function]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .where(field("title").startsWith("The "))
            .select(
                field("title").substring(
                  constant(4),
                    field("title").charLength().subtract(4))
                    .alias("titleWithoutLeadingThe")
            )
            .execute();
        // [END substr_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#str_reverse
    void strReverseFunction() {
        // [START str_reverse]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(
                field("name").reverse().alias("reversedName")
            )
            .execute();
        // [END str_reverse]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#str_trim
    void strTrimFunction() {
        // [START trim_function]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(
                field("name").trim().alias("whitespaceTrimmedName")
            )
            .execute();
        // [END trim_function]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#str_replace
    void strReplaceFunction() {
        // not yet supported until GA
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/string_functions#str_split
    void strSplitFunction() {
        // not yet supported until GA
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/timestamp_functions#unix_micros_to_timestamp
    void unixMicrosToTimestampFunction() {
        // [START unix_micros_timestamp]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("documents")
            .select(
                field("createdAtMicros").unixMicrosToTimestamp().alias("createdAtString")
            )
            .execute();
        // [END unix_micros_timestamp]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/timestamp_functions#unix_millis_to_timestamp
    void unixMillisToTimestampFunction() {
        // [START unix_millis_timestamp]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("documents")
            .select(
                field("createdAtMillis").unixMillisToTimestamp().alias("createdAtString")
            )
            .execute();
        // [END unix_millis_timestamp]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/timestamp_functions#unix_seconds_to_timestamp
    void unixSecondsToTimestampFunction() {
        // [START unix_seconds_timestamp]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("documents")
            .select(
                field("createdAtSeconds").unixSecondsToTimestamp().alias("createdAtString")
            )
            .execute();
        // [END unix_seconds_timestamp]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/timestamp_functions#timestamp_add
    void timestampAddFunction() {
        // [START timestamp_add]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("documents")
            .select(
                field("createdAt").timestampAdd("day", 3653).alias("expiresAt")
            )
            .execute();
        // [END timestamp_add]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/timestamp_functions#timestamp_sub
    void timestampSubFunction() {
        // [START timestamp_sub]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("documents")
            .select(
                field("expiresAt").timestampSubtract("day", 14).alias("sendWarningTimestamp")
            )
            .execute();
        // [END timestamp_sub]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/timestamp_functions#timestamp_to_unix_micros
    void timestampToUnixMicrosFunction() {
        // [START timestamp_unix_micros]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("documents")
            .select(
                field("dateString").timestampToUnixMicros().alias("unixMicros")
            )
            .execute();
        // [END timestamp_unix_micros]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/timestamp_functions#timestamp_to_unix_millis
    void timestampToUnixMillisFunction() {
        // [START timestamp_unix_millis]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("documents")
            .select(
                field("dateString").timestampToUnixMillis().alias("unixMillis")
            )
            .execute();
        // [END timestamp_unix_millis]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/timestamp_functions#timestamp_to_unix_seconds
    void timestampToUnixSecondsFunction() {
        // [START timestamp_unix_seconds]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("documents")
            .select(
                field("dateString").timestampToUnixSeconds().alias("unixSeconds")
            )
            .execute();
        // [END timestamp_unix_seconds]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/vector_functions#cosine_distance
    void cosineDistanceFunction() {
        // [START cosine_distance]
        double[] sampleVector = {0.0, 1.0, 2.0, 3.0, 4.0, 5.0};
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(
                field("embedding").cosineDistance(sampleVector).alias("cosineDistance")
            )
            .execute();
        // [END cosine_distance]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/vector_functions#dot_product
    void dotProductFunction() {
        // [START dot_product]
        double[] sampleVector = {0.0, 1.0, 2.0, 3.0, 4.0, 5.0};
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(
                field("embedding").dotProduct(sampleVector).alias("dotProduct")
            )
            .execute();
        // [END dot_product]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/vector_functions#euclidean_distance
    void euclideanDistanceFunction() {
        // [START euclidean_distance]
        double[] sampleVector = {0.0, 1.0, 2.0, 3.0, 4.0, 5.0};
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(
                field("embedding").euclideanDistance(sampleVector).alias("euclideanDistance")
            )
            .execute();
        // [END euclidean_distance]
        System.out.println(result);
    }

    // https://cloud.google.com/firestore/docs/pipeline/functions/vector_functions#vector_length
    void vectorLengthFunction() {
        // [START vector_length]
        Task<Pipeline.Snapshot> result = db.pipeline()
            .collection("books")
            .select(
                field("embedding").vectorLength().alias("vectorLength")
            )
            .execute();
        // [END vector_length]
        System.out.println(result);
    }
}
