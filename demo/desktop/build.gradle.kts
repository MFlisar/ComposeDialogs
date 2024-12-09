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

                implementation(project(":ComposeDialogs:Core"))
                implementation(project(":ComposeDialogs:Modules:Color"))
                implementation(project(":ComposeDialogs:Modules:Date"))
                implementation(project(":ComposeDialogs:Modules:Time"))
                implementation(project(":ComposeDialogs:Modules:Info"))
                implementation(project(":ComposeDialogs:Modules:Progress"))
                implementation(project(":ComposeDialogs:Modules:Input"))
                implementation(project(":ComposeDialogs:Modules:Number"))
                implementation(project(":ComposeDialogs:Modules:List"))

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