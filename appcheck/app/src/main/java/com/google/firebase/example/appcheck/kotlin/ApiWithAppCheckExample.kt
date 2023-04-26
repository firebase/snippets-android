package com.google.firebase.example.appcheck.kotlin

import com.google.firebase.appcheck.FirebaseAppCheck
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

    fun callApiExample() {
        FirebaseAppCheck.getInstance()
            .getAppCheckToken(false)
            .addOnSuccessListener { tokenResponse ->
                val appCheckToken = tokenResponse.token
                val apiCall = yourExampleBackendService.exampleData(appCheckToken)
                // ...
            }
    }
}
// [END appcheck_custom_backend]
