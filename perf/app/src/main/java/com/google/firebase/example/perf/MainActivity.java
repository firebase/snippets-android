package com.google.firebase.example.perf;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.example.perf.kotlin.model.Item;
import com.google.firebase.example.perf.kotlin.model.ItemCache;
import com.google.firebase.example.perf.kotlin.model.User;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.AddTrace;
import com.google.firebase.perf.metrics.HttpMetric;
import com.google.firebase.perf.metrics.Trace;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import devrel.firebase.google.com.firebaseoptions.R;

public class MainActivity extends AppCompatActivity {

    // [START perf_traced_create]
    @Override
    @AddTrace(name = "onCreateTrace", enabled = true /* optional */)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    // [END perf_traced_create]

    public void basicTrace() {
        ItemCache cache = new ItemCache();

        // [START perf_basic_trace_start]
        Trace myTrace = FirebasePerformance.getInstance().newTrace("test_trace");
        myTrace.start();
        // [END perf_basic_trace_start]

        // [START perf_basic_trace_increment]
        Item item = cache.fetch("item");
        if (item != null) {
            myTrace.incrementMetric("item_cache_hit", 1);
        } else {
            myTrace.incrementMetric("item_cache_miss", 1);
        }
        // [END perf_basic_trace_increment]

        // [START perf_basic_trace_stop]
        myTrace.stop();
        // [END perf_basic_trace_stop]
    }

    public void traceCustomAttributes() {
        // [START perf_trace_custom_attrs]
        Trace trace = FirebasePerformance.getInstance().newTrace("test_trace");

        // Update scenario.
        trace.putAttribute("experiment", "A");

        // Reading scenario.
        String experimentValue = trace.getAttribute("experiment");

        // Delete scenario.
        trace.removeAttribute("experiment");

        // Read attributes.
        Map<String, String> traceAttributes = trace.getAttributes();
        // [END perf_trace_custom_attrs]
    }

    public void disableWithConfig() {
        // [START perf_disable_with_config]
        // Setup remote config
        final FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();

        // You can uncomment the following two statements to permit more fetches when
        // validating your app, but you should comment out or delete these lines before
        // distributing your app in production.
        // FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
        //       .setMinimumFetchIntervalInSeconds(3600)
        //       .build();
        // config.setConfigSettingsAsync(configSettings);
        // Load in-app defaults from an XML file that sets perf_disable to false until you update
        // values in the Firebase Console

        //Observe the remote config parameter "perf_disable" and disable Performance Monitoring if true
        config.setDefaultsAsync(R.xml.remote_config_defaults)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (config.getBoolean("perf_disable")) {
                                FirebasePerformance.getInstance().setPerformanceCollectionEnabled(false);
                            } else {
                                FirebasePerformance.getInstance().setPerformanceCollectionEnabled(true);
                            }
                        } else {
                            // An error occurred while setting default parameters
                        }
                    }
                });
        // [END perf_disable_with_config]
    }

    public void activateConfig() {
        // [START perf_activate_config]
        //Remote Config fetches and activates parameter values from the service
        final FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();
        config.fetch(3600)
                .continueWithTask(new Continuation<Void, Task<Boolean>>() {
                    @Override
                    public Task<Boolean> then(@NonNull Task<Void> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return config.activate();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            // Parameter values successfully activated
                            // ...
                        } else {
                            // Handle errors
                        }
                    }
                });
        // [END perf_activate_config]
    }

    public void manualNetworkTrace() throws Exception {
        byte[] data = "badgerbadgerbadgerbadgerMUSHROOM!".getBytes();

        // [START perf_manual_network_trace]
        HttpMetric metric =
                FirebasePerformance.getInstance().newHttpMetric("https://www.google.com",
                        FirebasePerformance.HttpMethod.GET);
        final URL url = new URL("https://www.google.com");
        metric.start();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        try {
            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
            outputStream.write(data);
        } catch (IOException ignored) {
        }
        metric.setRequestPayloadSize(data.length);
        metric.setHttpResponseCode(conn.getResponseCode());
        printStreamContent(conn.getInputStream());

        conn.disconnect();
        metric.stop();
        // [END perf_manual_network_trace]
    }

    public void piiExamples() {
        Trace trace = FirebasePerformance.getInstance().newTrace("trace");
        User user = new User();

        // [START perf_attr_no_pii]
        trace.putAttribute("experiment", "A");
        // [END perf_attr_no_pii]

        // [START perf_attr_pii]
        trace.putAttribute("email", user.getEmailAddress());
        // [END perf_attr_pii]
    }

    public void printStreamContent(InputStream inputStream) {
        // Unimplemented
        // ...
    }

}
