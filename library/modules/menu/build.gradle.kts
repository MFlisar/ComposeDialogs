import com.michaelflisar.kmpdevtools.BuildFileUtil
import com.michaelflisar.kmpdevtools.Targets
import com.michaelflisar.kmpdevtools.configs.*
import com.michaelflisar.kmpdevtools.core.Target

plugins {
    // kmp + app/library
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    // org.jetbrains.kotlin
    alias(libs.plugins.jetbrains.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.parcelize)
    // org.jetbrains.compose
    alias(libs.plugins.jetbrains.compose)
    // docs, publishing, validation
    alias(libs.plugins.dokka)
    alias(libs.plugins.vanniktech.maven.publish.base)
    alias(libs.plugins.binary.compatibility.validator)
    // build tools
    alias(mflisar.plugins.kmpdevtools.buildplugin)
}

// ------------------------
// Setup
// ------------------------

val module = LibraryModuleConfig.read(project)

val buildTargets = Targets(
    Target.ANDROID,
    Target.IOS,
    Target.JVM,
    Target.WASM
)

val androidConfig = AndroidLibraryConfig.create(
    libraryModuleConfig = module,
    compileSdk = app.versions.compileSdk,
    minSdk = app.versions.minSdk,
    enableAndroidResources = false
)

// -------------------
// Kotlin
// -------------------

kotlin {

    //-------------
    // Targets
    //-------------

    buildTargets.setupTargetsLibrary(module)
    android {
        buildTargets.setupTargetsAndroidLibrary(module, androidConfig, this)
    }

    // -------
    // Sources
    // -------

    sourceSets {

        // ---------------------
        // dependencies
        // ---------------------

        commonMain.dependencies {

            // library
            api(project(":composedialogs:ui:menu"))
            api(project(":composedialogs:implementation:material3"))
        }
    }
}

// -------------------
// Publish
// -------------------

BuildFileUtil.setupMavenPublish(module)
