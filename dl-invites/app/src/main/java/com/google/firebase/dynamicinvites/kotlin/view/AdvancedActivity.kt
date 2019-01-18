package com.google.firebase.dynamicinvites.kotlin.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.google.firebase.dynamicinvites.R
import com.google.firebase.dynamicinvites.kotlin.presenter.InvitePresenter
import kotlinx.android.synthetic.main.activity_advanced.button_share

class AdvancedActivity : AppCompatActivity(), ShareDialogFragment.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advanced)

        button_share.setOnClickListener {
            ShareDialogFragment.newInstance().show(supportFragmentManager, "dialog")
        }
    }

    override fun onItemClicked(presenter: InvitePresenter) {
        presenter.sendInvite(this)
    }
}
