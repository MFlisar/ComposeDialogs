import com.michaelflisar.kmpdevtools.BuildFileUtil
import com.michaelflisar.kmpdevtools.Targets
import com.michaelflisar.kmpdevtools.configs.*
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
    alias(mflisar.plugins.kmpicon.plugin)
    alias(libs.plugins.launch4j)
}

// ------------------------
// Setup
// ------------------------

val module = AppModuleConfig.read(project)
val buildTargets = Targets(Target.JVM)

val desktopConfig = DesktopAppConfig(
    mainClass = "com.michaelflisar.demo.MainKt",
    ico = "icon.ico"
)

// -------------------
// Kotlin
// -------------------

kotlin {

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    buildTargets.setupTargetsApp(module)

    sourceSets {

        commonMain.dependencies {
            implementation(project(":demo:shared"))
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

compose.desktop {
    application {
        BuildFileUtil.setupWindowsApp(
            appModuleConfig = module,
            application = this,
            desktopAppConfig = desktopConfig
        )
    }
}

kmpIcon {
    setup {
        sourceModule = "demo/app/android"
        sourceFile = "src/main/ic_launcher-playstore.png"
    }
    generateIco {
        file = "icon.ico"
    }
}

BuildFileUtil.registerLaunch4JTask(appModuleConfig = module, desktopAppConfig = desktopConfig)
