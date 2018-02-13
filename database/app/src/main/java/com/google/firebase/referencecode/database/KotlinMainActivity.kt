package com.google.firebase.referencecode.database

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.referencecode.database.interfaces.MainActivityInterface

class KotlinMainActivity : AppCompatActivity(), MainActivityInterface {

    companion object {
        private const val TAG = "KotlinActivity"
    }

    private lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Reuse the layout used in MainActivity
        setContentView(R.layout.activity_main)
        val database = FirebaseDatabase.getInstance()
        myRef = database.getReference("message")
    }

    override fun valueRead() {
        // [START read_message]
        // Read from the database
        /*
        ValueEventListener provides a DataSnapshot of a whole node on which it's set,
        so every time the data on myRef changes, you get a snapshot of the entire myRef node.
         */
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // Since we get a snapshot of the data we create an Iterable to traverse through it.
                val snapshotIterable = dataSnapshot.children
                snapshotIterable
                        .map { it.getValue(String::class.java) }
                        .forEach { Log.d(TAG, "Value is: " + it!!) }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
        // [END read_message]
    }

    override fun childRead() {
        // [START read_message]
        // Read from the database
        /*
        ChildEventListener provides DataSnapshots of the child that was changed, so instead of getting the data for
        the whole node, you get the data of the child that was modified
         */
        myRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String) {
                // This method is called when a new child is added to myRef
                val value = dataSnapshot.getValue(String::class.java)
                Log.d(TAG, "Value is: " + value!!)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String) {
                // This method is called when data of a particular child in myRef was changed
                val value = dataSnapshot.getValue(String::class.java)
                Log.d(TAG, "Value is: " + value!!)
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                // This method is called when a child is removed from myRef
                val value = dataSnapshot.getValue(String::class.java)
                Log.d(TAG, "Value is: " + value!!)
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String) {
                // This method is triggered when a child location's priority changes
                val value = dataSnapshot.getValue(String::class.java)
                Log.d(TAG, "Value is: " + value!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException())
            }
        })
        // [END read_message]
    }

    override fun basicWrite() {
        // [START write_message]
        // Write a message to the database
        /*
            push() generates a random key and sets it as a child of our ref.
            The data is then stored to that child instead of storing it directly under ref
         */
        myRef.push().setValue("Hello, World!")
        // [END write_message]
    }
}
