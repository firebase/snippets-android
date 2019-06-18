package com.google.firebase.dynamicinvites.model;

import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

// [START ddl_invite_content]

/**
 * The content of an invitation, with optional fields to accommodate all presenters.
 * This type could be modified to also include an image, for sending invites over email.
 */
public class InviteContent {

    /**
     * The subject of the message. Not used for invites without subjects, like SMS.
     **/
    @Nullable
    public final String subject;

    /**
     * The body of the message. Indispensable content should go here.
     **/
    @Nullable
    public final String body;

    /**
     * The URL containing the link to invite. In link-copy cases, only this field will be used.
     **/
    @NonNull
    public final Uri link;

    public InviteContent(@Nullable String subject, @Nullable String body, @NonNull Uri link) {
        // [START_EXCLUDE]
        this.subject = subject;
        this.body = body;
        this.link = link;
        // [END_EXCLUDE]
    }

}
// [END ddl_invite_content]
