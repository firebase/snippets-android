package com.google.firebase.dynamicinvites.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.google.firebase.dynamicinvites.R;
import com.google.firebase.dynamicinvites.model.InviteContent;

public class MorePresenter extends InvitePresenter {

    public MorePresenter(boolean isAvailable, InviteContent content) {
        super("More", R.drawable.ic_more_horiz_black_24dp, isAvailable, content);
    }

    @Override
    public void sendInvite(Context context) {
        // TODO: Dynamic link
        Uri link = Uri.parse("https://www.google.com");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, link.toString());

        context.startActivity(Intent.createChooser(intent, "Share Link"));
    }
}
