package com.google.firebase.example.crashlytics.kotlin

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.crashlytics.ktx.setCustomKeys
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun setKeysBasic() {
        // [START crash_set_keys_basic]
        val crashlytics = Firebase.crashlytics
        crashlytics.setCustomKeys {
            key("my_string_key", "foo") // String value
            key("my_bool_key", true)    // boolean value
            key("my_double_key", 1.0)   // double value
            key("my_float_key", 1.0f)   // float value
            key("my_int_key", 1)        // int value
        }
        // [END crash_set_keys_basic]
    }

    fun resetKey() {
        // [START crash_re_set_key]
        val crashlytics = Firebase.crashlytics
        crashlytics.setCustomKeys {
            key("current_level", 3)
            key("last_UI_action", "logged_in")
        }
        // [END crash_re_set_key]
    }

    fun logReportAndPrint() {
        // [START crash_log_report_and_print]
        Firebase.crashlytics.log("message")
        // [END crash_log_report_and_print]
    }

    fun logReportOnly() {
        // [START crash_log_report_only]
        Firebase.crashlytics.log("message")
        // [END crash_log_report_only]
    }

    fun enableAtRuntime() {
        // [START crash_enable_at_runtime]
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(true)
        // [END crash_enable_at_runtime]
    }

    fun setUserId() {
        // [START crash_set_user_id]
        Firebase.crashlytics.setUserId("user123456789")
        // [END crash_set_user_id]
    }

    @Throws(Exception::class)
    fun methodThatThrows() {
        throw Exception()
    }

    fun logCaughtEx() {
        // [START crash_log_caught_ex]
        try {
            methodThatThrows()
        } catch (e: Exception) {
            Firebase.crashlytics.recordException(e)
            // handle your exception here
        }
        // [END crash_log_caught_ex]
    }

    fun forceACrash() {
        // [START crash_force_crash]
        val crashButton = Button(this)
        crashButton.text = "Crash!"
        crashButton.setOnClickListener {
            throw RuntimeException() // Force a crash
        }

        addContentView(crashButton, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT))
        // [END crash_force_crash]
    }
}
