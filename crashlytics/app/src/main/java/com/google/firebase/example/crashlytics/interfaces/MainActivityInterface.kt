package com.google.firebase.example.crashlytics.interfaces

import java.lang.Exception

/**
 * An interface to be implemented by the Activity.
 * Add any new method to this interface instead of adding it directly to the activity
 */
interface MainActivityInterface {
    fun setKeysBasic(key: String)
    fun resetKey()
    fun logReportAndPrint()
    fun logReportOnly()
    fun enableAtRuntime()
    fun setUserId()
    @Throws(Exception::class)
    fun methodThatThrows()
    fun logCaughtEx()
    fun enableDebugMode()
    fun forceACrash()
}