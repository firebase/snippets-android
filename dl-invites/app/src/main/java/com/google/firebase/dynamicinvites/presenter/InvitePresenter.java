package com.google.firebase.dynamicinvites.presenter;

import android.content.Context;
import androidx.annotation.DrawableRes;

import com.google.firebase.dynamicinvites.kotlin.model.InviteContent;

// [START ddl_invite_presenter]

/**
 * Presents the invite using a specific method, such as email or social.
 */
public class InvitePresenter {

    /**
     * The user-visible name of the invite method, like 'Email' or 'SMS'
     **/
    public final String name;

    /**
     * An icon representing the invite method.
     **/
    @DrawableRes
    public final int icon;

    /**
     * Whether or not the method is available on this device. For example, SMS is phone only.
     **/
    public final boolean isAvailable;

    /**
     * The Content of the invitation
     **/
    public final InviteContent content;

    public InvitePresenter(String name, @DrawableRes int icon, boolean isAvailable, InviteContent content) {
        // [START_EXCLUDE]
        this.name = name;
        this.icon = icon;
        this.isAvailable = isAvailable;
        this.content = content;
        // [END_EXCLUDE]
    }

    /**
     * Send the invitation using the specified method.
     */
    public void sendInvite(Context context) {
        // ...
    }

}
// [END ddl_invite_presenter]
