package com.google.firebase.quickstart.tasks.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.quickstart.tasks.R
import java.util.concurrent.ExecutionException
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

abstract class MainActivity : AppCompatActivity() {

    // [START basic_sign_in_task]
    private val task = Firebase.auth.signInAnonymously()
    // [END basic_sign_in_task]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun basicTaskHandlers() {
        // [START success_listener]
        task.addOnSuccessListener {
            // Task completed successfully
            // ...
        }
        // [END success_listener]

        // [START failure_listener]
        task.addOnFailureListener {
            // Task failed with an exception
            // ...
        }
        // [END failure_listener]

        // [START completion_listener]
        task.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Task completed successfully
                val result = task.result
            } else {
                // Task failed with an exception
                val exception = task.exception
            }
        }
        // [END completion_listener]

        // [START listener_try_catch]
        val signInTask = Firebase.auth.signInWithEmailAndPassword(
                "email@example.com", "mypassword1234")
        signInTask.addOnCompleteListener { task ->
            try {
                // Specific error information can be obtained by passing the expected
                // exception type into getResult(). In this case we expect a
                // FirebaseAuthException based on the documentation,
                val authResult = task.getResult(FirebaseAuthException::class.java)
            } catch (e: FirebaseAuthException) {
                // Task failed with FirebaseAuthException, which provides specific error
                // error information. such as the error code.
                val errorCode = e.errorCode
            }
        }
        // [END listener_try_catch]
    }

    private fun taskOnExecutor() {
        // [START create_handler_and_executor]
        // Create a new ThreadPoolExecutor with 2 threads for each processor on the
        // device and a 60 second keep-alive time.
        val numCores = Runtime.getRuntime().availableProcessors()
        val executor = ThreadPoolExecutor(numCores * 2, numCores *2,
                60L, TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>())
        // [END create_handler_and_executor]

        // [START tasks_run_task_executor]
        task.addOnCompleteListener(executor, OnCompleteListener {
            // ...
        })
        // [END tasks_run_task_executor]
    }

    private fun activityScopedTask() {
        // [START activity_scoped]
        val activity = this
        task.addOnCompleteListener(activity) {
            // ...
        }
        // [END activity_scoped]
    }

    // [START string_task_method]
    private fun doSomething(authResult: AuthResult?): Task<String> {
        // [START_EXCLUDE]
        return Tasks.forResult("Hello, World!")
        // [END_EXCLUDE]
    }
    // [END string_task_method]

    private fun taskChaining() {
        // [START task_chaining]
        val signInTask = Firebase.auth.signInAnonymously()

        signInTask.continueWithTask { task ->
            // Take the result from the first task and start the second one
            val result = task.result
            return@continueWithTask doSomething(result)
        }.addOnSuccessListener { s ->
            // Chain of tasks completed successfully, got result from last task.
            // ...
        }.addOnFailureListener { e ->
            // One of the tasks in the chain failed with an exception.
            // ...
        }
        // [END task_chaining]
    }

    private fun blockingTask() {
        // [START blocking_task]
        try {
            // Block on a task and get the result synchronously. This is generally done
            // when executing a task inside a separately managed background thread. Doing this
            // on the main (UI) thread can cause your application to become unresponsive.
            val authResult = Tasks.await(task)
        } catch (e: ExecutionException) {
            // The Task failed, this is the same exception you'd get in a non-blocking
            // failure handler.
            // ...
        } catch (e: InterruptedException) {
            // An interrupt occurred while waiting for the task to complete.
            // ...
        }
        // [END blocking_task]

        // [START blocking_task_timeout]
        try {
            // Block on the task for a maximum of 500 milliseconds, otherwise time out.
            val authResult = Tasks.await(task, 500, TimeUnit.MILLISECONDS)
        } catch (e: ExecutionException) {
            // ...
        } catch (e: InterruptedException) {
            // ...
        } catch (e: TimeoutException) {
            // Task timed out before it could complete.
            // ...
        }
        // [END blocking_task_timeout]
    }
}
