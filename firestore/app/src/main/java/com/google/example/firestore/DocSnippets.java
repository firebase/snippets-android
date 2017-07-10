package com.google.example.firestore;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentChange.Type;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.UpdateOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Snippets for inclusion in documentation.
 */
@SuppressWarnings({"unused", "Convert2Lambda"})
public class DocSnippets {

    private static final String TAG = "DocSnippets";

    private final FirebaseFirestore db;

    DocSnippets(FirebaseFirestore db) {
        this.db = db;
    }

    void runAll() {
        Log.d(TAG, "================= BEGIN RUN ALL ===============");

        // Write example data
        exampleData();

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
        updateWithOptions();
        deleteDocument();
        transactions();
        transactionPromise();
        getDocument();
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

    // TODO: Should be a way to batch these tasks
    private void deleteCollection(final String path) {
        db.collection(path).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documents) {
                        Log.d(TAG, "collectionGet:success:" + path);
                        for (DocumentSnapshot d : documents) {
                            if (d != null) {
                                Log.d(TAG, "deleting:" + d.getId());
                                d.getReference().delete();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "collectionGet:failure:" + path, e);
                    }
                });
    }

    // =============================================================================================
    // https://firebase-dot-devsite.googleplex.com/preview/firestore/client/setup-android
    // =============================================================================================

    private void setup() {
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

    // =============================================================================================
    // https://firebase-dot-devsite.googleplex.com/preview/firestore/client/quickstart
    // =============================================================================================

    private void addAdaLovelace() {
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


    private void addAlanTuring() {
        // [START add_alan_turing]
        // Create a new user with a first, middle, and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Alan");
        user.put("middle", "Mathison");
        user.put("last", "Turring");
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

    private void getAllUsers() {
        // [START get_all_users]
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        // [END get_all_users]
    }

    private void listenForUsers() {
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

    // =============================================================================================
    // https://firebase-dot-devsite.googleplex.com/preview/firestore/client/structure-data
    // =============================================================================================


    private void docReference() {
        // [START doc_reference]
        DocumentReference alovelaceDocumentRef = db.collection("users").document("alovelace");
        // [END doc_reference]
    }

    private void collectionReference() {
        // [START collection_reference]
        CollectionReference usersCollectionRef = db.collection("users");
        // [END collection_reference]
    }

    private void subcollectionReference() {
        // [START subcollection_reference]
        DocumentReference messageRef = db
                .collection("rooms").document("roomA")
                .collection("messages").document("message1");
        // [END subcollection_reference]
    }

    // =============================================================================================
    // https://firebase-dot-devsite.googleplex.com/preview/firestore/client/save-data
    // =============================================================================================

    private void setDocument() {
        // [START set_document]
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Washington D.C.");
        city.put("weather", "politically stormy");

        db.collection("cities").document("DC")
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
        db.collection("cities").document("new_city_id").set(data);
        // [END set_with_id]
    }
    private void dataTypes() {
        // [START data_types]
        Map<String, Object> docData = new HashMap<>();
        docData.put("stringExample", "Hello world!");
        docData.put("booleanExample", true);
        docData.put("numberExample", 3.14159265);
        docData.put("dateExample", new Date());
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

    private void addDocument() {
        // [START add_document]
        // Add a new document with a generated id.
        Map<String, Object> data = new HashMap<>();
        data.put("name", "Denver");
        data.put("weather", "rocky");

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

    private void newDocument() {
        // [START new_document]
        Map<String, Object> data = new HashMap<>();

        DocumentReference newCityRef = db.collection("cities").document();

        // Later...
        newCityRef.set(data);
        // [END new_document]
    }

    private void updateDocument() {
        // [START update_document]
        DocumentReference washingtonRef = db.collection("cities").document("DC");

        // Set the "isCapital" field of the city 'DC'
        washingtonRef
                .update("isCapital", true)
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

    private void updateDocumentNested() {
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

    private void updateWithOptions() {
        // [START update_with_options]
        // Update the population, creating the document if it
        // does not already exist.
        db.collection("cities").document("Beijing").update(
                new UpdateOptions().createIfMissing(),
                "population",
                21500000);
        // [END update_with_options]
    }

    private void deleteDocument() {
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

    private void transactions() {
        // [START transactions]
        final DocumentReference sfDocRef = db.collection("cities").document("SF");

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);
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

    private void transactionPromise() {
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

    private void writeBatch() {
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


    // =============================================================================================
    // https://firebase-dot-devsite.googleplex.com/preview/firestore/client/retrieve-data
    // =============================================================================================

    private void getDocument() {
        // [START get_document]
        DocumentReference docRef = db.collection("cities").document("SF");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getData());
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

    private void customObjects() {
        // [START custom_objects]
        DocumentReference docRef = db.collection("cities").document("Beijing");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                City city = documentSnapshot.toObject(City.class);
            }
        });
        // [END custom_objects]
    }

    private void listenToDocument() {
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

        // After 2 seconds, make an update so our listener will fire again.
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> data = new HashMap<>();
                data.put("population", 99999);
                docRef.update(data);
            }
        }, 2000);

        // RESULT:
        // Current data: {name=San Francisco, state=CA, population=864816.0}
        // Current data: {name=San Francisco, state=CA, population=99999.0}
        // Current data: {name=San Francisco, state=CA, population=99999.0}
        // [END listen_document]
    }

    private void listenToDocumentLocal() {
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

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> data = new HashMap<>();
                data.put("population", 100000);
                docRef.update(data);
            }
        }, 2000);

        // RESULT:
        // Server data: {name=San Francisco, state=CA, population=99999.0}
        // Local data: {name=San Francisco, state=CA, population=100000.0}
        // Server data: {name=San Francisco, state=CA, population=100000.0}
        // [END listen_document_local]
    }

    private void getMultipleDocs() {
        // [START get_multiple]
        db.collection("cities")
                .whereEqualTo("state", "CA")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        // RESULT:
        // MTV => {name=Mountain View, state=CA, population=74066.0}
        // SF => {name=San Francisco, state=CA, population=864816.0}
        // [END get_multiple]
    }

    private void getAllDocs() {
        // [START get_multiple_all]
        db.collection("cities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        // RESULT:
        // DC => {name=Washington D.C., state=null, population=672228.0}
        // DEN => {name=Denver, state=CO, population=600158.0}
        // MTV => {name=Mountain View, state=CA, population=74066.0}
        // SF => {name=San Francisco, state=CA, population=864816.0}
        // [END get_multiple_all]
    }

    private void listenToMultiple() {
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
                        for (DocumentSnapshot doc : value) {
                            if (doc.get("name") != null) {
                                cities.add(doc.getString("name"));
                            }
                        }
                        Log.d(TAG, "Current cites in CA: " + cities);
                    }
                });

        // After 2 seconds, make an update so our listener will fire again.
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> data = new HashMap<>();
                data.put("name", "Los Angeles");
                data.put("state", "CA");
                data.put("population", 4030904);
                db.collection("cities").document("LA").set(data);
            }
        }, 2000);

        // RESULT:
        // Current cites in CA: [Mountain View, San Francisco]
        // Current cites in CA: [Los Angeles, Mountain View, San Francisco]
        // [END listen_multiple]
    }

    private void listenToDiffs() {
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

        // After 2 seconds, let's delete LA
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                db.collection("cities").document("LA").delete();
            }
        }, 2000);

        // RESULT:
        // New city: {name=Los Angeles, state=CA, population=4030904.0}
        // New city: {name=Mountain View, state=CA, population=74066.0}
        // New city: {name=San Francisco, state=CA, population=864816.0}
        // Removed city: {name=Los Angeles, state=CA, population=4030904.0}
        // [END listen_diffs]
    }

    private void listenState() {
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

        // RESULT:
        // New city: {name=Mountain View, state=CA, population=74066.0}
        // New city: {name=San Francisco, state=CA, population=864816.0}
        // Got initial state.
        // [END listen_state]
    }

    private void detachListener() {
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

    private void handleListenErrors() {
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

    // =============================================================================================
    // https://firebase-dot-devsite.googleplex.com/preview/firestore/client/query-data
    // =============================================================================================

    private void exampleData() {
        // [START example_data]
        CollectionReference cities = db.collection("cities");
        Map<String, Object> data1 = new HashMap<>();
        data1.put("name", "San Francisco");
        data1.put("state", "CA");
        data1.put("population", 864816);
        cities.document("SF").set(data1);

        Map<String, Object> data2 = new HashMap<>();
        data2.put("name", "Mountain View");
        data2.put("state", "CA");
        data2.put("population", 74066);
        cities.document("MTV").set(data2);

        Map<String, Object> data3 = new HashMap<>();
        data3.put("name", "Denver");
        data3.put("state", "CO");
        data3.put("population", 600158);
        cities.document("DEN").set(data3);

        Map<String, Object> data4 = new HashMap<>();
        data4.put("name", "Washington D.C.");
        data4.put("state", null);
        data4.put("population", 672228);
        cities.document("DC").set(data4);
        // [END example_data]
    }

    private void simpleQueries() {
        // [START simple_queries]
        // Create a reference to the cities collection
        CollectionReference citiesRef = db.collection("cities");

        // Create a query against the collection.
        Query query = citiesRef.whereEqualTo("state", "CA");
        // [END simple_queries]

        // [START example_filters]
        citiesRef.whereEqualTo("state", "CA");
        citiesRef.whereLessThan("population", 100000);
        citiesRef.whereGreaterThanOrEqualTo("name", "San Francisco");
        // [END example_filters]
    }

    private void compoundQueries() {
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

    private void compoundQueriesInvalid() {
        CollectionReference citiesRef = db.collection("cities");

        // [START invalid_range_filters]
        citiesRef.whereGreaterThanOrEqualTo("state", "CA").whereGreaterThan("population", 100000);
        // [END invalid_range_filters]
    }

    private void orderAndLimit() {
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

    private void orderAndLimitInvalid() {
        CollectionReference citiesRef = db.collection("cities");

        // [START invalid_filter_and_order]
        citiesRef.whereGreaterThan("population", 100000).orderBy("state");
        // [END invalid_filter_and_order]
    }

    private void queryStartAtEndAt() {
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
                            .get(documentSnapshots.size());

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

    // [START delete_collection]
    public static Task<Void> deleteCollection(final CollectionReference collection,
                                              final int batchSize) {
        if (batchSize == 0) {
            return Tasks.forResult(null);
        }

        return collection
                .limit(batchSize)
                .get()
                .continueWithTask(
                        new Continuation<QuerySnapshot, Task<Void>>() {
                            @Override
                            public Task<Void> then(@NonNull Task<QuerySnapshot> task) throws Exception {
                                // Get the documents (throwing exception if the get failed)
                                QuerySnapshot documents = task.getResult(Exception.class);

                                // If there are no documents, we are done, return a successful task
                                if (documents.size() == 0) {
                                    return deleteCollection(collection, 0);
                                }

                                // Create a list of tasks, one for each delete in the collection
                                ArrayList<Task<Void>> deleteTasks = new ArrayList<>();

                                // For each document in the collection, kick off a delete
                                for (DocumentSnapshot d : documents) {
                                    deleteTasks.add(d.getReference().delete());
                                }

                                // Return a single task for all deletes
                                return Tasks.whenAll(deleteTasks);
                            }
                        })
                .continueWithTask(
                        new Continuation<Void, Task<Void>>() {
                            @Override
                            public Task<Void> then(@NonNull Task<Void> task) throws Exception {
                                // If the deletes failed, return a failed task
                                if (!task.isSuccessful()) {
                                    return Tasks.forException(task.getException());
                                }

                                // Recursively delete the next batch
                                return deleteCollection(collection, batchSize);
                            }
                        });
    }
    // [END delete_collection]

    // =============================================================================================
    // https://firebase.google.com/docs/firestore/enable-offline
    // =============================================================================================

    private void offlineListen(FirebaseFirestore db) {
        // [START offline_listen]
        db.collection("cities").whereEqualTo("state", "CA")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    private void updateWithServerTimestamp() {
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

    private void updateDeleteField() {
        // [START update_delete_field]
        DocumentReference docRef = db.collection("cities").document("Beijing");

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
