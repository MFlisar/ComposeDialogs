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
    // AndroidX
    // ------------------------

    implementation(androidx.core)
    implementation(androidx.lifecycle)

    // Compose
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.core)
    implementation(libs.compose.material.icons.extended)

    implementation(androidx.activity.compose)

    // accompanist
    implementation(deps.drawablepainter)

    // ------------------------
    // Libraries
    // ------------------------

    implementation(project(":composedialogs:core"))
    implementation(project(":composedialogs:modules:info"))
    implementation(project(":composedialogs:modules:input"))
    implementation(project(":composedialogs:modules:number"))
    implementation(project(":composedialogs:modules:time"))
    implementation(project(":composedialogs:modules:date"))
    implementation(project(":composedialogs:modules:progress"))
    implementation(project(":composedialogs:modules:color"))
    implementation(project(":composedialogs:modules:list"))
    implementation(project(":composedialogs:modules:billing"))
    implementation(project(":composedialogs:modules:menu"))

    // ------------------------
    // Desugar
    // ------------------------

    coreLibraryDesugaring(deps.desugar)

    // ------------------------
    // Others
    // ------------------------
    
    // a minimal library that provides some useful composables that I use inside demo activities
    implementation(deps.toolbox.core)
    implementation(deps.toolbox.ui)
    implementation(deps.toolbox.android.demo.app)
}