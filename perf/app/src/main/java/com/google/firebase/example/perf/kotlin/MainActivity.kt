package com.google.firebase.example.perf.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.example.perf.kotlin.model.ItemCache
import com.google.firebase.example.perf.kotlin.model.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.AddTrace
import com.google.firebase.remoteconfig.ktx.remoteConfig
import devrel.firebase.google.com.firebaseoptions.R
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    // [START perf_traced_create]
    @AddTrace(name = "onCreateTrace", enabled = true /* optional */)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    // [END perf_traced_create]

    fun basicTrace() {
        val cache = ItemCache()

        // [START perf_basic_trace_start]
        val myTrace = FirebasePerformance.getInstance().newTrace("test_trace")
        myTrace.start()
        // [END perf_basic_trace_start]

        // [START perf_basic_trace_increment]
        val item = cache.fetch("item")
        if (item != null) {
            myTrace.incrementMetric("item_cache_hit", 1)
        } else {
            myTrace.incrementMetric("item_cache_miss", 1)
        }
        // [END perf_basic_trace_increment]

        // [START perf_basic_trace_stop]
        myTrace.stop()
        // [END perf_basic_trace_stop]
    }

    fun traceCustomAttributes() {
        // [START perf_trace_custom_attrs]
        val trace = FirebasePerformance.getInstance().newTrace("test_trace")

        // Update scenario.
        trace.putAttribute("experiment", "A")

        // Reading scenario.
        val experimentValue = trace.getAttribute("experiment")

        // Delete scenario.
        trace.removeAttribute("experiment")

        // Read attributes.
        val traceAttributes = trace.attributes
        // [END perf_trace_custom_attrs]
    }

    fun disableWithConfig() {
        // [START perf_disable_with_config]
        // Setup remote config
        val config = Firebase.remoteConfig
        
        // You can uncomment the following two statements to permit more fetches when
        // validating your app, but you should comment out or delete these lines before
        // distributing your app in production.
        // val configSettings = remoteConfigSettings {
        //     minimumFetchIntervalInSeconds = 3600
        // }
        // config.setConfigSettingsAsync(configSettings)
        // Load in-app defaults from an XML file that sets perf_disable to false until you update
        // values in the Firebase Console

        // Observe the remote config parameter "perf_disable" and disable Performance Monitoring if true
        config.setDefaultsAsync(R.xml.remote_config_defaults)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        FirebasePerformance.getInstance()
                                .isPerformanceCollectionEnabled = !config.getBoolean("perf_disable")
                    } else {
                        // An error occurred while setting default parameters
                    }
                }
        // [END perf_disable_with_config]
    }

    fun activateConfig() {
        // [START perf_activate_config]
        // Remote Config fetches and activates parameter values from the service
        val config = Firebase.remoteConfig
        config.fetch(3600)
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    config.activate()
                }
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Parameter values successfully activated
                        // ...
                    } else {
                        // Handle errors
                    }
                }
        // [END perf_activate_config]
    }

    @Throws(Exception::class)
    fun manualNetworkTrace() {
        val data = "badgerbadgerbadgerbadgerMUSHROOM!".toByteArray()

        // [START perf_manual_network_trace]
        val metric = FirebasePerformance.getInstance().newHttpMetric("https://www.google.com",
                FirebasePerformance.HttpMethod.GET)
        val url = URL("https://www.google.com")
        metric.start()
        val conn = url.openConnection() as HttpURLConnection
        conn.doOutput = true
        conn.setRequestProperty("Content-Type", "application/json")
        try {
            val outputStream = DataOutputStream(conn.outputStream)
            outputStream.write(data)
        } catch (ignored: IOException) {
        }

        metric.setRequestPayloadSize(data.size.toLong())
        metric.setHttpResponseCode(conn.responseCode)
        printStreamContent(conn.inputStream)

        conn.disconnect()
        metric.stop()
        // [END perf_manual_network_trace]
    }

    fun piiExamples() {
        val trace = FirebasePerformance.getInstance().newTrace("trace")
        val user = User()

        // [START perf_attr_no_pii]
        trace.putAttribute("experiment", "A")
        // [END perf_attr_no_pii]

        // [START perf_attr_pii]
        trace.putAttribute("email", user.getEmailAddress())
        // [END perf_attr_pii]
    }

    private fun printStreamContent(stream: InputStream) {
        // Unimplemented
        // ...
    }
}
