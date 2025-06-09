plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsKotlinSerialization)
    alias(libs.plugins.googleDevtoolsKsp)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("kotlin-parcelize")
}

secrets {
    propertiesFileName = "secrets.properties"

    // A properties file containing default secret values. This file can be
    // checked in version control.
    defaultPropertiesFileName = "local.defaults.properties"
}

android {
    namespace = "com.example.hundredplaces"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.hundredplaces"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1,DEPENDENCIES,INDEX.LIST}"
        }
    }
}

dependencies {

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    //WindowSizeClass
    implementation(libs.androidx.material3.window.size)
    //ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    //Navhost and NavController
    implementation(libs.androidx.navigation.compose)
    //Datastore
    implementation(libs.androidx.datastore.preferences)
    //Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    //Retrofit
    implementation(libs.squareup.retrofit)
    //Kotlinx serialization fro JSON
    implementation(libs.jetbrains.kotlinx.serialization.json)
    //Retrofit with gson serialization converter
    implementation(libs.squareup.retrofit.converter.gson)
    //Maps and location libs
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)
    implementation(libs.maps.compose)
    implementation(libs.maps.compose.utils)
    implementation(libs.maps.compose.widgets)
    //Coil for Async image
    implementation(libs.coil.compose)
    //WorkManager dependency
    implementation(libs.androidx.work.runtime.ktx)
    //Adaptive layout
    implementation(libs.androidx.material3.adaptive)
    implementation(libs.androidx.material3.adaptive.layout)
    implementation(libs.androidx.material3.adaptive.navigation)
    //Camera libraries
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.compose)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.camera2)
    //MlKit for qr scanning
    implementation(libs.androidx.camera.mlkit)
    implementation(libs.google.mlkit.barcodeScanning)
    //Google cloud vision api
    implementation(libs.google.cloud.vision)
    implementation(libs.play.services.vision)
    //Google fonts
    implementation(libs.androidx.ui.text.google.fonts)

    testImplementation(libs.junit)
    //Coroutines test
    testImplementation(libs.jetbrains.kotlinx.coroutines.test)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}