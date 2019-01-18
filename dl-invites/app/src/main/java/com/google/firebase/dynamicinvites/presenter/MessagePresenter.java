package com.google.firebase.dynamicinvites.presenter;

import com.google.firebase.dynamicinvites.R;
import com.google.firebase.dynamicinvites.model.InviteContent;

public class MessagePresenter extends InvitePresenter {

    public MessagePresenter(boolean isAvailable, InviteContent content) {
        super("Message", R.drawable.ic_sms_black_24dp, isAvailable, content);
    }

}
