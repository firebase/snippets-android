package com.google.firebase.example.messaging.kotlin

import android.accounts.AccountManager
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.Scanner
import java.util.concurrent.atomic.AtomicInteger

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    fun runtimeEnableAutoInit() {
        // [START fcm_runtime_enable_auto_init]
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        // [END fcm_runtime_enable_auto_init]
    }

    fun deviceGroupUpstream() {
        // [START fcm_device_group_upstream]
        val to = "a_unique_key" // the notification key
        val msgId = AtomicInteger()
        FirebaseMessaging.getInstance().send(RemoteMessage.Builder(to)
                .setMessageId(msgId.get().toString())
                .addData("hello", "world")
                .build())
        // [END fcm_device_group_upstream]
    }

    // [START fcm_get_account]
    @SuppressLint("MissingPermission")
    fun getAccount(): String {
        // This call requires the Android GET_ACCOUNTS permission
        val accounts = AccountManager.get(this /* activity */).getAccountsByType("com.google")
        return if (accounts.isEmpty()) {
            ""
        } else accounts[0].name
    }
    // [END fcm_get_account]

    fun getAuthToken() {
        // [START fcm_get_token]
        val accountName = getAccount()

        // Initialize the scope using the client ID you got from the Console.
        val scope = "audience:server:client_id:" +
                "1262xxx48712-9qs6n32447mcj9dirtnkyrejt82saa52.apps.googleusercontent.com"

        var idToken: String? = null
        try {
            idToken = GoogleAuthUtil.getToken(this, accountName, scope)
        } catch (e: Exception) {
            Log.w(TAG, "Exception while getting idToken: $e")
        }

        // [END fcm_get_token]
    }

    // [START fcm_add_to_group]
    @Throws(IOException::class, JSONException::class)
    fun addToGroup(
        senderId: String,
        userEmail: String,
        registrationId: String,
        idToken: String
    ): String {
        val url = URL("https://fcm.googleapis.com/fcm/googlenotification")
        val con = url.openConnection() as HttpURLConnection
        con.doOutput = true

        // HTTP request header
        con.setRequestProperty("project_id", senderId)
        con.setRequestProperty("Content-Type", "application/json")
        con.setRequestProperty("Accept", "application/json")
        con.requestMethod = "POST"
        con.connect()

        // HTTP request
        val data = JSONObject()
        data.put("operation", "add")
        data.put("notification_key_name", userEmail)
        data.put("registration_ids", JSONArray(arrayListOf(registrationId)))
        data.put("id_token", idToken)

        val os = con.outputStream
        os.write(data.toString().toByteArray(charset("UTF-8")))
        os.close()

        // Read the response into a string
        val `is` = con.inputStream
        val responseString = Scanner(`is`, "UTF-8").useDelimiter("\\A").next()
        `is`.close()

        // Parse the JSON string and return the notification key
        val response = JSONObject(responseString)
        return response.getString("notification_key")
    }
    // [END fcm_add_to_group]

    @Throws(JSONException::class)
    fun removeFromGroup(userEmail: String, registrationId: String, idToken: String) {
        // [START fcm_remove_from_group]
        // HTTP request
        val data = JSONObject()
        data.put("operation", "remove")
        data.put("notification_key_name", userEmail)
        data.put("registration_ids", JSONArray(arrayListOf(registrationId)))
        data.put("id_token", idToken)
        // [END fcm_remove_from_group]
    }

    fun sendUpstream() {
        val SENDER_ID = "YOUR_SENDER_ID"
        val messageId = 0 // Increment for each
        // [START fcm_send_upstream]
        val fm = FirebaseMessaging.getInstance()
        fm.send(RemoteMessage.Builder("$SENDER_ID@fcm.googleapis.com")
                .setMessageId(Integer.toString(messageId))
                .addData("my_message", "Hello World")
                .addData("my_action", "SAY_HELLO")
                .build())
        // [END fcm_send_upstream]
    }
}
