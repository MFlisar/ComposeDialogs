import com.michaelflisar.kmpdevtools.BuildFileUtil
import com.michaelflisar.kmpdevtools.Targets
import com.michaelflisar.kmpdevtools.configs.*
import com.michaelflisar.kmpdevtools.core.Target
import com.michaelflisar.kmpdevtools.setupDependencies

plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.parcelize)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.dokka)
    alias(libs.plugins.vanniktech.maven.publish.base)
    alias(libs.plugins.binary.compatibility.validator)
    alias(mflisar.plugins.kmpdevtools.buildplugin)
}

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

kotlin {
    buildTargets.setupTargetsLibrary(module)
    android {
        buildTargets.setupTargetsAndroidLibrary(module, androidConfig, this)
    }

    sourceSets {
        val nativeMain by creating { dependsOn(commonMain.get()) }
        setupDependencies(module, buildTargets, sourceSets) {
            Target.LIST_APPLE addSourceSet nativeMain
        }

        commonMain {
            dependencies {
                api(project(":composedialogs:core"))
                api(libs.jetbrains.kotlinx.coroutines.core)
                api(libs.jetbrains.compose.material3)
                api(libs.jetbrains.compose.ui)
                api(libs.jetbrains.compose.ui.backhandler)
                implementation(libs.jetbrains.compose.material.icons.core)
                implementation(libs.jetbrains.compose.material.icons.extended)
                api(deps.composables.dialog)
                api(deps.composables.modal)
                api(deps.composables.modal.bottom.sheet)
            }
        }

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
    }
}

// -------------------
// Publish
// -------------------

BuildFileUtil.setupMavenPublish(module)
