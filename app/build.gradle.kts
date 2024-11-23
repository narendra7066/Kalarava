plugins {
    id("com.android.application")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.Reva_Events"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.Reva_Events"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // Enabling ViewBinding
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    // AndroidX and Google libraries
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Google Maps and Location
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.2.0")
    implementation("com.google.maps.android:maps-utils-ktx:5.0.0")

    // Firebase
    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-messaging:23.4.1")

    // Dexter for permissions
    implementation("com.karumi:dexter:6.2.3")

    // Biometric API
    implementation("androidx.biometric:biometric:1.1.0")

    // Cronet for networking
    implementation("org.chromium.net:cronet-fallback:119.6045.31")
    implementation("org.chromium.net:cronet-api:119.6045.31")

    // Mail support (JavaMail)
    implementation("com.sun.mail:android-mail:1.6.5")
    implementation("com.sun.mail:android-activation:1.6.5")

    // Unit testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
