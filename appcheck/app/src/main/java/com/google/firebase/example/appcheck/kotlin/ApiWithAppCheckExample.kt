package com.google.firebase.example.appcheck.kotlin

import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header

// [START appcheck_custom_backend]
class ApiWithAppCheckExample {
    interface YourExampleBackendService {
        @GET("yourExampleEndpoint")
        fun exampleData(
            @Header("X-Firebase-AppCheck") appCheckToken: String,
        ): Call<List<String>>
    }

    var yourExampleBackendService: YourExampleBackendService = Retrofit.Builder()
        .baseUrl("https://yourbackend.example.com/")
        .build()
        .create(YourExampleBackendService::class.java)

    suspend fun callApiExample() {
        val tokenResponse = Firebase.appCheck.getToken(false).await()
        val appCheckToken = tokenResponse.token
        val apiCall = yourExampleBackendService.exampleData(appCheckToken)
        // ...
    }
}
// [END appcheck_custom_backend]

class Misc {
    private suspend fun getLimitedUseToken() {
        // [START appcheck_get_limited_use_token]
        val tokenResponse = Firebase.appCheck.limitedUseToken.await()
        // [END appcheck_get_limited_use_token]
    }
}