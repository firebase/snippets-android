package com.google.firebase.example.appindexing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseUserActions;

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
    private void displayNoteDialog(final String positiveText, final String negativeText) {
        // ...

        // If you’re logging an action on content that hasn’t been added to the index yet,
        // add it first.
        // See <a href="https://firebase.google.com/docs/app-indexing/android/personal-content#update-the-index">https://firebase.google.com/docs/app-indexing/android/personal-content#update-the-index</a>.

        FirebaseUserActions.getInstance().end(getNoteCommentAction());
        // ...
    }

    private Action getNoteCommentAction() {
        return new Action.Builder(Action.Builder.COMMENT_ACTION)
                .setObject(mNote.getTitle(), mNote.getNoteUrl())
                // Keep action data for personal connulltent on the device
                .setMetadata(new Action.Metadata.Builder().setUpload(false))
                .build();
    }
    // [END appindexing_instantaneous]

    private class Note {

        public String getTitle() {
            return "";
        }

        public String getNoteUrl() {
            return "";
        }

    }

    private Action getRecipeViewAction() {
        // This is just to make some things compile.
        return null;
    }

}
