pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        // Ironsource library used for ad_impression snippets
        maven("https://android-sdk.is.com/")
    }
}

include(":app")
