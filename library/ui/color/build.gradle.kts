import com.michaelflisar.kmpdevtools.BuildFileUtil
import com.michaelflisar.kmpdevtools.Targets
import com.michaelflisar.kmpdevtools.configs.AndroidLibraryConfig
import com.michaelflisar.kmpdevtools.configs.LibraryModuleConfig
import com.michaelflisar.kmpdevtools.core.Target

plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.compose)
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
    Target.MACOS,
    Target.WASM
)
val androidConfig = AndroidLibraryConfig.create(
    libraryModuleConfig = module,
    compileSdk = app.versions.compileSdk,
    minSdk = app.versions.minSdk,
    enableAndroidResources = false
)

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
            api(libs.jetbrains.compose.runtime)
            api(libs.jetbrains.compose.foundation)
            api(libs.jetbrains.compose.ui)
            implementation(libs.jetbrains.compose.components.resources)
            implementation(libs.jetbrains.compose.animation)
            implementation(libs.jetbrains.compose.ui.backhandler)
        }
    }
}

// -------------------
// Publish
// -------------------

BuildFileUtil.setupMavenPublish(module)
