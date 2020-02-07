package com.google.firebase.example.predictions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.Map;

public class PreventChurnActivity extends AppCompatActivity {

    private void preventChurn() {
        // [START pred_prevent_churn]
        final FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();

        Map<String, Object> remoteConfigDefaults = new HashMap<>();
        remoteConfigDefaults.put("grant_retention_gift", false);
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
                            // Act on the retrieved parameters. For example, grant the
                            // retention gift to players who haven't yet received one.
                            boolean shouldGrantGift = config.getBoolean("grant_retention_gift");
                            if (shouldGrantGift && !playerAlreadyReceivedGift()) {
                                grantGiftToPlayer();
                            }
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
