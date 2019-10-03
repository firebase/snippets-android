package com.google.example.firestore;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableReference;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

public class SolutionDeletes {

    // [START call_delete_function]
    /**
     * Call the 'recursiveDelete' callable function with a path to initiate
     * a server-side delete.
     */
    public void deleteAtPath(String path) {
        Map<String, Object> data = new HashMap<>();
        data.put("path", path);

        HttpsCallableReference deleteFn =
                FirebaseFunctions.getInstance().getHttpsCallable("recursiveDelete");
        deleteFn.call(data)
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                        // Delete Success
                        // ...
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Delete failed
                        // ...
                    }
                });
    }
    // [END call_delete_function]
}
