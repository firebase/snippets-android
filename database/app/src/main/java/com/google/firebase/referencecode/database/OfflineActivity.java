package com.google.firebase.referencecode.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.OnDisconnect;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

public class OfflineActivity extends AppCompatActivity {
    
    private static final String TAG = "OfflineActivity";

    private void enablePersistence() {
        // [START rtdb_enable_persistence]
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        // [END rtdb_enable_persistence]
    }

    private void keepSynced() {
        // [START rtdb_keep_synced]
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("scores");
        scoresRef.keepSynced(true);
        // [END rtdb_keep_synced]

        // [START rtdb_undo_keep_synced]
        scoresRef.keepSynced(false);
        // [END rtdb_undo_keep_synced]
    }

    private void queryRecentScores() {
        // [START rtdb_query_recent_scores]
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("scores");
        scoresRef.orderByValue().limitToLast(4).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChild) {
                Log.d(TAG, "The " + snapshot.getKey() + " dinosaur's score is " + snapshot.getValue());
            }

            // [START_EXCLUDE]
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            // [END_EXCLUDE]
        });
        // [END rtdb_query_recent_scores]

        // [START rtdb_query_recent_scores_overlap]
        scoresRef.orderByValue().limitToLast(2).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChild) {
                Log.d(TAG, "The " + snapshot.getKey() + " dinosaur's score is " + snapshot.getValue());
            }

            // [START_EXCLUDE]
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            // [END_EXCLUDE]
        });
        // [END rtdb_query_recent_scores_overlap]
    }

    private void onDisconnect() {
        // [START rtdb_on_disconnect_set]
        DatabaseReference presenceRef = FirebaseDatabase.getInstance().getReference("disconnectmessage");
        // Write a string when this client loses connection
        presenceRef.onDisconnect().setValue("I disconnected!");
        // [END rtdb_on_disconnect_set]

        // [START rtdb_on_disconnect_remove]
        presenceRef.onDisconnect().removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, @NonNull DatabaseReference reference) {
                if (error != null) {
                    Log.d(TAG, "could not establish onDisconnect event:" + error.getMessage());
                }
            }
        });
        // [END rtdb_on_disconnect_remove]

        // [START rtdb_on_disconnect_cancel]
        OnDisconnect onDisconnectRef = presenceRef.onDisconnect();
        onDisconnectRef.setValue("I disconnected");
        // ...
        // some time later when we change our minds
        // ...
        onDisconnectRef.cancel();
        // [END rtdb_on_disconnect_cancel]
    }

    private void getConnectionState() {
        // [START rtdb_listen_connected]
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Log.d(TAG, "connected");
                } else {
                    Log.d(TAG, "not connected");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Listener was cancelled");
            }
        });
        // [END rtdb_listen_connected]
    }

    private void disconnectionTimestamp() {
        // [START rtdb_on_disconnect_timestamp]
        DatabaseReference userLastOnlineRef = FirebaseDatabase.getInstance().getReference("users/joe/lastOnline");
        userLastOnlineRef.onDisconnect().setValue(ServerValue.TIMESTAMP);
        // [END rtdb_on_disconnect_timestamp]
    }

    private void getServerTimeOffset() {
        // [START rtdb_server_time_offset]
        DatabaseReference offsetRef = FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
        offsetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double offset = snapshot.getValue(Double.class);
                double estimatedServerTimeMs = System.currentTimeMillis() + offset;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Listener was cancelled");
            }
        });
        // [END rtdb_server_time_offset]
    }

    private void fullConnectionExample() {
        // [START rtdb_full_connection_example]
        // Since I can connect from multiple devices, we store each connection instance separately
        // any time that connectionsRef's value is null (i.e. has no children) I am offline
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myConnectionsRef = database.getReference("users/joe/connections");

        // Stores the timestamp of my last disconnect (the last time I was seen online)
        final DatabaseReference lastOnlineRef = database.getReference("/users/joe/lastOnline");

        final DatabaseReference connectedRef = database.getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    DatabaseReference con = myConnectionsRef.push();

                    // When this device disconnects, remove it
                    con.onDisconnect().removeValue();

                    // When I disconnect, update the last time I was seen online
                    lastOnlineRef.onDisconnect().setValue(ServerValue.TIMESTAMP);

                    // Add this device to my connections list
                    // this value could contain info about the device or a timestamp too
                    con.setValue(Boolean.TRUE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Listener was cancelled at .info/connected");
            }
        });
        // [END rtdb_full_connection_example]
    }

}
