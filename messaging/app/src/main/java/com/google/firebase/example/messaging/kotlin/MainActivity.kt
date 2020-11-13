package com.google.firebase.example.messaging.kotlin

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.messaging.ktx.remoteMessage
import java.util.concurrent.atomic.AtomicInteger

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    fun runtimeEnableAutoInit() {
        // [START fcm_runtime_enable_auto_init]
        Firebase.messaging.isAutoInitEnabled = true
        // [END fcm_runtime_enable_auto_init]
    }

    fun deviceGroupUpstream() {
        // [START fcm_device_group_upstream]
        val to = "a_unique_key" // the notification key
        val msgId = AtomicInteger()
        Firebase.messaging.send(remoteMessage(to) {
            setMessageId(msgId.get().toString())
            addData("hello", "world")
        })
        // [END fcm_device_group_upstream]
    }

    fun sendUpstream() {
        val SENDER_ID = "YOUR_SENDER_ID"
        val messageId = 0 // Increment for each
        // [START fcm_send_upstream]
        val fm = Firebase.messaging
        fm.send(remoteMessage("$SENDER_ID@fcm.googleapis.com") {
            setMessageId(messageId.toString())
            addData("my_message", "Hello World")
            addData("my_action", "SAY_HELLO")
        })
        // [END fcm_send_upstream]
    }
}
