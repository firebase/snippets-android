package com.google.firebase.example.invites;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import devrel.firebase.google.com.firebaseoptions.R;

/**
 * App Invites is deprecated, this file serves only to contain snippets that are still
 * referenced in some documentation.
 */
public class DeepLinkActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = com.google.firebase.example.invites.kotlin.DeepLinkActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_link);

        // Button click listener
        findViewById(R.id.buttonOk).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check for link in intent
        if (getIntent() != null && getIntent().getData() != null) {
            Uri data = getIntent().getData();

            Log.d(TAG, "data:" + data);
            ((TextView) findViewById(R.id.deepLinkText))
                    .setText(getString(R.string.deep_link_fmt, data.toString()));
        }
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.buttonOk) {
            finish();
        }
    }
}
