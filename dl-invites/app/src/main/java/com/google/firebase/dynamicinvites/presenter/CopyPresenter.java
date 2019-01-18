package com.google.firebase.dynamicinvites.presenter;

import com.google.firebase.dynamicinvites.R;
import com.google.firebase.dynamicinvites.model.InviteContent;

public class CopyPresenter extends InvitePresenter {

    public CopyPresenter(boolean isAvailable, InviteContent content) {
        super("Copy Link", R.drawable.ic_content_copy_black_24dp, isAvailable, content);
    }

}
