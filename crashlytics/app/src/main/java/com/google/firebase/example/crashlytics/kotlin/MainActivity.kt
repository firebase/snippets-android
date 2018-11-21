package com.google.firebase.example.crashlytics.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun setKeysBasic(key: String) {
        // [START crash_set_keys_basic]
        Crashlytics.setString(key, "foo" /* string value */)

        Crashlytics.setBool(key, true /* boolean value */)

        Crashlytics.setDouble(key, 1.0 /* double value */)

        Crashlytics.setFloat(key, 1.0f /* float value */)

        Crashlytics.setInt(key, 1 /* int value */)
        // [END crash_set_keys_basic]
    }

    private fun resetKey() {
        // [START crash_re_set_key]
        Crashlytics.setInt("current_level", 3)
        Crashlytics.setString("last_UI_action", "logged_in")
        // [END crash_re_set_key]
    }

    private fun logReportAndPrint() {
        // [START crash_log_report_and_print]
        Crashlytics.log(Log.DEBUG, "tag", "message")
        // [END crash_log_report_and_print]
    }

    private fun logReportOnly() {
        // [START crash_log_report_only]
        Crashlytics.log("message")
        // [END crash_log_report_only]
    }

    private fun enableAtRuntime() {
        // [START crash_enable_at_runtime]
        Fabric.with(this, Crashlytics())
        // [END crash_enable_at_runtime]
    }

    private fun setUserId() {
        // [START crash_set_user_id]
        Crashlytics.setUserIdentifier("user123456789")
        // [END crash_set_user_id]
    }

    @Throws(Exception::class)
    private fun methodThatThrows() {
        throw Exception()
    }

    private fun logCaughtEx() {
        // [START crash_log_caught_ex]
        try {
            methodThatThrows()
        } catch (e: Exception) {
            Crashlytics.logException(e)
            // handle your exception here
        }
        // [END crash_log_caught_ex]
    }

    private fun enableDebugMode() {
        // [START crash_enable_debug_mode]
        val fabric = Fabric.Builder(this)
                .kits(Crashlytics())
                .debuggable(true) // Enables Crashlytics debugger
                .build()
        Fabric.with(fabric)
        // [END crash_enable_debug_mode]
    }

    private fun forceACrash() {
        // [START crash_force_crash]
        val crashButton = Button(this)
        crashButton.text = "Crash!"
        crashButton.setOnClickListener {
            Crashlytics.getInstance().crash() // Force a crash
        }

        addContentView(crashButton, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT))
        // [END crash_force_crash]
    }
}
