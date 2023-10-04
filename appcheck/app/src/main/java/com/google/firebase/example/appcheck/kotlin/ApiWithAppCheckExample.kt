package com.google.firebase.example.appcheck.kotlin

import com.google.firebase.appcheck.appCheck
import com.google.firebase.Firebase
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
        Firebase.appCheck.getAppCheckToken(false).addOnSuccessListener { appCheckToken ->
            val token = appCheckToken.token
            val apiCall = yourExampleBackendService.exampleData(token)
            // ...
        }
    }
}
// [END appcheck_custom_backend]

class Misc {
    private fun getLimitedUseToken() {
        // [START appcheck_get_limited_use_token]
        Firebase.appCheck.limitedUseAppCheckToken.addOnSuccessListener {
            // ...
        }
        // [END appcheck_get_limited_use_token]
    }
}