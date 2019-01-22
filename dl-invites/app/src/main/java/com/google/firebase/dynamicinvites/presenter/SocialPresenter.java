package com.google.firebase.dynamicinvites.presenter;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.dynamicinvites.R;
import com.google.firebase.dynamicinvites.kotlin.model.InviteContent;

public class SocialPresenter extends InvitePresenter {

    public SocialPresenter(boolean isAvailable, InviteContent content) {
        super("Social", R.drawable.ic_people, isAvailable, content);
    }

    @Override
    public void sendInvite(Context context) {
        super.sendInvite(context);
        Toast.makeText(context, "TODO: Implement social sending", Toast.LENGTH_SHORT).show();
    }

}
