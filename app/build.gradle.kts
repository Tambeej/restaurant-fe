plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.tamar.restaurant"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.tamar.restaurant"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation (libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.play.services.auth)
    // Google Sign-In
    implementation(libs.play.services.auth.v2070)
    implementation(libs.credentials)
    implementation (libs.googleid)
    //QR
    implementation(libs.camera.core)
    implementation(libs.camera.camera2)
    implementation(libs.camera.lifecycle)
    implementation(libs.camera.view)
    implementation(libs.barcode.scanning)
}