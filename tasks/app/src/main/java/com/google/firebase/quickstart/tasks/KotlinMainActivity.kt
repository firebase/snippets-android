package com.google.firebase.quickstart.tasks

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.quickstart.tasks.interfaces.MainActivityInterface
import java.util.concurrent.*

class KotlinMainActivity : AppCompatActivity(), MainActivityInterface {

    // [START basic_sign_in_task]
    val task = FirebaseAuth.getInstance().signInAnonymously()
    // [END basic_sign_in_task]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun basicTaskHandlers() {
        // [START success_listener]
        task.addOnSuccessListener { authResult ->
            // Task completed successfully
            // ...
        }
        // [END success_listener]

        // [START failure_listener]
        task.addOnFailureListener { e ->
            // Task failed with an exception
            // ...
        }
        // [END failure_listener]

        // [START completion_listener]
        task.addOnCompleteListener { task ->
            if (task.isSuccessful()) {
                // Task completed successfully
                val result = task.result
            } else {
                // Task failed with an exception
                val exception = task.exception
            }
        }
        // [END completion_listener]

        // [START listener_try_catch]
        val signInTask = FirebaseAuth.getInstance().signInWithEmailAndPassword(
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

    override fun taskOnExecutor() {
        // [START create_handler_and_executor]
        // Create a new ThreadPoolExecutor with 2 threads for each processor on the
        // device and a 60 second keep-alive time.
        val numCores = Runtime.getRuntime().availableProcessors()
        val executor = ThreadPoolExecutor(numCores * 2, numCores *2,
                60L, TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>())
        // [END create_handler_and_executor]

        // [START run_task_executor]
        task.addOnCompleteListener(executor, OnCompleteListener { task ->
            // ...
        })
        // [END run_task_executor]
    }

    override fun activityScopedTask() {
        // [START activity_scoped]
        val activity = this
        task.addOnCompleteListener(activity, OnCompleteListener { task ->
            // ...
        })
        // [END activity_scoped]
    }

    override
    // [START string_task_method]
    fun doSomething(authResult: AuthResult): Task<String> {
        // [START_EXCLUDE]
        return Tasks.forResult("Hello, World!")
        // [END_EXCLUDE]
    }
    // [END string_task_method]

    override fun taskChaining() {
        // [START task_chaining]
        val signInTask = FirebaseAuth.getInstance().signInAnonymously()

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

    override fun blockingTask() {
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