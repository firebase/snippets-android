package com.google.firebase.example.predictions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.Map;

public class PreventChurnActivity extends AppCompatActivity {

    private static final long CACHE_EXPIRATION = 60 * 1000;

    private void preventChurn() {
        // [START pred_prevent_churn]
        final FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();

        Map<String, Object> remoteConfigDefaults = new HashMap<>();
        remoteConfigDefaults.put("grant_retention_gift", false);
        config.setDefaults(remoteConfigDefaults);

        // ...

        config.fetch(CACHE_EXPIRATION)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            config.activateFetched();
                        }

                        // Act on the retrieved parameters. For example, grant the
                        // retention gift to players who haven't yet received one.
                        boolean shouldGrantGift = config.getBoolean("grant_retention_gift");
                        if (shouldGrantGift && !playerAlreadyReceivedGift()) {
                            grantGiftToPlayer();
                        }
                    }
                });
        // [END pred_prevent_churn]
    }

    private boolean playerAlreadyReceivedGift() {
        // Dummy method
        return false;
    }

    private void grantGiftToPlayer() {
        // Dummy method
    }

}
