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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.referencecode.database.interfaces.MainActivityInterface;

public class MainActivity extends AppCompatActivity implements MainActivityInterface {

    private static final String TAG = "MainActivity";
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
    }

    @Override
    public void valueRead() {
        // [START read_message]
        // Read from the database
        /*
        ValueEventListener provides a DataSnapshot of a whole node on which it's set,
        so every time the data on myRef changes, you get a snapshot of the entire myRef node.
         */
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // Since we get a snapshot of the data we create an Iterable to traverse through it.
                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
                for (DataSnapshot d : snapshotIterable) {
                    String value = d.getValue(String.class);
                    Log.d(TAG, "Value is: " + value);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        // [END read_message]
    }

    @Override
    public void childRead() {
        // [START read_message]
        // Read from the database

        /*
        ChildEventListener provides DataSnapshots of the child that was changed, so instead of getting the data for
        the whole node, you get the data of the child that was modified
         */
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // This method is called when a new child is added to myRef
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // This method is called when data of a particular child in myRef was changed
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // This method is called when a child is removed from myRef
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // This method is triggered when a child location's priority changes
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
        // [END read_message]
    }

    @Override
    public void basicWrite() {
        // [START write_message]
        // Write a message to the database
        /*
            push() generates a random key and sets it as a child of our ref.
            The data is then stored to that child instead of storing it directly under ref
         */
        myRef.push().setValue("Hello, World!");
        // [END write_message]
    }
}
