import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose)
}

kotlin {

    jvm {
        withJava()
    }

    sourceSets {
        val jvmMain by getting {
            dependencies {

                implementation(compose.desktop.currentOs)

                implementation(libs.compose.material3)
                implementation(libs.compose.material.icons.core)
                implementation(libs.compose.material.icons.extended)

                implementation(project(":composedialogs:core"))
                implementation(project(":composedialogs:modules:color"))
                implementation(project(":composedialogs:modules:date"))
                implementation(project(":composedialogs:modules:time"))
                implementation(project(":composedialogs:modules:info"))
                implementation(project(":composedialogs:modules:progress"))
                implementation(project(":composedialogs:modules:input"))
                implementation(project(":composedialogs:modules:number"))
                implementation(project(":composedialogs:modules:list"))
                implementation(project(":composedialogs:modules:menu"))

                implementation(deps.toolbox.core)
                implementation(deps.toolbox.ui)
                //implementation(libs.toolbox.windows.app)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.michaelflisar.composedialogs.demo.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Exe)
            packageName = "ComposeDialogs JVM Demo"
            packageVersion = "1.0.0"
        }
    }
}