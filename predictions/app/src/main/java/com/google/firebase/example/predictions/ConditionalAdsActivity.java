package com.google.firebase.example.predictions;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.Map;

public class ConditionalAdsActivity extends AppCompatActivity {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initRemoteConfig() {
        // [START pred_conditional_ads_init]
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        Map<String, Object> remoteConfigDefaults = new HashMap<>();
        remoteConfigDefaults.put("ads_enabled", true);
        mFirebaseRemoteConfig.setDefaultsAsync(remoteConfigDefaults)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Default value successfully set
                        } else {
                            // Failed to set default value
                        }
                    }
                });
        // [END pred_conditional_ads_init]
    }

    public void fetchRemoteConfig() {
        // [START pred_conditional_ads_fetch]
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            // Act on the retrieved parameters

                            // Show ads based on the ad policy retrieved with Remote Config
                            executeAdsPolicy();

                            // ...
                        } else {
                            // Handle errors
                        }
                    }
                });
        // [END pred_conditional_ads_fetch]
    }

    // [START pred_conditional_ads_policy]
    private void executeAdsPolicy() {
        boolean showAds = mFirebaseRemoteConfig.getBoolean("ads_enabled");
        AdView mAdView = findViewById(R.id.adView);

        if (showAds) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            mAdView.setVisibility(View.VISIBLE);
        } else {
            mAdView.setVisibility(View.GONE);
        }
    }
    // [END pred_conditional_ads_policy]
}
