package com.google.firebase.example.testlab.interfaces

import java.io.FileNotFoundException

/**
 * An interface to be implemented by the Activity.
 * Add any new method to this interface instead of adding it directly to the activity
 */
interface MainActivityInterface {

    fun checkEnvironment()
    fun gameCheckIntent()
    fun gameFinish()
    fun gameOutputFile()
    @Throws(FileNotFoundException::class)
    fun gameOutputFileDescriptor()
    fun gameScenario()
    fun takeScreenshot()

}