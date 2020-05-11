package com.google.firebase.example.crashlytics.kotlin

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.crashlytics.FirebaseCrashlytics

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun setKeysBasic(key: String) {
        // [START crash_set_keys_basic]
        val crashlytics = FirebaseCrashlytics.getInstance()

        crashlytics.setCustomKey(key, "foo" /* string value */)

        crashlytics.setCustomKey(key, true /* boolean value */)

        crashlytics.setCustomKey(key, 1.0 /* double value */)

        crashlytics.setCustomKey(key, 1.0f /* float value */)

        crashlytics.setCustomKey(key, 1 /* int value */)
        // [END crash_set_keys_basic]
    }

    fun resetKey() {
        // [START crash_re_set_key]
        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.setCustomKey("current_level", 3)
        crashlytics.setCustomKey("last_UI_action", "logged_in")
        // [END crash_re_set_key]
    }

    fun logReportAndPrint() {
        // [START crash_log_report_and_print]
        FirebaseCrashlytics.getInstance().log("message")
        // [END crash_log_report_and_print]
    }

    fun logReportOnly() {
        // [START crash_log_report_only]
        FirebaseCrashlytics.getInstance().log("message")
        // [END crash_log_report_only]
    }

    fun enableAtRuntime() {
        // [START crash_enable_at_runtime]
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        // [END crash_enable_at_runtime]
    }

    fun setUserId() {
        // [START crash_set_user_id]
        FirebaseCrashlytics.getInstance().setUserId("user123456789")
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
            FirebaseCrashlytics.getInstance().recordException(e)
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
