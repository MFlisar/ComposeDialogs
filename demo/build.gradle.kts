plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
}

android {

    compileSdk = app.versions.compileSdk.get().toInt()

    buildFeatures {
        compose = true
    }

    defaultConfig {
        minSdk = app.versions.minSdk.get().toInt()
        targetSdk = app.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    composeOptions {
        kotlinCompilerExtensionVersion = compose.versions.compiler.get()
    }
}

dependencies {

    // ------------------------
    // Kotlin
    // ------------------------

    implementation(libs.kotlin)

    // ------------------------
    // AndroidX
    // ------------------------

    implementation(androidx.core)
    implementation(androidx.lifecycle)

    implementation(compose.compiler)
    implementation(compose.ui)
    implementation(compose.ui.tooling.preview)
    implementation(compose.lifecycle)
    implementation(compose.activity)
    implementation(compose.material)
    implementation(compose.material3)
    implementation(compose.theme.adapter)
    implementation(compose.theme.adapter.core)
    implementation(compose.material.extendedicons)
    implementation(compose.bottomsheet.dialog)

    // ------------------------
    // Libraries
    // ------------------------

    implementation(project(":ComposeDialogs:Core"))
    implementation(project(":ComposeDialogs:Info"))
    implementation(project(":ComposeDialogs:Input"))
}