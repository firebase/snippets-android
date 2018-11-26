package com.google.firebase.example.predictions;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.Map;

public class OptimizePromotionsActivity extends AppCompatActivity {

    private static final long CACHE_EXPIRATION = 60 * 1000;

    private FirebaseRemoteConfig mConfig;
    private String mPromotedBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initConfig() {
        // [START pred_optimize_promotions_init]
        mConfig = FirebaseRemoteConfig.getInstance();

        Map<String, Object> remoteConfigDefaults = new HashMap<>();
        remoteConfigDefaults.put("promoted_bundle", "basic");
        mConfig.setDefaults(remoteConfigDefaults);
        // [END pred_optimize_promotions_init]
    }

    private void fetchConfig() {
        // [START pred_optimize_promotions_fetch]
        mConfig.fetch(CACHE_EXPIRATION)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mConfig.activateFetched();
                        }

                        // Act on the retrieved parameters

                        // Set the bundle to promote based on parameters retrieved with
                        // Remote Config. This depends entirely on your app, but for
                        // example, you might retrieve and use image assets based on the
                        // specified bundle name.
                        mPromotedBundle = mConfig.getString("promoted_bundle");
                        // ...
                    }
                });
        // [END pred_optimize_promotions_fetch]
    }
}
