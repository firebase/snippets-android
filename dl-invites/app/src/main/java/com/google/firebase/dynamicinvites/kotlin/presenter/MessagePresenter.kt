package com.google.firebase.dynamicinvites.kotlin.presenter

import android.content.Context
import android.widget.Toast

import com.google.firebase.dynamicinvites.R
import com.google.firebase.dynamicinvites.kotlin.model.InviteContent

class MessagePresenter(isAvailable: Boolean, content: InviteContent) :
    InvitePresenter("Message", R.drawable.ic_sms, isAvailable, content) {

    override fun sendInvite(context: Context) {
        super.sendInvite(context)
        Toast.makeText(context, "TODO: Implement SMS", Toast.LENGTH_SHORT).show()
    }
}
