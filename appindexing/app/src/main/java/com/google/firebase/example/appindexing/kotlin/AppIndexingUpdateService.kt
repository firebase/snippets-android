package com.google.firebase.example.appindexing.kotlin

import android.content.Context
import android.content.Intent
import android.support.v4.app.JobIntentService
import com.google.firebase.appindexing.FirebaseAppIndex
import com.google.firebase.appindexing.Indexable
import com.google.firebase.appindexing.builders.Indexables
import com.google.firebase.example.appindexing.model.Recipe

// [START appindexing_update_service]
class AppIndexingUpdateService : JobIntentService() {

    companion object {

        // Job-ID must be unique across your whole app.
        private const val UNIQUE_JOB_ID = 42

        fun enqueueWork(context: Context) {
            JobIntentService.enqueueWork(context, AppIndexingUpdateService::class.java, UNIQUE_JOB_ID, Intent())
        }
    }

    override fun onHandleWork(intent: Intent) {
        // TODO Insert your Indexable objects — for example, the recipe notes look as follows:

        val indexableNotes = arrayListOf<Indexable>()

        for (recipe in getAllRecipes()) {
            val note = recipe.note
            if (note != null) {
                val noteToIndex = Indexables.noteDigitalDocumentBuilder()
                        .setName(recipe.title + " Note")
                        .setText(note.text)
                        .setUrl(recipe.noteUrl)
                        .build()

                indexableNotes.add(noteToIndex)
            }
        }

        if (indexableNotes.size > 0) {
            val notesArr: Array<Indexable> = indexableNotes.toTypedArray()

            // batch insert indexable notes into index
            FirebaseAppIndex.getInstance().update(*notesArr)
        }
    }

    // [START_EXCLUDE]
    private fun getAllRecipes(): List<Recipe> {
        return emptyList()
    }
    // [END_EXCLUDE]
}
// [END appindexing_update_service]
