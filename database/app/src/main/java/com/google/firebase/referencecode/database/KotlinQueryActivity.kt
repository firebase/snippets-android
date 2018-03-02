package com.google.firebase.referencecode.database

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.referencecode.database.interfaces.QueryActivityInterface
import com.google.firebase.referencecode.database.models.Message

/**
 * Kotlin verison of {@link QueryActivity].
 */
class KotlinQueryActivity : AppCompatActivity(), QueryActivityInterface {

    companion object {
        private val TAG = "KotlinQueryActivity"
    }

    private lateinit var messagesRef: DatabaseReference
    private lateinit var databaseReference: DatabaseReference
    private lateinit var messagesQuery: Query

    private lateinit var messagesListener: ValueEventListener
    private lateinit var messagesQueryListener: ChildEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query)

        databaseReference = FirebaseDatabase.getInstance().reference
    }

    override fun getUid(): String {
        return "42"
    }

    override fun basicListen() {
        // [START basic_listen]
        // Get a reference to Messages and attach a listener
        messagesRef = databaseReference.child("messages")
        messagesListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // New data at this path. This method will be called after every change in the
                // data at this path or a subpath.

                Log.d(TAG, "Number of messages: ${dataSnapshot.childrenCount}")
                dataSnapshot.children?.forEach { child ->
                    // Extract Message object from the DataSnapshot
                    val message: Message? = child.getValue(Message::class.java)

                    // Use the message
                    // [START_EXCLUDE]
                    Log.d(TAG, "message text: ${message?.text}")
                    Log.d(TAG, "message sender name: ${message?.name}")
                    // [END_EXCLUDE]
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Could not successfully listen for data, log the error
                Log.e(TAG, "messages:onCancelled: ${error.message}")
            }
        }
        messagesRef.addValueEventListener(messagesListener)
        // [END basic_listen]
    }

    override fun basicQuery() {
        // [START basic_query]
        // My top posts by number of stars
        val myUserId = uid
        val myTopPostsQuery = databaseReference.child("user-posts").child(myUserId)
            .orderByChild("starCount")

        myTopPostsQuery.addChildEventListener(object : ChildEventListener {
            // TODO: implement the ChildEventListener methods as documented above
            // [START_EXCLUDE]
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String) {}

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String) {}
            override fun onCancelled(databaseError: DatabaseError) {}
            // [END_EXCLUDE]
        })
        // [END basic_query]
    }

    override fun basicQueryValueListener() {
        val myUserId = uid
        val myTopPostsQuery = databaseReference.child("user-posts").child(myUserId)
            .orderByChild("starCount")

        // [START basic_query_value_listener]
        // My top posts by number of stars
        myTopPostsQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    // TODO: handle the post
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })
        // [END basic_query_value_listener]
    }

    override fun cleanBasicListener() {
        // Clean up value listener
        // [START clean_basic_listen]
        messagesRef.removeEventListener(messagesListener)
        // [END clean_basic_listen]
    }

    override fun cleanBasicQuery() {
        // Clean up query listener
        // [START clean_basic_query]
        messagesQuery.removeEventListener(messagesQueryListener)
        // [END clean_basic_query]
    }

    override fun onStart() {
        super.onStart()
        basicListen()
        basicQuery()
        basicQueryValueListener()
    }

    override fun onStop() {
        super.onStop()
        cleanBasicListener()
        cleanBasicQuery()
    }

}
