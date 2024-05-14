plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.google.firebase.example.mlkit"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.google.firebase.example.mlkit"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    lint {
        // MLKit libraries are deprecated
        abortOnError = false
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    packaging {
        resources.excludes += "META-INF/androidx.exifinterface_exifinterface.version"
        resources.excludes += "META-INF/proguard/androidx-annotations.pro"
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.exifinterface:exifinterface:1.3.5")
    implementation("com.google.firebase:firebase-ml-common:22.1.2")
    implementation("com.google.firebase:firebase-ml-model-interpreter:22.0.4")
    implementation("com.google.firebase:firebase-ml-vision:24.1.0")

    // Needed to fix a temporary issue with duplicate class com.google.android.gms.internal.vision.* errors
    // Image Labeling model.
    implementation("com.google.android.gms:play-services-vision:20.1.3")
    implementation("com.google.android.gms:play-services-vision-common:19.1.3")
}
