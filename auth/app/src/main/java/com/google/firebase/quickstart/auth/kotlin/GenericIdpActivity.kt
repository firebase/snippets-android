package com.google.firebase.quickstart.auth.kotlin

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider

class GenericIdpActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    fun twitter() {
        // [START auth_twitter_provider_create]
        val provider = OAuthProvider.newBuilder("twitter.com")
        // [END auth_twitter_provider_create]

        // [START auth_twitter_provider_params]
        // Localize to French.
        provider.addCustomParameter("lang", "fr")
        // [END auth_twitter_provider_params]
    }

    fun github() {
        // [START auth_github_provider_create]
        val provider = OAuthProvider.newBuilder("github.com")
        // [END auth_github_provider_create]

        // [START auth_github_provider_params]
        // Target specific email with login hint.
        provider.addCustomParameter("login", "your-email@gmail.com")
        // [END auth_github_provider_params]

        // [START auth_github_provider_scopes]
        // Request read access to a user's email addresses.
        // This must be preconfigured in the app's API permissions.
        provider.scopes = listOf("user:email")
        // [END auth_github_provider_scopes]
    }

    fun microsoft() {
        // [START auth_microsoft_provider_create]
        val provider = OAuthProvider.newBuilder("microsoft.com")
        // [END auth_microsoft_provider_create]

        // [START auth_microsoft_provider_params]
        // Target specific email with login hint.
        // Force re-consent.
        provider.addCustomParameter("prompt", "consent")

        // Target specific email with login hint.
        provider.addCustomParameter("login_hint", "user@firstadd.onmicrosoft.com")
        // [END auth_microsoft_provider_params]

        // [START auth_microsoft_provider_tenant]
        // Optional "tenant" parameter in case you are using an Azure AD tenant.
        // eg. '8eaef023-2b34-4da1-9baa-8bc8c9d6a490' or 'contoso.onmicrosoft.com'
        // or "common" for tenant-independent tokens.
        // The default value is "common".
        provider.addCustomParameter("tenant", "TENANT_ID")
        // [END auth_microsoft_provider_tenant]

        // [START auth_microsoft_provider_scopes]
        // Request read access to a user's email addresses.
        // This must be preconfigured in the app's API permissions.
        provider.scopes = listOf("mail.read", "calendars.read")
        // [END auth_microsoft_provider_scopes]
    }

    fun yahoo() {
        // [START auth_yahoo_provider_create]
        val provider = OAuthProvider.newBuilder("yahoo.com")
        // [END auth_yahoo_provider_create]


        // [START auth_yahoo_provider_params]
        // Prompt user to re-authenticate to Yahoo.
        provider.addCustomParameter("prompt", "login")

        // Localize to French.
        provider.addCustomParameter("language", "fr")
        // [END auth_yahoo_provider_params]


        // [START auth_yahoo_provider_scopes]
        // Request read access to a user's email addresses.
        // This must be preconfigured in the app's API permissions.
        provider.scopes = listOf("mail-r", "sdct-w")
        // [END auth_yahoo_provider_scopes]
    }

    fun oidc() {
        // [START auth_oidc_provider_create]
        val providerBuilder = OAuthProvider.newBuilder("oidc.example-provider")
        // [END auth_oidc_provider_create]


        // [START auth_oidc_provider_params]
        // Target specific email with login hint.
        providerBuilder.addCustomParameter("login_hint", "user@example.com")
        // [END auth_oidc_provider_params]


        // [START auth_oidc_provider_scopes]
        // Request read access to a user's email addresses.
        // This must be preconfigured in the app's API permissions.
        providerBuilder.scopes = listOf("mail.read", "calendars.read")
        // [END auth_oidc_provider_scopes]
    }

    fun getPendingAuthResult() {
        // [START auth_oidc_pending_result]
        val pendingResultTask = firebaseAuth.pendingAuthResult
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                .addOnSuccessListener {
                    // User is signed in.
                    // IdP data available in
                    // authResult.getAdditionalUserInfo().getProfile().
                    // The OAuth access token can also be retrieved:
                    // ((OAuthCredential)authResult.getCredential()).getAccessToken().
                    // The OAuth secret can be retrieved by calling:
                    // ((OAuthCredential)authResult.getCredential()).getSecret().
                }
                .addOnFailureListener {
                    // Handle failure.
                }
        } else {
            // There's no pending result so you need to start the sign-in flow.
            // See below.
        }
        // [END auth_oidc_pending_result]
    }

    fun signInWithProvider(provider: OAuthProvider.Builder) {
        // [START auth_oidc_provider_signin]
        firebaseAuth
            .startActivityForSignInWithProvider( /* activity = */this, provider.build())
            .addOnSuccessListener {
                // User is signed in.
                // IdP data available in
                // authResult.getAdditionalUserInfo().getProfile().
                // The OAuth access token can also be retrieved:
                // ((OAuthCredential)authResult.getCredential()).getAccessToken().
                // The OAuth secret can be retrieved by calling:
                // ((OAuthCredential)authResult.getCredential()).getSecret().
            }
            .addOnFailureListener {
                // Handle failure.
            }
        // [END auth_oidc_provider_signin]
    }

    fun linkWithProvider(provider: OAuthProvider.Builder) {
        // [START auth_oidc_provider_link]
        // The user is already signed-in.
        val firebaseUser = firebaseAuth.currentUser!!
        firebaseUser
            .startActivityForLinkWithProvider( /* activity = */this, provider.build())
            .addOnSuccessListener {
                // Provider credential is linked to the current user.
                // IdP data available in
                // authResult.getAdditionalUserInfo().getProfile().
                // The OAuth access token can also be retrieved:
                // authResult.getCredential().getAccessToken().
                // The OAuth secret can be retrieved by calling:
                // authResult.getCredential().getSecret().
            }
            .addOnFailureListener {
                // Handle failure.
            }
        // [END auth_oidc_provider_link]
    }

    fun reauthenticateWithProvider(provider: OAuthProvider.Builder) {
        // The user is already signed-in.
        val firebaseUser = firebaseAuth.currentUser!!
        firebaseUser
            .startActivityForReauthenticateWithProvider( /* activity = */this, provider.build())
            .addOnSuccessListener {
                // User is re-authenticated with fresh tokens and
                // should be able to perform sensitive operations
                // like account deletion and email or password
                // update.
            }
            .addOnFailureListener {
                // Handle failure.
            }
    }
}