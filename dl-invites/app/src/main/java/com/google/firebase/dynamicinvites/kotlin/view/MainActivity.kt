package com.google.firebase.dynamicinvites.kotlin.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.dynamicinvites.R
import com.google.firebase.dynamicinvites.kotlin.util.DynamicLinksUtil

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.buttonShare).setOnClickListener {
            onShareClicked()
        }
    }

    // [START ddl_on_share_clicked]
    private fun onShareClicked() {
        val link = DynamicLinksUtil.generateContentLink()

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, link.toString())

        startActivity(Intent.createChooser(intent, "Share Link"))
    }
    // [END ddl_on_share_clicked]
}
