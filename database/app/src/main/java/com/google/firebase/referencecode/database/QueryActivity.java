/*
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.firebase.referencecode.database;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.referencecode.database.models.Message;

/**
 * Activity to demonstrate basic data querying. To start this Activity, run:
 * <code>adb shell am start -n com.google.firebase.quickstart.database/.QueryActivity</code>
 *
 * Use {@link MainActivity} to populate the Message data.
 */
public class QueryActivity extends AppCompatActivity {

    private static final String TAG = "QueryActivity";

    private DatabaseReference databaseReference;
    private DatabaseReference mMessagesRef;
    private Query mMessagesQuery;

    private ValueEventListener mMessagesListener;
    private ChildEventListener mMessagesQueryListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        // Get a reference to the Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private String getUid() {
        return "42";
    }

    @Override
    public void onStart() {
        super.onStart();

        // [START basic_listen]
        // Get a reference to Messages and attach a listener
        mMessagesRef = databaseReference.child("messages");
        mMessagesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // New data at this path. This method will be called after every change in the
                // data at this path or a subpath.

                // Get the data as Message objects
                Log.d(TAG, "Number of messages: " + dataSnapshot.getChildrenCount());
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    // Extract a Message object from the DataSnapshot
                    Message message = child.getValue(Message.class);

                    // Use the Message
                    // [START_EXCLUDE]
                    Log.d(TAG, "message text:" + message.getText());
                    Log.d(TAG, "message sender name:" + message.getName());
                    // [END_EXCLUDE]
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Could not successfully listen for data, log the error
                Log.e(TAG, "messages:onCancelled:" + error.getMessage());
            }
        };
        mMessagesRef.addValueEventListener(mMessagesListener);
        // [END basic_listen]


        // [START basic_query]
        // My top posts by number of stars
        String myUserId = getUid();
        Query myTopPostsQuery = databaseReference.child("user-posts").child(myUserId)
                .orderByChild("starCount");
        myTopPostsQuery.addChildEventListener(new ChildEventListener() {
            // TODO: implement the ChildEventListener methods as documented above
            // [START_EXCLUDE]
            public void onChildAdded(DataSnapshot dataSnapshot, String s) { }
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }
            public void onChildRemoved(DataSnapshot dataSnapshot) { }
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
            public void onCancelled(DatabaseError databaseError) { }
            // [END_EXCLUDE]
        });
        // [END basic_query]

        // [START basic_query_value_listener]
        // My top posts by number of stars
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
        // [END basic_query_value_listener]

    }

    @Override
    public void onStop() {
        super.onStop();

        // Clean up value listener
        // [START clean_basic_listen]
        mMessagesRef.removeEventListener(mMessagesListener);
        // [END clean_basic_listen]

        // Clean up query listener
        // [START clean_basic_query]
        mMessagesQuery.removeEventListener(mMessagesQueryListener);
        // [END clean_basic_query]
    }
}

