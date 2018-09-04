package com.google.firebase.example.messaging.interfaces

import org.json.JSONException
import java.io.IOException

/**
 * An interface to be implemented by the Activity.
 * Add any new method to this interface instead of adding it directly to the activity
 */
interface MainActivityInterface {
    fun deviceGroupUpstream()
    fun getAccount(): String
    fun getAuthToken()
    @Throws(IOException::class, JSONException::class)
    fun addToGroup(senderId: String, email: String, registrationId: String, idToken: String): String
    @Throws(JSONException::class)
    fun removeFromGroup(email: String, registrationId: String, idToken: String)
}