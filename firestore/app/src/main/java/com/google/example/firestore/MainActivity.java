package com.google.example.firestore;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Main Activity to launch a smoke test.
 */
public class MainActivity extends Activity implements OnClickListener {

    private static final String TAG = "MainActivity";
    private static final String DEFAULT_COLLECTION = "samstern";

    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_write).setOnClickListener(this);
        findViewById(R.id.button_smoketest).setOnClickListener(this);
        findViewById(R.id.button_delete_all).setOnClickListener(this);

        mFirestore = FirebaseFirestore.getInstance();

        new SolutionRateLimiting().startUpdates();
    }

    private void onWriteClicked() {
        String random = UUID.randomUUID().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("text", random);

        mFirestore.collection(DEFAULT_COLLECTION)
                .document()
                .set(map)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "write:onComplete");
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "write:onComplete:failed", task.getException());
                        }
                    }
                });
    }

    private void onSmokeTestClicked() {
        FirebaseAuth.getInstance()
                .signInAnonymously()
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d(TAG, "auth:onSuccess");

                        // Run snippets
                        DocSnippets docSnippets = new DocSnippets(mFirestore);
                        docSnippets.runAll();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "auth:onFailure", e);
                    }
                });
    }

    private void onDeleteAllClicked() {
        FirebaseAuth.getInstance()
                .signInAnonymously()
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d(TAG, "auth:onSuccess");

                        // Delete
                        DocSnippets docSnippets = new DocSnippets(mFirestore);
                        docSnippets.deleteAll();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "auth:onFailure", e);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_write) {
            onWriteClicked();
        }

        if (v.getId() == R.id.button_smoketest) {
            onSmokeTestClicked();
        }

        if (v.getId() == R.id.button_delete_all) {
            onDeleteAllClicked();
        }
    }
}
