plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("maven-publish")
}

android {

    namespace = "com.michaelflisar.composedialogs.dialogs.time"

    compileSdk = app.versions.compileSdk.get().toInt()

    buildFeatures {
        compose = true
    }

    defaultConfig {
        minSdk = app.versions.minSdk.get().toInt()
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            consumerProguardFiles("proguard-rules.pro")
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

    composeOptions {
        kotlinCompilerExtensionVersion = compose.versions.compiler.get()
    }
}

dependencies {

    // ------------------------
    // Kotlin
    // ------------------------

    implementation(libs.kotlin)
    implementation(libs.kotlinx.coroutines)

    // ------------------------
    // AndroidX / Google / Goolge
    // ------------------------

    // Compose BOM
    implementation(platform(compose.bom))
    implementation(compose.material3)
    implementation(compose.activity)

    // ------------------------
    // Libraries
    // ------------------------

    implementation(project(":ComposeDialogs:Core"))

    // ------------------------
    // Desugar
    // ------------------------

    coreLibraryDesugaring(deps.desugar)
}

project.afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                artifactId = "dialog-time"
                from(components["release"])
            }
        }
    }
}