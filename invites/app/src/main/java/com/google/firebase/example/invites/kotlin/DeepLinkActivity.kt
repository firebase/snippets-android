package com.google.firebase.example.invites.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import devrel.firebase.google.com.firebaseoptions.R
import kotlinx.android.synthetic.main.activity_deep_link.buttonOk
import kotlinx.android.synthetic.main.activity_deep_link.deepLinkText

/**
 * App Invites is deprecated, this file serves only to contain snippets that are still
 * referenced in some documentation.
 */
class DeepLinkActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deep_link)

        // Button click listener
        buttonOk.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()

        // Check for link in intent
        intent?.let {
            val data = it.data

            Log.d(TAG, "data:$data")
            deepLinkText.text = getString(R.string.deep_link_fmt, data.toString())
        }
    }

    override fun onClick(v: View) {
        val i = v.id
        if (i == R.id.buttonOk) {
            finish()
        }
    }

    companion object {
        private val TAG = DeepLinkActivity::class.java.simpleName
    }
}
