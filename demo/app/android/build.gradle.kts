import com.michaelflisar.kmpdevtools.BuildFileUtil
import com.michaelflisar.kmpdevtools.configs.app.AndroidAppConfig
import com.michaelflisar.kmpdevtools.core.configs.AppConfig
import com.michaelflisar.kmpdevtools.core.configs.Config
import com.michaelflisar.kmpdevtools.core.configs.LibraryConfig

plugins {
    // kmp + app/library
    alias(libs.plugins.android.application)
    // org.jetbrains.kotlin
    alias(libs.plugins.jetbrains.kotlin.compose)
    // org.jetbrains.compose
    // --
    // docs, publishing, validation
    // --
    // build tools
    alias(deps.plugins.kmpdevtools.buildplugin)
    // others
    // ...
}

// ------------------------
// Setup
// ------------------------

val config = Config.read(rootProject)
val libraryConfig = LibraryConfig.read(rootProject)
val appConfig = AppConfig.read(rootProject)

val androidConfig = AndroidAppConfig(
    compileSdk = app.versions.compileSdk,
    minSdk = app.versions.minSdk,
    targetSdk = app.versions.targetSdk
)

// -------------------
// Configurations
// -------------------

android {

    BuildFileUtil.setupAndroidApp(
        project = project,
        config = config,
        appConfig = appConfig,
        androidAppConfig = androidConfig,
        generateResAppName = true,
        buildConfig = true,
        checkDebugKeyStoreProperty = true,
        setupBuildTypesDebugAndRelease = true
    )
}

dependencies {

    implementation(libs.androidx.activity.compose)

    // Library
    implementation(project(":demo:app:compose"))
}