package com.google.firebase.referencecode.storage;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;


public class UploadActivity extends AppCompatActivity {

    private StorageReference mStorageRef;  //mStorageRef was previously used to transfer data.
    private boolean mSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
    }

    // [START storage_upload_lifecycle]
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // If there's an upload in progress, save the reference so you can query it later
        if (mStorageRef != null) {
            outState.putString("reference", mStorageRef.toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // If there was an upload in progress, get its reference and create a new StorageReference
        final String stringRef = savedInstanceState.getString("reference");
        if (stringRef == null) {
            return;
        }
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(stringRef);

        // Find all UploadTasks under this StorageReference (in this example, there should be one)
        List<UploadTask> tasks = mStorageRef.getActiveUploadTasks();
        if (tasks.size() > 0) {
            // Get the task monitoring the upload
            UploadTask task = tasks.get(0);

            // Add new listeners to the task using an Activity scope
            task.addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot state) {
                    // Success!
                    // ...
                }
            });
        }
    }
    // [END storage_upload_lifecycle]

    public void continueAcrossRestarts() {
        Uri localFile = null;
        Uri sessionUri = null;
        UploadTask uploadTask;

        // [START save_before_restart]
        uploadTask = mStorageRef.putFile(localFile);
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                Uri sessionUri = taskSnapshot.getUploadSessionUri();
                if (sessionUri != null && !mSaved) {
                    mSaved = true;
                    // A persisted session has begun with the server.
                    // Save this to persistent storage in case the process dies.
                }
            }
        });
        // [END save_before_restart]

        // [START restore_after_restart]
        //resume the upload task from where it left off when the process died.
        //to do this, pass the sessionUri as the last parameter
        uploadTask = mStorageRef.putFile(localFile,
                new StorageMetadata.Builder().build(), sessionUri);
        // [END restore_after_restart]
    }
}
