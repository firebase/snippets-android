package com.google.firebase.example.appindexing.kotlin

import android.support.v7.app.AppCompatActivity
import com.google.firebase.appindexing.Action
import com.google.firebase.appindexing.FirebaseAppIndex
import com.google.firebase.appindexing.FirebaseUserActions
import com.google.firebase.appindexing.builders.Indexables
import com.google.firebase.example.appindexing.model.Note
import com.google.firebase.example.appindexing.model.Recipe

class MainActivity : AppCompatActivity() {

    private lateinit var note: Note

    // [START appindexing_onstart_onstop]
    override fun onStart() {
        super.onStart()
        // If you’re logging an action on content that hasn’t been added to the index yet,
        // add it first.
        // See <a href="https://firebase.google.com/docs/app-indexing/android/personal-content#update-the-index">https://firebase.google.com/docs/app-indexing/android/personal-content#update-the-index</a>.

        FirebaseUserActions.getInstance().start(getRecipeViewAction())
    }

    override fun onStop() {
        FirebaseUserActions.getInstance().end(getRecipeViewAction())
        super.onStop()
    }
    // [END appindexing_onstart_onstop]

    // [START appindexing_instantaneous]
    fun displayNoteDialog(positiveText: String, negativeText: String) {
        // ...

        // If you’re logging an action on content that hasn’t been added to the index yet,
        // add it first.
        // See <a href="https://firebase.google.com/docs/app-indexing/android/personal-content#update-the-index">https://firebase.google.com/docs/app-indexing/android/personal-content#update-the-index</a>.

        FirebaseUserActions.getInstance().end(getNoteCommentAction())
        // ...
    }

    fun getNoteCommentAction(): Action {
        return Action.Builder(Action.Builder.COMMENT_ACTION)
                .setObject(note.title, note.noteUrl)
                // Keep action data for personal connulltent on the device
                .setMetadata(Action.Metadata.Builder().setUpload(false))
                .build()
    }
    // [END appindexing_instantaneous]

    // [START appindexing_update]
    fun indexNote(recipe: Recipe) {
        val note = recipe.note
        val noteToIndex = Indexables.noteDigitalDocumentBuilder()
                .setName(recipe.title)
                .setText(note!!.text)
                .setUrl(recipe.noteUrl)
                .build()

        val task = FirebaseAppIndex.getInstance().update(noteToIndex)
        // ...
    }
    // [END appindexing_update]

    fun removeAll() {
        // [START appindexing_remove_all]
        FirebaseAppIndex.getInstance().removeAll()
        // [END appindexing_remove_all]
    }

    fun getRecipeViewAction(): Action? {
        // This is just to make some things compile.
        return null
    }

}
