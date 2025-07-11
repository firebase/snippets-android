plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp") version "2.2.0-2.0.2"
}

android {
    namespace = "com.google.firebase.referencecode.storage"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.google.firebase.referencecode.storage"
        minSdk = 21
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    lint {
        // TODO(thatfiredev): Remove this once
        //  https://github.com/bumptech/glide/issues/4940 is fixed
        disable += "NotificationPermission"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.appcompat:appcompat:1.7.1")

    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:33.16.0"))

    // Add the dependency for the Cloud Storage library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-storage")

    implementation("com.firebaseui:firebase-ui-storage:8.0.2")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    ksp("com.github.bumptech.glide:ksp:4.16.0")

    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
}
