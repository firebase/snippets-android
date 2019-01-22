package com.google.firebase.dynamicinvites.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.google.firebase.dynamicinvites.R;
import com.google.firebase.dynamicinvites.kotlin.model.InviteContent;
import com.google.firebase.dynamicinvites.util.DynamicLinksUtil;

public class MorePresenter extends InvitePresenter {

    public MorePresenter(boolean isAvailable, InviteContent content) {
        super("More", R.drawable.ic_more_horiz, isAvailable, content);
    }

    @Override
    public void sendInvite(Context context) {
        super.sendInvite(context);
        Uri link = DynamicLinksUtil.generateContentLink();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, link.toString());

        context.startActivity(Intent.createChooser(intent, "Share Link"));
    }
}
