package com.google.firebase.dynamicinvites.presenter;

import com.google.firebase.dynamicinvites.R;
import com.google.firebase.dynamicinvites.model.InviteContent;

public class SocialPresenter extends InvitePresenter {

    public SocialPresenter(boolean isAvailable, InviteContent content) {
        super("Social", R.drawable.ic_people_black_24dp, isAvailable, content);
    }

}
