package com.google.firebase.example.messaging.kotlin

import android.accounts.Account
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
