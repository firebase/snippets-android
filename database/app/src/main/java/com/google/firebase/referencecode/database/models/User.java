package com.google.firebase.referencecode.database.models;

import com.google.firebase.database.IgnoreExtraProperties;

// [START rtdb_user_class]
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

}
// [END rtdb_user_class]
