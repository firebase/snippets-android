package com.google.firebase.dynamicinvites.kotlin.presenter

import android.content.Context
import android.widget.Toast

import com.google.firebase.dynamicinvites.R
import com.google.firebase.dynamicinvites.kotlin.model.InviteContent

class SocialPresenter(isAvailable: Boolean, content: InviteContent) :
    InvitePresenter("Social", R.drawable.ic_people, isAvailable, content) {

    override fun sendInvite(context: Context) {
        super.sendInvite(context)
        Toast.makeText(context, "TODO: Implement social sending", Toast.LENGTH_SHORT).show()
    }
}
