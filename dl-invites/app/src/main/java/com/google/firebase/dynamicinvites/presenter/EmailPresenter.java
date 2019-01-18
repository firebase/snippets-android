package com.google.firebase.dynamicinvites.presenter;

import com.google.firebase.dynamicinvites.R;
import com.google.firebase.dynamicinvites.model.InviteContent;

public class EmailPresenter extends InvitePresenter {

    public EmailPresenter(boolean isAvailable, InviteContent content) {
        super("Email", R.drawable.ic_email_black_24dp, isAvailable, content);
    }

}
