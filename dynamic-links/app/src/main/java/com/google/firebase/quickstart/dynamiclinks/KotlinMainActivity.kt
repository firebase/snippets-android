package com.google.firebase.quickstart.dynamiclinks

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.quickstart.dynamiclinks.interfaces.MainActivityInterface

class KotlinMainActivity : AppCompatActivity(), MainActivityInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun createDynamicLink_Basic() {
        // [START create_link_basic]
        val dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.example.com/"))
                .setDynamicLinkDomain("example.page.link")
                // Open links with this app on Android
                .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(DynamicLink.IosParameters.Builder("com.example.ios").build())
                .buildDynamicLink()

        val dynamicLinkUri = dynamicLink.uri
        // [END create_link_basic]
    }

    override fun createDynamicLink_Advanced() {
        // [START create_link_advanced]
        val dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.example.com/"))
                .setDynamicLinkDomain("example.page.link")
                .setAndroidParameters(
                        DynamicLink.AndroidParameters.Builder("com.example.android")
                                .setMinimumVersion(125)
                                .build())
                .setIosParameters(
                        DynamicLink.IosParameters.Builder("com.example.ios")
                                .setAppStoreId("123456789")
                                .setMinimumVersion("1.0.1")
                                .build())
                .setGoogleAnalyticsParameters(
                        DynamicLink.GoogleAnalyticsParameters.Builder()
                                .setSource("orkut")
                                .setMedium("social")
                                .setCampaign("example-promo")
                                .build())
                .setItunesConnectAnalyticsParameters(
                        DynamicLink.ItunesConnectAnalyticsParameters.Builder()
                                .setProviderToken("123456")
                                .setCampaignToken("example-promo")
                                .build())
                .setSocialMetaTagParameters(
                        DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle("Example of a Dynamic Link")
                                .setDescription("This link works whether the app is installed or not!")
                                .build())
                .buildDynamicLink()  // Or buildShortDynamicLink()
        // [END create_link_advanced]
    }

    override fun createShortLink() {
        // [START create_short_link]
        val shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.example.com/"))
                .setDynamicLinkDomain("example.page.link")
                // Set parameters
                // ...
                .buildShortDynamicLink()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Short link created
                        val shortLink = task.result.shortLink
                        val flowchartLink = task.result.previewLink
                    } else {
                        // Error
                        // ...
                    }
                }
        // [END create_short_link]
    }

    override fun shortenLongLink() {
        // [START shorten_long_link]
        val shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse("https://example.page.link/?link=https://www.example.com/&apn=com.example.android&ibn=com.example.ios"))
                .buildShortDynamicLink()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Short link created
                        val shortLink = task.result.shortLink
                        val flowchartLink = task.result.previewLink
                    } else {
                        // Error
                        // ...
                    }
                }
        // [END shorten_long_link]
    }
}