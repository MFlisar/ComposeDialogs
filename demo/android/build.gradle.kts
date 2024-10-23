plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    alias(libs.plugins.kotlin.compose)
}

android {

    namespace = "com.michaelflisar.composedialogs.demo"

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

    // eventually use local custom signing
    val debugKeyStore = providers.gradleProperty("debugKeyStore").orNull
    if (debugKeyStore != null) {
        signingConfigs {
            getByName("debug") {
                keyAlias = "androiddebugkey"
                keyPassword = "android"
                storeFile = File(debugKeyStore)
                storePassword = "android"
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
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

    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle)

    // Compose
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.core)
    implementation(libs.compose.material.icons.extended)

    implementation(libs.androidx.activity.compose)

    // accompanist
    implementation(libs.drawablepainter)

    // ------------------------
    // Libraries
    // ------------------------

    implementation(project(":ComposeDialogs:Core"))
    implementation(project(":ComposeDialogs:Modules:Info"))
    implementation(project(":ComposeDialogs:Modules:Input"))
    implementation(project(":ComposeDialogs:Modules:Number"))
    implementation(project(":ComposeDialogs:Modules:Time"))
    implementation(project(":ComposeDialogs:Modules:Date"))
    implementation(project(":ComposeDialogs:Modules:Progress"))
    implementation(project(":ComposeDialogs:Modules:Color"))
    implementation(project(":ComposeDialogs:Modules:List"))
    implementation(project(":ComposeDialogs:Modules:Billing"))

    // ------------------------
    // Desugar
    // ------------------------

    coreLibraryDesugaring(libs.desugar)

    // ------------------------
    // Others
    // ------------------------
    
    // a minimal library that provides some useful composables that I use inside demo activities
    implementation(libs.composedemobaseactivity)
}