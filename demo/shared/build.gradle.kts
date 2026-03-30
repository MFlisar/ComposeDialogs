import com.codingfeline.buildkonfig.compiler.FieldSpec.Type
import com.michaelflisar.kmpdevtools.Targets
import com.michaelflisar.kmpdevtools.configs.library.AndroidLibraryConfig
import com.michaelflisar.kmpdevtools.core.Platform
import com.michaelflisar.kmpdevtools.core.configs.AppConfig
import com.michaelflisar.kmpdevtools.core.configs.Config
import com.michaelflisar.kmpdevtools.core.configs.LibraryConfig
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
    // --
    // build tools
    alias(deps.plugins.kmpdevtools.buildplugin)
    alias(libs.plugins.buildkonfig)
    // others
    // ...
}

// ------------------------
// Setup
// ------------------------

val config = Config.read(rootProject)
val libraryConfig = LibraryConfig.read(rootProject)
val appConfig = AppConfig.read(rootProject)

val buildTargets = Targets(
    // mobile
    android = true,
    //iOS = true,
    // desktop
    windows = true,
    //macOS = true,
    // web
    wasm = true
)

val androidConfig = AndroidLibraryConfig.createManualNamespace(
    compileSdk = app.versions.compileSdk,
    minSdk = app.versions.minSdk,
    enableAndroidResources = true,
    namespaceAddon = "demo.shared"
)

// ------------------------
// Kotlin
// ------------------------

buildkonfig {
    packageName = appConfig.packageName
    exposeObjectWithName = "BuildKonfig"
    defaultConfigs {
        buildConfigField(Type.STRING, "versionName", appConfig.versionName)
        buildConfigField(Type.INT, "versionCode", appConfig.versionCode.toString())
        buildConfigField(Type.STRING, "packageName", appConfig.packageName)
        buildConfigField(Type.STRING, "appName", appConfig.name)
    }
}

kotlin {

    //-------------
    // Targets
    //-------------

    buildTargets.setupTargetsLibrary(project)
    android {
        buildTargets.setupTargetsAndroidLibrary(project, config, libraryConfig, androidConfig, this)
    }

    // -------
    // Sources
    // -------

    sourceSets {

        // ---------------------
        // custom source sets
        // ---------------------

        val notAndroidMain by creating { dependsOn(commonMain.get()) }

        setupDependencies(buildTargets, sourceSets) {

            notAndroidMain supportedBy !Platform.ANDROID

        }

        // ---------------------
        // dependencies
        // ---------------------

        commonMain.dependencies {

            // Kotlin
            api(libs.jetbrains.kotlinx.coroutines.core)

            // Compose
            api(libs.jetbrains.compose.material3)
            api(libs.jetbrains.compose.material.icons.core)
            api(libs.jetbrains.compose.material.icons.extended)
            api(libs.jetbrains.compose.ui.backhandler)

            // ------------------------
            // Libraries
            // ------------------------

            api(project(":composedialogs:core"))
            api(project(":composedialogs:modules:info"))
            api(project(":composedialogs:modules:input"))
            api(project(":composedialogs:modules:number"))
            api(project(":composedialogs:modules:time"))
            api(project(":composedialogs:modules:date"))
            api(project(":composedialogs:modules:progress"))
            api(project(":composedialogs:modules:color"))
            api(project(":composedialogs:modules:list"))
            api(project(":composedialogs:modules:menu"))
            api(project(":composedialogs:modules:frequency"))

            // demo ui composables
            api(deps.kmp.democomposables)

        }

        androidMain.dependencies {

            api(deps.drawablepainter)
        }
    }
}