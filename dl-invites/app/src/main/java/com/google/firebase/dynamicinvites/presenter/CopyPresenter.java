package com.google.firebase.dynamicinvites.presenter;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.dynamicinvites.R;
import com.google.firebase.dynamicinvites.kotlin.model.InviteContent;

public class CopyPresenter extends InvitePresenter {

    public CopyPresenter(boolean isAvailable, InviteContent content) {
        super("Copy Link", R.drawable.ic_content_copy, isAvailable, content);
    }

    @Override
    public void sendInvite(Context context) {
        super.sendInvite(context);
        Toast.makeText(context, "TODO: Implement link copying", Toast.LENGTH_SHORT).show();
    }

}
