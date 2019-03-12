package com.google.firebase.dynamicinvites.kotlin.presenter

import android.content.Context
import android.content.Intent
import com.google.firebase.dynamicinvites.R
import com.google.firebase.dynamicinvites.kotlin.model.InviteContent
import com.google.firebase.dynamicinvites.util.DynamicLinksUtil

class MorePresenter(isAvailable: Boolean, content: InviteContent) :
    InvitePresenter("More", R.drawable.ic_more_horiz, isAvailable, content) {

    override fun sendInvite(context: Context) {
        super.sendInvite(context)
        val link = DynamicLinksUtil.generateContentLink()

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, link.toString())

        context.startActivity(Intent.createChooser(intent, "Share Link"))
    }
}
