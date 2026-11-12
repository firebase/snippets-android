/*
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.firebase.referencecode.storage;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class StorageActivity extends AppCompatActivity {
    private final String TAG = "java.StorageActivity";
    // [START storage_field_declaration]
    // [END storage_field_declaration]


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        // [START storage_field_initialization]
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // [END storage_field_initialization]

        includesForCreateReference();
    }

    public void includesForCreateReference() {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // ## Create a Reference

        // [START create_storage_reference]
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        // [END create_storage_reference]

        // [START create_child_reference]
        // Create a child reference
        // imagesRef now points to "images"
        StorageReference imagesRef = storageRef.child("images");

        // Child references can also take paths
        // spaceRef now points to "images/space.jpg
        // imagesRef still points to "images"
        StorageReference spaceRef = storageRef.child("images/space.jpg");
        // [END create_child_reference]

        // ## Navigate with References

        // [START navigate_references]
        // getParent allows us to move our reference to a parent node
        // imagesRef now points to 'images'
        imagesRef = spaceRef.getParent();

        // getRoot allows us to move all the way back to the top of our bucket
        // rootRef now points to the root
        StorageReference rootRef = spaceRef.getRoot();
        // [END navigate_references]

        // [START chain_navigation]
        // References can be chained together multiple times
        // earthRef points to 'images/earth.jpg'
        StorageReference earthRef = spaceRef.getParent().child("earth.jpg");

        // nullRef is null, since the parent of root is null
        StorageReference nullRef = spaceRef.getRoot().getParent();
        // [END chain_navigation]

        // ## Reference Properties

        // [START reference_properties]
        // Reference's path is: "images/space.jpg"
        // This is analogous to a file path on disk
        spaceRef.getPath();

        // Reference's name is the last segment of the full path: "space.jpg"
        // This is analogous to the file name
        spaceRef.getName();

        // Reference's bucket is the name of the storage bucket that the files are stored in
        spaceRef.getBucket();
        // [END reference_properties]

        // ## Full Example

        // [START reference_full_example]
        // Points to the root reference
        storageRef = storage.getReference();

        // Points to "images"
        imagesRef = storageRef.child("images");

        // Points to "images/space.jpg"
        // Note that you can use variables to create child values
        String fileName = "space.jpg";
        spaceRef = imagesRef.child(fileName);

        // File path is "images/space.jpg"
        String path = spaceRef.getPath();

        // File name is "space.jpg"
        String name = spaceRef.getName();

        // Points to "images"
        imagesRef = spaceRef.getParent();
        // [END reference_full_example]
    }

    public void includesForUploadFiles() throws FileNotFoundException {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // [START upload_create_reference]
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Create a reference to "mountains.jpg"
        StorageReference mountainsRef = storageRef.child("mountains.jpg");

        // Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = storageRef.child("images/mountains.jpg");

        // While the file names are the same, the references point to different files
        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false
        // [END upload_create_reference]


        ImageView imageView = (ImageView)findViewById(android.R.id.text1);

        // [START upload_memory]
        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
        // [END upload_memory]

        // [START upload_stream]
        InputStream stream = new FileInputStream(new File("path/to/images/rivers.jpg"));

        uploadTask = mountainsRef.putStream(stream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
        // [END upload_stream]

        // [START upload_file]
        Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
        uploadTask = riversRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
        // [END upload_file]

        // [START upload_with_metadata]
        // Create file metadata including the content type
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .build();

        // Upload the file and metadata
        uploadTask = storageRef.child("images/mountains.jpg").putFile(file, metadata);
        // [END upload_with_metadata]

        // [START manage_uploads]
        uploadTask = storageRef.child("images/mountains.jpg").putFile(file);

        // Pause the upload
        uploadTask.pause();

        // Resume the upload
        uploadTask.resume();

        // Cancel the upload
        uploadTask.cancel();
        // [END manage_uploads]

        // [START monitor_upload_progress]
        // Observe state change events such as progress, pause, and resume
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                Log.d(TAG, "Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Upload is paused");
            }
        });
        // [END monitor_upload_progress]

        // [START upload_complete_example]
        // File or Blob
        file = Uri.fromFile(new File("path/to/mountains.jpg"));

        // Create the file metadata
        metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();

        // Upload file and metadata to the path 'images/mountains.jpg'
        uploadTask = storageRef.child("images/"+file.getLastPathSegment()).putFile(file, metadata);

        // Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                Log.d(TAG, "Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Handle successful uploads on complete
                // ...
            }
        });
        // [END upload_complete_example]

        // [START upload_get_download_url]
        final StorageReference ref = storageRef.child("images/mountains.jpg");
        uploadTask = ref.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
        // [END upload_get_download_url]
    }

    public void includesForDownloadFiles() throws IOException {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // [START download_create_reference]
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Create a reference with an initial file path and name
        StorageReference pathReference = storageRef.child("images/stars.jpg");

        // Create a reference to a file from a Google Cloud Storage URI
        StorageReference gsReference = storage.getReferenceFromUrl("gs://bucket/images/stars.jpg");

        // Create a reference from an HTTPS URL
        // Note that in the URL, characters are URL escaped!
        StorageReference httpsReference = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/b/bucket/o/images%20stars.jpg");
        // [END download_create_reference]

        // [START download_to_memory]
        StorageReference islandRef = storageRef.child("images/island.jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        // [END download_to_memory]

        // [START download_to_local_file]
        islandRef = storageRef.child("images/island.jpg");

        File localFile = File.createTempFile("images", "jpg");

        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        // [END download_to_local_file]

        // [START download_via_url]
        storageRef.child("users/me/profile.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        // [END download_via_url]

        // [START download_full_example]
        storageRef.child("users/me/profile.png").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Use the bytes to display the image
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        // [END download_full_example]
    }

    public void includesForFileMetadata() {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // [START metadata_get_storage_reference]
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Get reference to the file
        StorageReference forestRef = storageRef.child("images/forest.jpg");
        // [END metadata_get_storage_reference]


        // [START get_file_metadata]
        forestRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                // Metadata now contains the metadata for 'images/forest.jpg'
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });
        // [END get_file_metadata]

        // [START update_file_metadata]
        // Create file metadata including the content type
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .setCustomMetadata("myCustomProperty", "myValue")
                .build();

        // Update metadata properties
        forestRef.updateMetadata(metadata)
                .addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        // Updated metadata is in storageMetadata
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred!
                    }
                });
        // [END update_file_metadata]
    }

    public void includesForMetadata_delete() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference forestRef = storageRef.child("images/forest.jpg");

        // [START delete_file_metadata]
        // Create file metadata with property to delete
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType(null)
                .build();

        // Delete the metadata property
        forestRef.updateMetadata(metadata)
                .addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        // metadata.contentType should be null
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred!
                    }
                });
        // [END delete_file_metadata]
    }

    public void includesForMetadata_custom() {
        // [START custom_metadata]
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setCustomMetadata("location", "Yosemite, CA, USA")
                .setCustomMetadata("activity", "Hiking")
                .build();
        // [END custom_metadata]
    }

    public void includesForDeleteFiles() {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // [START delete_file]
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Create a reference to the file to delete
        StorageReference desertRef = storageRef.child("images/desert.jpg");

        // Delete the file
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });
        // [END delete_file]
    }

    public void nonDefaultBucket() {
        // [START storage_non_default_bucket]
        // Get a non-default Storage bucket
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://my-custom-bucket");
        // [END storage_non_default_bucket]
    }

    public void customApp() {
        FirebaseApp customApp = FirebaseApp.initializeApp(this);

        // [START storage_custom_app]
        // Get the default bucket from a custom FirebaseApp
        FirebaseStorage storage = FirebaseStorage.getInstance(customApp);

        // Get a non-default bucket from a custom FirebaseApp
        FirebaseStorage customStorage = FirebaseStorage.getInstance(customApp, "gs://my-custom-bucket");
        // [END storage_custom_app]
    }

    public void listAllFiles() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // [START storage_list_all]
        StorageReference listRef = storage.getReference().child("files/uid");

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.
                        }

                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
        // [END storage_list_all]
    }

    // [START storage_list_paginated]
    public void listAllPaginated(@Nullable String pageToken) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference listRef = storage.getReference().child("files/uid");

        // Fetch the next page of results, using the pageToken if we have one.
        Task<ListResult> listPageTask = pageToken != null
                ? listRef.list(100, pageToken)
                : listRef.list(100);

        listPageTask
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        List<StorageReference> prefixes = listResult.getPrefixes();
                        List<StorageReference> items = listResult.getItems();

                        // Process page of results
                        // ...

                        // Recurse onto next page
                        if (listResult.getPageToken() != null) {
                            listAllPaginated(listResult.getPageToken());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred.
                    }
                });
    }
    // [END storage_list_paginated]

    // [START storage_custom_failure_listener]
    class MyFailureListener implements OnFailureListener {
        @Override
        public void onFailure(@NonNull Exception exception) {
            int errorCode = ((StorageException) exception).getErrorCode();
            String errorMessage = exception.getMessage();
            // test the errorCode and errorMessage, and handle accordingly
        }
    }
    // [END storage_custom_failure_listener]

}
