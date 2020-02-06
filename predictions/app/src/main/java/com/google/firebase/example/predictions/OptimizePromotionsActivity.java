package com.google.firebase.example.predictions;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.Map;

public class OptimizePromotionsActivity extends AppCompatActivity {

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
        mConfig.setDefaultsAsync(remoteConfigDefaults)
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
        // [END pred_optimize_promotions_init]
    }

    private void fetchConfig() {
        // [START pred_optimize_promotions_fetch]
        mConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            // Act on the retrieved parameters

                            // Set the bundle to promote based on parameters retrieved with
                            // Remote Config. This depends entirely on your app, but for
                            // example, you might retrieve and use image assets based on the
                            // specified bundle name.
                            mPromotedBundle = mConfig.getString("promoted_bundle");
                            // ...
                        }
                    }
                });
        // [END pred_optimize_promotions_fetch]
    }
}
