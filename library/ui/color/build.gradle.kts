import com.michaelflisar.kmpdevtools.BuildFileUtil
import com.michaelflisar.kmpdevtools.Targets
import com.michaelflisar.kmpdevtools.configs.*
import com.michaelflisar.kmpdevtools.core.Target
import com.michaelflisar.kmpdevtools.setupDependencies

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
    // others
    // ...
}

// ------------------------
// Setup
// ------------------------

val module = LibraryModuleConfig.read(project)

val buildTargets = Targets(
    Target.ANDROID,
    Target.IOS,
    Target.JVM,
    Target.WASM,
    Target.MACOS
)

val androidConfig = AndroidLibraryConfig.create(
    libraryModuleConfig = module,
    compileSdk = app.versions.compileSdk,
    minSdk = app.versions.minSdk,
    enableAndroidResources = true
)

// -------------------
// Kotlin
// -------------------

compose.resources {
    packageOfResClass = "${module.projectNamespace}.color.resources"
    publicResClass = true
}

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
        // custom source sets
        // ---------------------

        // --

        // ---------------------
        // dependencies
        // ---------------------

        commonMain.dependencies {

            // Kotlin
            implementation(libs.jetbrains.kotlinx.coroutines.core)

            // Compose + AndroidX
            implementation(libs.jetbrains.compose.animation)
            implementation(libs.jetbrains.compose.components.resources)
            implementation(libs.jetbrains.compose.foundation)
            implementation(libs.jetbrains.compose.ui.backhandler)

            // Color Palette
            implementation(mflisar.composecolors.material)
            implementation(mflisar.composecolors.material.palette)

            // library
            api(project(":composedialogs:core"))

            implementation(mflisar.kmp.parcelize)

        }
    }
}

// -------------------
// Publish
// -------------------

BuildFileUtil.setupMavenPublish(module)