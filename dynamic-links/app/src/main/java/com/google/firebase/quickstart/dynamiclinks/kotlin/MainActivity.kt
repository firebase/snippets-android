package com.google.firebase.quickstart.dynamiclinks.kotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.appinvite.AppInviteReferral
import com.google.firebase.appinvite.FirebaseAppInvite
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.component1
import com.google.firebase.dynamiclinks.ktx.component2
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.googleAnalyticsParameters
import com.google.firebase.dynamiclinks.ktx.iosParameters
import com.google.firebase.dynamiclinks.ktx.itunesConnectAnalyticsParameters
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.dynamiclinks.ktx.socialMetaTagParameters
import com.google.firebase.ktx.Firebase
import com.google.firebase.quickstart.dynamiclinks.R

abstract class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun createDynamicLink_Basic() {
        // [START create_link_basic]
        val dynamicLink = Firebase.dynamicLinks.dynamicLink {
            link = Uri.parse("https://www.example.com/")
            domainUriPrefix = "https://example.page.link"
            // Open links with this app on Android
            androidParameters { }
            // Open links with com.example.ios on iOS
            iosParameters("com.example.ios") { }
        }

        val dynamicLinkUri = dynamicLink.uri
        // [END create_link_basic]
    }

    fun createDynamicLink_Advanced() {
        // [START create_link_advanced]
        val dynamicLink = Firebase.dynamicLinks.dynamicLink { // or Firebase.dynamicLinks.shortLinkAsync
            link = Uri.parse("https://www.example.com/")
            domainUriPrefix = "https://example.page.link"
            androidParameters("com.example.android") {
                minimumVersion = 125
            }
            iosParameters("com.example.ios") {
                appStoreId = "123456789"
                minimumVersion = "1.0.1"
            }
            googleAnalyticsParameters {
                source = "orkut"
                medium = "social"
                campaign = "example-promo"
            }
            itunesConnectAnalyticsParameters {
                providerToken = "123456"
                campaignToken = "example-promo"
            }
            socialMetaTagParameters {
                title = "Example of a Dynamic Link"
                description = "This link works whether the app is installed or not!"
            }
        }
        // [END create_link_advanced]
    }

    private fun processShortLink(shortLink: Uri?, previewLink: Uri?) {

    }

    fun createShortLink() {
        // [START create_short_link]
        val shortLinkTask = Firebase.dynamicLinks.shortLinkAsync {
            link = Uri.parse("https://www.example.com/")
            domainUriPrefix = "https://example.page.link"
            // Set parameters
            // ...
        }.addOnSuccessListener { (shortLink, flowchartLink) ->
            // You'll need to import com.google.firebase.dynamiclinks.ktx.component1 and
            // com.google.firebase.dynamiclinks.ktx.component2

            // Short link created
            processShortLink(shortLink, flowchartLink)
        }.addOnFailureListener {
            // Error
            // ...
        }
        // [END create_short_link]
    }

    fun shortenLongLink() {
        // [START shorten_long_link]
        val shortLinkTask = Firebase.dynamicLinks.shortLinkAsync {
            longLink = Uri.parse("https://example.page.link/?link=" +
                    "https://www.example.com/&apn=com.example.android&ibn=com.example.ios")
        }.addOnSuccessListener { (shortLink, flowChartLink) ->
            // You'll need to import com.google.firebase.dynamiclinks.ktx.component1 and
            // com.google.firebase.dynamiclinks.ktx.component2

            // Short link created
            processShortLink(shortLink, flowChartLink)
        }.addOnFailureListener {
            // Error
            // ...
        }
        // [END shorten_long_link]
    }

    fun buildShortSuffix() {
        // [START ddl_short_suffix]
        val shortLinkTask = Firebase.dynamicLinks.shortLinkAsync(ShortDynamicLink.Suffix.SHORT) {
            // Set parameters
            // ...
        }
        // [END ddl_short_suffix]
    }

    fun shareLink(myDynamicLink: Uri) {
        // [START ddl_share_link]
        val sendIntent = Intent().apply {
            val msg = "Hey, check this out: $myDynamicLink"
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, msg)
            type = "text/plain"
        }
        startActivity(sendIntent)
        // [END ddl_share_link]
    }

    fun getInvitation() {
        // [START ddl_get_invitation]
        Firebase.dynamicLinks
                .getDynamicLink(intent)
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        // Handle error
                        // ...
                    }

                    val invite = FirebaseAppInvite.getInvitation(task.result)
                    if (invite != null) {
                        // Handle invite
                        // ...
                    }
                }
        // [END ddl_get_invitation]
    }

    fun onboardingShare(dl: ShortDynamicLink) {
        // [START ddl_onboarding_share]
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Try this amazing app: ${dl.shortLink}")
        }
        startActivity(Intent.createChooser(intent, "Share using"))
        // [END ddl_onboarding_share]
    }
}
