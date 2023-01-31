package com.google.firebase.dynamicinvites.kotlin.view

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.dynamicinvites.R
import com.google.firebase.dynamicinvites.kotlin.presenter.InvitePresenter

class AdvancedActivity : AppCompatActivity(), ShareDialogFragment.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advanced)

        findViewById<Button>(R.id.buttonShare).setOnClickListener {
            ShareDialogFragment.newInstance().show(supportFragmentManager, "dialog")
        }
    }

    override fun onItemClicked(presenter: InvitePresenter) {
        presenter.sendInvite(this)
    }
}
