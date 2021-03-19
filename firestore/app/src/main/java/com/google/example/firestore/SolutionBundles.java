package com.google.example.firestore;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.LoadBundleTask;
import com.google.firebase.firestore.LoadBundleTaskProgress;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * https://firebase.google.com/docs/firestore/solutions/serve-bundles
 */
public class SolutionBundles {

    private static final String TAG = "SolutionBundles";

    private FirebaseFirestore db;

    // [START fs_bundle_load]
    public InputStream getBundleStream(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        return connection.getInputStream();
    }

    public void fetchBundleFrom() throws IOException {
        final InputStream bundleStream = getBundleStream("https://example.com/createBundle");
        LoadBundleTask loadTask = db.loadBundle(bundleStream);

        // Chain the following tasks
        // 1) Load the bundle
        // 2) Get the named query from the local cache
        // 3) Execute a get() on the named query
        loadTask.continueWithTask(new Continuation<LoadBundleTaskProgress, Task<Query>>() {
            @Override
            public Task<Query> then(@NonNull Task<LoadBundleTaskProgress> task) throws Exception {
                // Close the stream
                bundleStream.close();

                // Calling getResult() propagates errors
                LoadBundleTaskProgress progress = task.getResult(Exception.class);

                // Get the named query from the bundle cache
                return db.getNamedQuery("latest-stories-query");
            }
        }).continueWithTask(new Continuation<Query, Task<QuerySnapshot>>() {
            @Override
            public Task<QuerySnapshot> then(@NonNull Task<Query> task) throws Exception {
                Query query = task.getResult(Exception.class);

                // get() the query results from the cache
                return query.get(Source.CACHE);
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "Bundle loading failed", task.getException());
                    return;
                }

                // Get the QuerySnapshot from the bundle
                QuerySnapshot storiesSnap = task.getResult();

                // Use the results
                // ...
            }
        });
    }
    // [END fs_bundle_load]

}

