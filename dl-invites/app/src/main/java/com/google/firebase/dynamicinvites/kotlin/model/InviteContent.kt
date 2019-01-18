package com.google.firebase.dynamicinvites.kotlin.model

import android.net.Uri

// [START ddl_invite_content]
/**
 * The content of an invitation, with optional fields to accommodate all presenters.
 * This type could be modified to also include an image, for sending invites over email.
 */
data class InviteContent(
    /** The subject of the message. Not used for invites without subjects, like SMS.  */
    val subject: String?,
    /** The body of the message. Indispensable content should go here.  */
    val body: String?,
    /** The URL containing the link to invite. In link-copy cases, only this field will be used.  */
    val link: Uri
)
// [END ddl_invite_content]
