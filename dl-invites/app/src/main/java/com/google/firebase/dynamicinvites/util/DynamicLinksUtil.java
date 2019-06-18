package com.google.firebase.dynamicinvites.util;

import android.net.Uri;

import com.google.firebase.dynamicinvites.kotlin.model.InviteContent;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

public class DynamicLinksUtil {

    public static InviteContent generateInviteContent() {
        return new InviteContent(
                "Hey check out my great app!",
                "It's like the best app ever.",
                generateContentLink());
    }

    // [START ddl_generate_content_link]
    public static Uri generateContentLink() {
        Uri baseUrl = Uri.parse("https://your-custom-name.page.link");
        String domain = "https://your-app.page.link";

        DynamicLink link = FirebaseDynamicLinks.getInstance()
                .createDynamicLink()
                .setLink(baseUrl)
                .setDomainUriPrefix(domain)
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.your.bundleid").build())
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.your.packageName").build())
                .buildDynamicLink();

        return link.getUri();
    }
    // [END ddl_generate_content_link]

}
