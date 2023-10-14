plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
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

    // Compose BOM
    implementation(platform(compose.bom))
    implementation(compose.material3)
    implementation(compose.material.extendedicons)

    implementation(compose.activity)
    implementation(compose.drawablepainter)

    // ------------------------
    // Libraries
    // ------------------------

    implementation(project(":ComposeDialogs:Core"))
    implementation(project(":ComposeDialogs:Modules:Info"))
    implementation(project(":ComposeDialogs:Modules:Input"))
    implementation(project(":ComposeDialogs:Modules:Time"))
    implementation(project(":ComposeDialogs:Modules:Date"))
    implementation(project(":ComposeDialogs:Modules:Progress"))
    implementation(project(":ComposeDialogs:Modules:Color"))
    implementation(project(":ComposeDialogs:Modules:List"))

    // TODO:
    implementation(project(":ComposeDialogs:Modules:Ads"))
    implementation(project(":ComposeDialogs:Modules:Billing"))
    implementation(project(":ComposeDialogs:Modules:GDPR"))
}