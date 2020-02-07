package com.google.firebase.example.predictions;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void configShowAds() {
        // [START pred_config_show_ads]
        final FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();

        Map<String, Object> remoteConfigDefaults = new HashMap<>();
        remoteConfigDefaults.put("ads_policy", "ads_never");
        config.setDefaultsAsync(remoteConfigDefaults)
                .continueWithTask(new Continuation<Void, Task<Boolean>>() {
                    @Override
                    public Task<Boolean> then(@NonNull Task<Void> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return config.fetchAndActivate();
                    }
                })
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
        // [END pred_config_show_ads]
    }

    public void executeAdsPolicy() {
        // [START pred_ads_policy]
        FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();
        String adPolicy = config.getString("ads_policy");
        boolean will_not_spend = config.getBoolean("will_not_spend");
        AdView mAdView = findViewById(R.id.adView);

        if (adPolicy.equals("ads_always") ||
                (adPolicy.equals("ads_nonspenders") && will_not_spend)) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            mAdView.setVisibility(View.VISIBLE);
        } else {
            mAdView.setVisibility(View.GONE);
        }

        FirebaseAnalytics.getInstance(this).logEvent("ads_policy_set", new Bundle());
        // [END pred_ads_policy]
    }

    public void configPromoStrategy() {
        // [START config_promo_strategy]
        final FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();

        Map<String, Object> remoteConfigDefaults = new HashMap<>();
        remoteConfigDefaults.put("promoted_bundle", "basic");
        config.setDefaultsAsync(remoteConfigDefaults)
                .continueWithTask(new Continuation<Void, Task<Boolean>>() {
                    @Override
                    public Task<Boolean> then(@NonNull Task<Void> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return config.fetchAndActivate();
                    }
                })
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            // Act on the retrieved parameters
                            String promotedBundle =  getPromotedBundle();
                            // ...
                        } else {
                            // Handle errors
                        }
                    }
                });
        // [END config_promo_strategy]
    }

    // [START pred_get_promoted_bundle]
    public String getPromotedBundle() {
        FirebaseAnalytics.getInstance(this).logEvent("promotion_set", new Bundle());

        FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();
        String promotedBundle = config.getString("promoted_bundle");
        boolean will_spend = config.getBoolean("predicted_will_spend");

        if (promotedBundle.equals("predicted") && will_spend) {
            return "premium";
        } else {
            return promotedBundle;
        }
    }
    // [END pred_get_promoted_bundle]

    public void configPreventChurn() {
        // [START pred_config_prevent_churn]
        final FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();

        Map<String, Object> remoteConfigDefaults = new HashMap<>();
        remoteConfigDefaults.put("gift_policy", "gift_never");
        config.setDefaultsAsync(remoteConfigDefaults)
                .continueWithTask(new Continuation<Void, Task<Boolean>>() {
                    @Override
                    public Task<Boolean> then(@NonNull Task<Void> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return config.fetchAndActivate();
                    }
                })
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            // Act on the retrieved parameters
                            executeGiftPolicy();
                            // ...
                        } else {
                            // Handle errors
                        }
                    }
                });
        // [END pred_config_prevent_churn]
    }

    // [START pred_execute_gift_policy]
    public void executeGiftPolicy() {
        FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();
        String giftPolicy = config.getString("gift_policy");
        boolean willChurn = config.getBoolean("will_churn");

        if (giftPolicy.equals("gift_achievement")) {
            grantGiftOnLevel2();
        } else if (giftPolicy.equals("gift_likelychurn") && willChurn) {
            grantGiftNow();
        }

        FirebaseAnalytics.getInstance(this).logEvent("gift_policy_set", new Bundle());
    }
    // [END pred_execute_gift_policy]

    public void grantGiftOnLevel2() {
        // Nothing
    }

    public void grantGiftNow() {
        // Nothing
    }
}
