package com.google.firebase.example.appcheck;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.appcheck.AppCheckToken;
import com.google.firebase.appcheck.FirebaseAppCheck;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Header;

// [START appcheck_custom_backend]
public class ApiWithAppCheckExample {
    private interface YourExampleBackendService {
        @GET("yourExampleEndpoint")
        Call<List<String>> exampleData(
                @Header("X-Firebase-AppCheck") String appCheckToken);
    }

    YourExampleBackendService yourExampleBackendService = new Retrofit.Builder()
            .baseUrl("https://yourbackend.example.com/")
            .build()
            .create(YourExampleBackendService.class);

    public void callApiExample() {
        FirebaseAppCheck.getInstance()
                .getAppCheckToken(/*forceRefresh=*/false)
                .addOnSuccessListener(new OnSuccessListener<AppCheckToken>() {
                    @Override
                    public void onSuccess(@NonNull AppCheckToken tokenResponse) {
                        String appCheckToken = tokenResponse.getToken();
                        Call<List<String>> apiCall =
                                yourExampleBackendService.exampleData(appCheckToken);
                        // ...
                    }
                });
    }
}
// [END appcheck_custom_backend]
