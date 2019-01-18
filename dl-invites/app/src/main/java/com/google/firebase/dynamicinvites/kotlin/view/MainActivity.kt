package com.google.firebase.dynamicinvites.kotlin.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.dynamicinvites.R
import com.google.firebase.dynamicinvites.kotlin.util.DynamicLinksUtil
import kotlinx.android.synthetic.main.activity_main.button_share

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_share.setOnClickListener {
            onShareClicked()
        }
    }

    private fun onShareClicked() {
        val link = DynamicLinksUtil.generateContentLink()

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, link.toString())

        startActivity(Intent.createChooser(intent, "Share Link"))
    }
}
