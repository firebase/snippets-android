package com.google.firebase.example.appindexing.interfaces

import com.google.firebase.appindexing.Action
import com.google.firebase.example.appindexing.model.Recipe

/**
 * An interface to be implemented by the Activity.
 * Add any new method to this interface instead of adding it directly to the activity
 */
interface MainActivityInterface {
    fun displayNoteDialog(positiveText : String, negativeText : String)
    fun getNoteCommentAction() : Action
    fun indexNote(recipe: Recipe)
    fun removeAll()
    fun getRecipeViewAction() : Action?
}