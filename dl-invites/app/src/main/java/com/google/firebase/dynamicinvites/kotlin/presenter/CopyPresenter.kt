package com.google.firebase.dynamicinvites.kotlin.presenter

import android.content.Context
import android.widget.Toast

import com.google.firebase.dynamicinvites.R
import com.google.firebase.dynamicinvites.kotlin.model.InviteContent

class CopyPresenter(isAvailable: Boolean, content: InviteContent) :
    InvitePresenter("Copy Link", R.drawable.ic_content_copy, isAvailable, content) {

    override fun sendInvite(context: Context) {
        super.sendInvite(context)
        Toast.makeText(context, "TODO: Implement link copying", Toast.LENGTH_SHORT).show()
    }
}
