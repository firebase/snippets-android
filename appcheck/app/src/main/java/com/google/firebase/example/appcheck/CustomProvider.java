package com.google.firebase.example.appcheck;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.AppCheckProvider;
import com.google.firebase.appcheck.AppCheckProviderFactory;
import com.google.firebase.appcheck.AppCheckToken;
import com.google.firebase.appcheck.FirebaseAppCheck;

public class CustomProvider {
    // [START appcheck_custom_provider]
    public class YourCustomAppCheckToken extends AppCheckToken {
        private String token;
        private long expiration;

        YourCustomAppCheckToken(String token, long expiration) {
            this.token = token;
            this.expiration = expiration;
        }

        @NonNull
        @Override
        public String getToken() {
            return token;
        }

        @Override
        public long getExpireTimeMillis() {
            return expiration;
        }
    }

    public class YourCustomAppCheckProvider implements AppCheckProvider {
        public YourCustomAppCheckProvider(FirebaseApp firebaseApp) {
            // ...
        }

        @NonNull
        @Override
        public Task<AppCheckToken> getToken() {
            // Logic to exchange proof of authenticity for an App Check token and
            //   expiration time.
            // [START_EXCLUDE]
            long expirationFromServer = 0L;
            String tokenFromServer = "token";
            // [END_EXCLUDE]

            // Refresh the token early to handle clock skew.
            long expMillis = expirationFromServer * 1000L - 60000L;

            // Create AppCheckToken object.
            AppCheckToken appCheckToken =
                    new YourCustomAppCheckToken(tokenFromServer, expMillis);

            return Tasks.forResult(appCheckToken);
        }
    }
    // [END appcheck_custom_provider]

    // [START appcheck_custom_provider_factory]
    public class YourCustomAppCheckProviderFactory implements AppCheckProviderFactory {
        @NonNull
        @Override
        public AppCheckProvider create(@NonNull FirebaseApp firebaseApp) {
            // Create and return an AppCheckProvider object.
            return new YourCustomAppCheckProvider(firebaseApp);
        }
    }
    // [END appcheck_custom_provider_factory]

    private void init(Context context) {
        // [START appcheck_initialize_custom_provider]
        FirebaseApp.initializeApp(/*context=*/ context);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                new YourCustomAppCheckProviderFactory());
        // [END appcheck_initialize_custom_provider]
    }
}
