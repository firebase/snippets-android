package com.google.firebase.example.appindexing;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Indexables;
import com.google.firebase.example.appindexing.model.Note;
import com.google.firebase.example.appindexing.model.Recipe;

public class MainActivity extends AppCompatActivity {

    private Note mNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // [START appindexing_onstart_onstop]
    @Override
    protected void onStart() {
        super.onStart();
        // If you’re logging an action on content that hasn’t been added to the index yet,
        // add it first.
        // See <a href="https://firebase.google.com/docs/app-indexing/android/personal-content#update-the-index">https://firebase.google.com/docs/app-indexing/android/personal-content#update-the-index</a>.

        FirebaseUserActions.getInstance().start(getRecipeViewAction());
    }

    @Override
    protected void onStop() {
        FirebaseUserActions.getInstance().end(getRecipeViewAction());
        super.onStop();
    }
    // [END appindexing_onstart_onstop]

    // [START appindexing_instantaneous]
    public void displayNoteDialog(final String positiveText, final String negativeText) {
        // ...

        // If you’re logging an action on content that hasn’t been added to the index yet,
        // add it first.
        // See <a href="https://firebase.google.com/docs/app-indexing/android/personal-content#update-the-index">https://firebase.google.com/docs/app-indexing/android/personal-content#update-the-index</a>.

        FirebaseUserActions.getInstance().end(getNoteCommentAction());
        // ...
    }

    public Action getNoteCommentAction() {
        return new Action.Builder(Action.Builder.COMMENT_ACTION)
                .setObject(mNote.getTitle(), mNote.getNoteUrl())
                // Keep action data for personal connulltent on the device
                .setMetadata(new Action.Metadata.Builder().setUpload(false))
                .build();
    }
    // [END appindexing_instantaneous]

    // [START appindexing_update]
    public void indexNote(Recipe recipe) {
        Note note = recipe.getNote();
        Indexable noteToIndex = Indexables.noteDigitalDocumentBuilder()
                .setName(recipe.getTitle())
                .setText(note.getText())
                .setUrl(recipe.getNoteUrl())
                .build();

        Task<Void> task = FirebaseAppIndex.getInstance().update(noteToIndex);
        // ...
    }
    // [END appindexing_update]

    private void removeNote(Recipe recipe) {
        // [START appindexing_remove_one]
        // Deletes or removes the corresponding notes from index.
        String noteUrl = recipe.getNoteUrl();
        FirebaseAppIndex.getInstance().remove(noteUrl);
        // [END appindexing_remove_one]
    }

    public void removeAll() {
        // [START appindexing_remove_all]
        FirebaseAppIndex.getInstance().removeAll();
        // [END appindexing_remove_all]
    }

    public Action getRecipeViewAction() {
        // This is just to make some things compile.
        return null;
    }

}
