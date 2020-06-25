package com.google.firebase.example.crashlytics;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setKeysBasic() {
        // [START crash_set_keys_basic]
        FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();

        crashlytics.setCustomKey("my_string_key", "foo" /* string value */);

        crashlytics.setCustomKey("my_bool_key", true /* boolean value */);

        crashlytics.setCustomKey("my_double_key", 1.0 /* double value */);

        crashlytics.setCustomKey("my_float_key", 1.0f /* float value */);

        crashlytics.setCustomKey("my_int_key", 1 /* int value */);
        // [END crash_set_keys_basic]
    }

    public void resetKey() {
        // [START crash_re_set_key]
        FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();

        crashlytics.setCustomKey("current_level", 3);
        crashlytics.setCustomKey("last_UI_action", "logged_in");
        // [END crash_re_set_key]
    }

    public void logReportAndPrint() {
        // [START crash_log_report_and_print]
        FirebaseCrashlytics.getInstance().log("message");
        // [END crash_log_report_and_print]
    }

    public void logReportOnly() {
        // [START crash_log_report_only]
        FirebaseCrashlytics.getInstance().log("message");
        // [END crash_log_report_only]
    }

    public void enableAtRuntime() {
        // [START crash_enable_at_runtime]
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        // [END crash_enable_at_runtime]
    }

    public void setUserId() {
        // [START crash_set_user_id]
        FirebaseCrashlytics.getInstance().setUserId("user123456789");
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
            FirebaseCrashlytics.getInstance().recordException(e);
            // handle your exception here
        }
        // [END crash_log_caught_ex]
    }

    public void forceACrash() {
        // [START crash_force_crash]
        Button crashButton = new Button(this);
        crashButton.setText("Crash!");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                throw new RuntimeException(); // Force a crash
            }
        });

        addContentView(crashButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        // [END crash_force_crash]
    }
}
