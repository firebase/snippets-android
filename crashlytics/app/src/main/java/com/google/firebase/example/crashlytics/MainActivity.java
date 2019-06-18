package com.google.firebase.example.crashlytics;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setKeysBasic(String key) {
        // [START crash_set_keys_basic]
        Crashlytics.setString(key, "foo" /* string value */);

        Crashlytics.setBool(key, true /* boolean value */);

        Crashlytics.setDouble(key, 1.0 /* double value */);

        Crashlytics.setFloat(key, 1.0f /* float value */);

        Crashlytics.setInt(key, 1 /* int value */);
        // [END crash_set_keys_basic]
    }

    public void resetKey() {
        // [START crash_re_set_key]
        Crashlytics.setInt("current_level", 3);
        Crashlytics.setString("last_UI_action", "logged_in");
        // [END crash_re_set_key]
    }

    public void logReportAndPrint() {
        // [START crash_log_report_and_print]
        Crashlytics.log(Log.DEBUG, "tag", "message");
        // [END crash_log_report_and_print]
    }

    public void logReportOnly() {
        // [START crash_log_report_only]
        Crashlytics.log("message");
        // [END crash_log_report_only]
    }

    public void enableAtRuntime() {
        // [START crash_enable_at_runtime]
        Fabric.with(this, new Crashlytics());
        // [END crash_enable_at_runtime]
    }

    public void setUserId() {
        // [START crash_set_user_id]
        Crashlytics.setUserIdentifier("user123456789");
        // [END crash_set_user_id]
    }

    public void methodThatThrows() throws Exception {
        throw new Exception();
    }

    public void logCaughtEx() {
        // [START crash_log_caught_ex]
        try {
            methodThatThrows();
        } catch (Exception e) {
            Crashlytics.logException(e);
            // handle your exception here
        }
        // [END crash_log_caught_ex]
    }

    public void enableDebugMode() {
        // [START crash_enable_debug_mode]
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)  // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);
        // [END crash_enable_debug_mode]
    }

    public void forceACrash() {
        // [START crash_force_crash]
        Button crashButton = new Button(this);
        crashButton.setText("Crash!");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Crashlytics.getInstance().crash(); // Force a crash
            }
        });

        addContentView(crashButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        // [END crash_force_crash]
    }

}
