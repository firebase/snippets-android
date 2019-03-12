package com.google.firebase.dynamicinvites.kotlin.presenter

import android.content.Context
import android.widget.Toast

import com.google.firebase.dynamicinvites.R
import com.google.firebase.dynamicinvites.kotlin.model.InviteContent

class EmailPresenter(isAvailable: Boolean, content: InviteContent) :
    InvitePresenter("Email", R.drawable.ic_email, isAvailable, content) {

    override fun sendInvite(context: Context) {
        super.sendInvite(context)
        Toast.makeText(context, "TODO: Implement email sending", Toast.LENGTH_SHORT).show()
    }
}
