package com.google.firebase.dynamicinvites.kotlin.util

import android.net.Uri

import com.google.firebase.dynamicinvites.kotlin.model.InviteContent
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks

object DynamicLinksUtil {

    fun generateInviteContent(): InviteContent {
        return InviteContent(
                "Hey check out my great app!",
                "It's like the best app ever.",
                generateContentLink())
    }

    // [START ddl_generate_content_link]
    fun generateContentLink(): Uri {
        val baseUrl = Uri.parse("https://your-custom-name.page.link")
        val domain = "https://your-app.page.link"

        val link = FirebaseDynamicLinks.getInstance()
                .createDynamicLink()
                .setLink(baseUrl)
                .setDomainUriPrefix(domain)
                .setIosParameters(DynamicLink.IosParameters.Builder("com.your.bundleid").build())
                .setAndroidParameters(DynamicLink.AndroidParameters.Builder("com.your.packageName").build())
                .buildDynamicLink()

        return link.uri
    }
    // [END ddl_generate_content_link]
}
