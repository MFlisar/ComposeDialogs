import com.michaelflisar.kmpdevtools.Targets
import com.michaelflisar.kmpdevtools.configs.AppModuleConfig
import com.michaelflisar.kmpdevtools.configs.WasmAppConfig
import com.michaelflisar.kmpdevtools.core.Target

plugins {
    // kmp + app/library
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    // org.jetbrains.kotlin
    alias(libs.plugins.jetbrains.kotlin.compose)
    // org.jetbrains.compose
    alias(libs.plugins.jetbrains.compose)
    // build tools
    alias(mflisar.plugins.kmpdevtools.buildplugin)
}

// ------------------------
// Setup
// ------------------------

val module = AppModuleConfig.read(project)
val buildTargets = Targets(Target.WASM)
val wasmConfig = WasmAppConfig(
    moduleName = "demo",
    outputFileName = "demo.js"
)

// -------------------
// Kotlin
// -------------------

kotlin {

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    buildTargets.setupTargetsApp(module, wasmAppConfig = wasmConfig)

    sourceSets {

        commonMain.dependencies {
            implementation(project(":demo:shared"))
        }
    }
}
