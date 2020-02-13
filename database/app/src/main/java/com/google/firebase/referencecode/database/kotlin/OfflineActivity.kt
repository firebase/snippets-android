package com.google.firebase.referencecode.database.kotlin

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class OfflineActivity : AppCompatActivity() {

    private fun enablePersistence() {
        // [START rtdb_enable_persistence]
        Firebase.database.setPersistenceEnabled(true)
        // [END rtdb_enable_persistence]
    }

    private fun keepSynced() {
        // [START rtdb_keep_synced]
        val scoresRef = Firebase.database.getReference("scores")
        scoresRef.keepSynced(true)
        // [END rtdb_keep_synced]

        // [START rtdb_undo_keep_synced]
        scoresRef.keepSynced(false)
        // [END rtdb_undo_keep_synced]
    }

    private fun queryRecentScores() {
        // [START rtdb_query_recent_scores]
        val scoresRef = Firebase.database.getReference("scores")
        scoresRef.orderByValue().limitToLast(4).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChild: String?) {
                Log.d(TAG, "The ${snapshot.key} dinosaur's score is ${snapshot.value}")
            }

            // [START_EXCLUDE]
            override fun onChildRemoved(dataSnapshot: DataSnapshot) = Unit

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) = Unit

            override fun onCancelled(databaseError: DatabaseError) = Unit

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) = Unit
            // [END_EXCLUDE]
        })
        // [END rtdb_query_recent_scores]

        // [START rtdb_query_recent_scores_overlap]
        scoresRef.orderByValue().limitToLast(2).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChild: String?) {
                Log.d(TAG, "The ${snapshot.key} dinosaur's score is ${snapshot.value}")
            }

            // [START_EXCLUDE]
            override fun onChildRemoved(dataSnapshot: DataSnapshot) = Unit

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) = Unit

            override fun onCancelled(databaseError: DatabaseError) = Unit

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) = Unit
            // [END_EXCLUDE]
        })
        // [END rtdb_query_recent_scores_overlap]
    }

    private fun onDisconnect() {
        // [START rtdb_on_disconnect_set]
        val presenceRef = Firebase.database.getReference("disconnectmessage")
        // Write a string when this client loses connection
        presenceRef.onDisconnect().setValue("I disconnected!")
        // [END rtdb_on_disconnect_set]

        // [START rtdb_on_disconnect_remove]
        presenceRef.onDisconnect().removeValue { error, reference ->
            error?.let {
                Log.d(TAG, "could not establish onDisconnect event: ${error.message}")
            }
        }
        // [END rtdb_on_disconnect_remove]

        // [START rtdb_on_disconnect_cancel]
        val onDisconnectRef = presenceRef.onDisconnect()
        onDisconnectRef.setValue("I disconnected")
        // ...
        // some time later when we change our minds
        // ...
        onDisconnectRef.cancel()
        // [END rtdb_on_disconnect_cancel]
    }

    private fun getConnectionState() {
        // [START rtdb_listen_connected]
        val connectedRef = Firebase.database.getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false
                if (connected) {
                    Log.d(TAG, "connected")
                } else {
                    Log.d(TAG, "not connected")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Listener was cancelled")
            }
        })
        // [END rtdb_listen_connected]
    }

    private fun disconnectionTimestamp() {
        // [START rtdb_on_disconnect_timestamp]
        val userLastOnlineRef = Firebase.database.getReference("users/joe/lastOnline")
        userLastOnlineRef.onDisconnect().setValue(ServerValue.TIMESTAMP)
        // [END rtdb_on_disconnect_timestamp]
    }

    private fun getServerTimeOffset() {
        // [START rtdb_server_time_offset]
        val offsetRef = Firebase.database.getReference(".info/serverTimeOffset")
        offsetRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val offset = snapshot.getValue(Double::class.java) ?: 0.0
                val estimatedServerTimeMs = System.currentTimeMillis() + offset
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Listener was cancelled")
            }
        })
        // [END rtdb_server_time_offset]
    }

    private fun fullConnectionExample() {
        // [START rtdb_full_connection_example]
        // Since I can connect from multiple devices, we store each connection instance separately
        // any time that connectionsRef's value is null (i.e. has no children) I am offline
        val database = Firebase.database
        val myConnectionsRef = database.getReference("users/joe/connections")

        // Stores the timestamp of my last disconnect (the last time I was seen online)
        val lastOnlineRef = database.getReference("/users/joe/lastOnline")

        val connectedRef = database.getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue<Boolean>() ?: false
                if (connected) {
                    val con = myConnectionsRef.push()

                    // When this device disconnects, remove it
                    con.onDisconnect().removeValue()

                    // When I disconnect, update the last time I was seen online
                    lastOnlineRef.onDisconnect().setValue(ServerValue.TIMESTAMP)

                    // Add this device to my connections list
                    // this value could contain info about the device or a timestamp too
                    con.setValue(java.lang.Boolean.TRUE)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Listener was cancelled at .info/connected")
            }
        })
        // [END rtdb_full_connection_example]
    }

    companion object {
        private val TAG = "OfflineActivity"
    }
}
