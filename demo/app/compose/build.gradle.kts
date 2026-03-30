import com.michaelflisar.kmpdevtools.Targets
import com.michaelflisar.kmpdevtools.BuildFileUtil
import com.michaelflisar.kmpdevtools.core.Platform
import com.michaelflisar.kmpdevtools.configs.library.AndroidLibraryConfig
import com.michaelflisar.kmpdevtools.configs.app.DesktopAppConfig
import com.michaelflisar.kmpdevtools.configs.app.WasmAppConfig
import com.michaelflisar.kmpdevtools.core.configs.AppConfig
import com.michaelflisar.kmpdevtools.core.configs.Config
import com.michaelflisar.kmpdevtools.core.configs.LibraryConfig

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
    // others
    // --
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
    namespaceAddon = "demo.app.compose"
)

val desktopConfig = DesktopAppConfig(
    mainClass = "com.michaelflisar.demo.MainKt",
    ico = "icon.ico"
)

val wasmConfig = WasmAppConfig(
    moduleName = "demo",
    outputFileName = "demo.js"
)

// -------------------
// Kotlin
// -------------------

dependencies {
    coreLibraryDesugaring(libs.desugar)
}

kotlin {

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    //-------------
    // Targets
    //-------------

    buildTargets.setupTargetsApp(project, wasmAppConfig = wasmConfig)
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

        // --

        // ---------------------
        // dependencies
        // ---------------------

        commonMain.dependencies {

            api(project(":demo:shared"))

        }

        jvmMain.dependencies {

            implementation(compose.desktop.currentOs) {
                exclude(group = "org.jetbrains.compose.material", module = "material")
            }

        }
    }
}

// -------------------
// Configurations
// -------------------

// windows configuration
compose.desktop {
    application {
        BuildFileUtil.setupWindowsApp(
            project = project,
            config = config,
            application = this,
            appConfig = appConfig,
            desktopAppConfig = desktopConfig
        )
    }
}