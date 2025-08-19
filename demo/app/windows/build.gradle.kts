import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose)
}

kotlin {

    jvm()

    sourceSets {
        val jvmMain by getting {
            dependencies {

                implementation(compose.desktop.currentOs)

                implementation(libs.compose.material3)
                implementation(libs.compose.material.icons.core)
                implementation(libs.compose.material.icons.extended)

                implementation(project(":composedialogs:core"))
                implementation(project(":demo:shared"))

            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.michaelflisar.composedialogs.demo.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Exe)
            packageName = "Compose Dialogs Demo"
            packageVersion = "1.0.0"
        }
    }
}