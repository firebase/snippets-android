package com.google.firebase.example.appindexing;

import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.appindexing.AndroidAppUri;

// [START appindexing_measure_activity]
public class MeasureActivity extends AppCompatActivity {

    @Override
    public Uri getReferrer() {

        // There is a built in function available from SDK>=22
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return super.getReferrer();
        }

        Intent intent = getIntent();
        Uri referrer = (Uri) intent.getExtras().get("android.intent.extra.REFERRER");
        if (referrer != null) {
            return referrer;
        }

        String referrerName = intent.getStringExtra("android.intent.extra.REFERRER_NAME");

        if (referrerName != null) {
            try {
                return Uri.parse(referrerName);
            } catch (ParseException e) {
                // ...
            }
        }

        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ...
        Uri referrer = getReferrer();

        if (referrer != null) {
            if (referrer.getScheme().equals("http") || referrer.getScheme().equals("https")) {
                // App was opened from a browser
                // host will contain the host path (e.g. www.google.com)
                String host = referrer.getHost();

                // Add analytics code below to track this click from web Search
                // ...

            } else if (referrer.getScheme().equals("android-app")) {
                // App was opened from another app
                AndroidAppUri appUri = AndroidAppUri.newAndroidAppUri(referrer);
                String referrerPackage = appUri.getPackageName();
                if ("com.google.android.googlequicksearchbox".equals(referrerPackage)) {
                    // App was opened from the Google app
                    // host will contain the host path (e.g. www.google.com)
                    String host = appUri.getDeepLinkUri().getHost();

                    // Add analytics code below to track this click from the Google app
                    // ...

                } else if ("com.google.appcrawler".equals(referrerPackage)) {
                    // Make sure this is not being counted as part of app usage
                    // ...
                }
            }
        }
        // ...
    }
}
// [END appindexing_measure_activity]
