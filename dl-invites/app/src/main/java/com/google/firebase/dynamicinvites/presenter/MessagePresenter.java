package com.google.firebase.dynamicinvites.presenter;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.dynamicinvites.R;
import com.google.firebase.dynamicinvites.kotlin.model.InviteContent;

public class MessagePresenter extends InvitePresenter {

    public MessagePresenter(boolean isAvailable, InviteContent content) {
        super("Message", R.drawable.ic_sms, isAvailable, content);
    }

    @Override
    public void sendInvite(Context context) {
        super.sendInvite(context);
        Toast.makeText(context, "TODO: Implement SMS", Toast.LENGTH_SHORT).show();
    }

}
