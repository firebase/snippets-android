package com.google.example.firestore;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Interface for {@link DocSnippets}.
 */
public interface DocSnippetsInterface {
    void setup();

    void addAdaLovelace();

    void addAlanTuring();

    void getAllUsers();

    void listenForUsers();

    void docReference();

    void collectionReference();

    void subcollectionReference();

    void docReferenceAlternate();

    void setDocument();

    void dataTypes();

    void addCustomClass();

    void addDocument();

    void newDocument();

    void updateDocument();

    void updateDocumentArray();

    void updateDocumentNested();

    void setFieldWithMerge();

    void deleteDocument();

    void transactions();

    void transactionPromise();

    void writeBatch();

    void getDocument();

    void getDocumentWithOptions();

    void customObjects();

    void listenToDocument();

    void listenToDocumentLocal();

    void listenWithMetadata();

    void getMultipleDocs();

    void getAllDocs();

    void listenToMultiple();

    void listenToDiffs();

    void listenState();

    void detachListener();

    void handleListenErrors();

    void exampleData();

    void simpleQueries();

    void arrayContainsQueries();

    void compoundQueries();

    void compoundQueriesInvalid();

    void orderAndLimit();

    void orderAndLimitInvalid();

    void queryStartAtEndAt();

    void toggleOffline();

    void offlineListen(FirebaseFirestore db);

    void updateWithServerTimestamp();

    void updateDeleteField();
}
