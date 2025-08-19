import com.michaelflisar.kmpgradletools.BuildFilePlugin
import com.michaelflisar.kmpgradletools.Target
import com.michaelflisar.kmpgradletools.Targets

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(deps.plugins.kmp.gradle.tools.build.gradle.plugin)
}

// get build logic plugin
val buildFilePlugin = project.plugins.getPlugin(BuildFilePlugin::class.java)

// targets
val buildTargets = Targets(
    // mobile
    android = true,
    iOS = true,
    // desktop
    windows = true,
    macOS = false, // because of compose unstyled dialogs
    // web
    wasm = true
)

val androidNamespace = "com.michaelflisar.composedialogs.demo.shared"

// -------------------
// Setup
// -------------------

kotlin {

    //-------------
    // Targets
    //-------------

    buildFilePlugin.setupTargetsLibrary(buildTargets)

    // -------
    // Sources
    // -------

    sourceSets {

        // ---------------------
        // custom shared sources
        // ---------------------

        val notAndroidMain by creating { dependsOn(commonMain.get()) }

        // ---------------------
        // target sources
        // ---------------------

        buildTargets.updateSourceSetDependencies(sourceSets) { groupMain, target ->
            when (target) {
                Target.ANDROID,  -> {
                    // --
                }

                Target.WINDOWS, Target.IOS, Target.MACOS, Target.WASM -> {
                    groupMain.dependsOn(notAndroidMain)
                }

                Target.LINUX,
                Target.JS -> {
                    // not enabled
                }
            }
        }

        // ---------------------
        // dependencies
        // ---------------------

        commonMain.dependencies {

            // Kotlin
            implementation(kotlinx.coroutines.core)

            // Compose
            implementation(libs.compose.material3)
            implementation(libs.compose.material.icons.core)
            implementation(libs.compose.material.icons.extended)
            implementation(libs.compose.ui.backhandler)

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
            implementation(project(":composedialogs:modules:menu"))

        }

        androidMain.dependencies {

            implementation(deps.drawablepainter)

            implementation(project(":composedialogs:modules:billing"))
        }
    }
}

// -------------------
// Configurations
// -------------------

// android configuration
android {
    buildFilePlugin.setupAndroidLibrary(
        androidNamespace = androidNamespace,
        compileSdk = app.versions.compileSdk,
        minSdk = app.versions.minSdk,
        buildConfig = false
    )
}