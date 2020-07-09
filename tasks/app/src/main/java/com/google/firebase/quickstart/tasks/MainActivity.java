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
package com.google.firebase.quickstart.tasks;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {

    // [START basic_sign_in_task]
    Task<AuthResult> task = FirebaseAuth.getInstance().signInAnonymously();
    // [END basic_sign_in_task]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void basicTaskHandlers() {
        // [START success_listener]
        task.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // Task completed successfully
                // ...
            }
        });
        // [END success_listener]

        // [START failure_listener]
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Task failed with an exception
                // ...
            }
        });
        // [END failure_listener]

        // [START completion_listener]
        task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Task completed successfully
                    AuthResult result = task.getResult();
                } else {
                    // Task failed with an exception
                    Exception exception = task.getException();
                }
            }
        });
        // [END completion_listener]

        // [START listener_try_catch]
        Task<AuthResult> signInTask = FirebaseAuth.getInstance().signInWithEmailAndPassword(
                "email@example.com", "mypassword1234");
        signInTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                try {
                    // Specific error information can be obtained by passing the expected
                    // exception type into getResult(). In this case we expect a
                    // FirebaseAuthException based on the documentation,
                    AuthResult authResult = task.getResult(FirebaseAuthException.class);
                } catch (FirebaseAuthException e) {
                    // Task failed with FirebaseAuthException, which provides specific error
                    // error information. such as the error code.
                    String errorCode = e.getErrorCode();
                }
            }
        });
        // [END listener_try_catch]
    }

    public void taskOnExecutor() {
        // [START create_handler_and_executor]
        // Create a new ThreadPoolExecutor with 2 threads for each processor on the
        // device and a 60 second keep-alive time.
        int numCores = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(numCores * 2, numCores *2,
                60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        // [END create_handler_and_executor]

        // [START tasks_run_task_executor]
        task.addOnCompleteListener(executor, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // ...
            }
        });
        // [END tasks_run_task_executor]
    }

    public void activityScopedTask() {
        // [START activity_scoped]
        Activity activity = MainActivity.this;
        task.addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // ...
            }
        });
        // [END activity_scoped]
    }

    // [START string_task_method]
    public Task<String> doSomething(AuthResult authResult) {
        // [START_EXCLUDE]
        return Tasks.forResult("Hello, World!");
        // [END_EXCLUDE]
    }
    // [END string_task_method]

    public void taskChaining() {
        // [START task_chaining]
        Task<AuthResult> signInTask = FirebaseAuth.getInstance().signInAnonymously();

        signInTask.continueWithTask(new Continuation<AuthResult, Task<String>>() {
            @Override
            public Task<String> then(@NonNull Task<AuthResult> task) throws Exception {
                // Take the result from the first task and start the second one
                AuthResult result = task.getResult();
                return doSomething(result);
            }
        }).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                // Chain of tasks completed successfully, got result from last task.
                // ...
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // One of the tasks in the chain failed with an exception.
                // ...
            }
        });
        // [END task_chaining]
    }

    public void blockingTask() {
        // [START blocking_task]
        try {
            // Block on a task and get the result synchronously. This is generally done
            // when executing a task inside a separately managed background thread. Doing this
            // on the main (UI) thread can cause your application to become unresponsive.
            AuthResult authResult = Tasks.await(task);
        } catch (ExecutionException e) {
            // The Task failed, this is the same exception you'd get in a non-blocking
            // failure handler.
            // ...
        } catch (InterruptedException e) {
            // An interrupt occurred while waiting for the task to complete.
            // ...
        }
        // [END blocking_task]

        // [START blocking_task_timeout]
        try {
            // Block on the task for a maximum of 500 milliseconds, otherwise time out.
            AuthResult authResult = Tasks.await(task, 500, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            // ...
        } catch (InterruptedException e) {
            // ...
        } catch (TimeoutException e) {
            // Task timed out before it could complete.
            // ...
        }
        // [END blocking_task_timeout]
    }
}
