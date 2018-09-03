package com.google.firebase.example.perf.interfaces

import java.io.InputStream
import java.lang.Exception

/**
 * An interface to be implemented by the Activity.
 * Add any new method to this interface instead of adding it directly to the activity
 */
interface MainActivityInterface {

    fun basicTrace()
    fun traceCustomAttributes()
    fun disableWithConfig()
    fun activateConfig()
    @Throws(Exception::class)
    fun manualNetworkTrace()
    fun piiExamples()
    fun printStreamContent(stream: InputStream)

}