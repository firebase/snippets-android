package com.google.firebase.example.appindexing;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Indexables;
import com.google.firebase.example.appindexing.model.Note;
import com.google.firebase.example.appindexing.model.Recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.concurrent.ExecutionException;

// [START appindexing_update_service]
public class AppIndexingUpdateService extends JobIntentService {

    // Job-ID must be unique across your whole app.
    private static final int UNIQUE_JOB_ID = 42;

    public static void enqueueWork(Context context) {
        enqueueWork(context, AppIndexingUpdateService.class, UNIQUE_JOB_ID, new Intent());
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        // TODO Insert your Indexable objects — for example, the recipe notes look as follows:

        ArrayList<Indexable> indexableNotes = new ArrayList<>();

        for (Recipe recipe : getAllRecipes()) {
            Note note = recipe.getNote();
            if (note != null) {
                Indexable noteToIndex = Indexables.noteDigitalDocumentBuilder()
                        .setName(recipe.getTitle() + " Note")
                        .setText(note.getText())
                        .setUrl(recipe.getNoteUrl())
                        .build();

                indexableNotes.add(noteToIndex);
            }
        }

        if (indexableNotes.size() > 0) {
            Indexable[] notesArr = new Indexable[indexableNotes.size()];
            notesArr = indexableNotes.toArray(notesArr);

            // batch insert indexable notes into index
            try {
                Tasks.await(FirebaseAppIndex.getInstance().update(notesArr));
            } catch (ExecutionException e) {
                // update failed
            } catch (InterruptedException e) {
                // await was interrupted
            }
        }
    }

    // [START_EXCLUDE]
    private List<Recipe> getAllRecipes() {
        return Collections.emptyList();
    }
    // [END_EXCLUDE]
}
// [END appindexing_update_service]
