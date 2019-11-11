package com.google.firebase.example.appindexing.kotlin

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.google.android.gms.tasks.Tasks
import com.google.firebase.appindexing.FirebaseAppIndex
import com.google.firebase.appindexing.Indexable
import com.google.firebase.appindexing.builders.Indexables
import com.google.firebase.example.appindexing.model.Recipe
import java.util.concurrent.ExecutionException

// [START appindexing_update_service]
class AppIndexingUpdateService : JobIntentService() {

    companion object {

        // Job-ID must be unique across your whole app.
        private const val UNIQUE_JOB_ID = 42

        fun enqueueWork(context: Context) {
            enqueueWork(context, AppIndexingUpdateService::class.java, UNIQUE_JOB_ID, Intent())
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
            try {
                Tasks.await(FirebaseAppIndex.getInstance().update(*notesArr))
            } catch (e: ExecutionException) {
                // update failed
            } catch (e: InterruptedException) {
                // await was interrupted
            }
        }
    }

    // [START_EXCLUDE]
    private fun getAllRecipes(): List<Recipe> {
        return emptyList()
    }
    // [END_EXCLUDE]
}
// [END appindexing_update_service]
