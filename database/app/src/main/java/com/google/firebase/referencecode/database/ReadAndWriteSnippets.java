package com.google.firebase.referencecode.database;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.referencecode.database.models.User;

public class ReadAndWriteSnippets {

    // [START initialize_database_ref]
    private DatabaseReference mDatabase;
    // ...
        // [START_EXCLUDE]
    public ReadAndWriteSnippets(DatabaseReference database) {
        // [END_EXCLUDE]
    mDatabase = FirebaseDatabase.getInstance().getReference();
    // [END initialize_database_ref]
    }

    // [START rtdb_write_new_user]
    public void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }
    // [END rtdb_write_new_user]

    public void writeNewUserWithTaskListeners(String userId, String name, String email) {
        User user = new User(name, email);

        // [START rtdb_write_new_user_task]
        mDatabase.child("users").child(userId).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        // ...
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                    }
                });
        // [END rtdb_write_new_user_task]
    }

}
