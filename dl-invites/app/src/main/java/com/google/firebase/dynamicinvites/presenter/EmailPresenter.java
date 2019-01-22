package com.google.firebase.dynamicinvites.presenter;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.dynamicinvites.R;
import com.google.firebase.dynamicinvites.kotlin.model.InviteContent;

public class EmailPresenter extends InvitePresenter {

    public EmailPresenter(boolean isAvailable, InviteContent content) {
        super("Email", R.drawable.ic_email, isAvailable, content);
    }

    @Override
    public void sendInvite(Context context) {
        super.sendInvite(context);
        Toast.makeText(context, "TODO: Implement email sending", Toast.LENGTH_SHORT).show();
    }
}
