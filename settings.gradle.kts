//enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }
    versionCatalogs {

        val kotlin = "1.8.10"
        val ksp = "1.8.10-1.0.9"
        val coroutines = "1.6.4"
        val gradle = "7.2.2"
        val maven = "2.0"

        // TOML Files
        create("androidx") {
            from(files("gradle/androidx.versions.toml"))
        }
        create("deps") {
            from(files("gradle/dependencies.versions.toml"))
        }
        create("compose") {
            from(files("gradle/compose.versions.toml"))
        }

        // Rest
        create("tools") {
            version("kotlin", kotlin)
            version("gradle", gradle)
            version("maven", maven)
            version("ksp", ksp)
        }
        create("app") {
            version("compileSdk", "33")
            version("minSdk", "21")
            version("targetSdk", "33")
        }
        create("libs") {
            // Kotlin
            library("kotlin", "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin")
            library("kotlin.coroutines", "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")
            library("kotlin.reflect", "org.jetbrains.kotlin:kotlin-reflect:$kotlin")
        }
    }
}

// --------------
// App
// --------------

include(":ComposeDialogs:Core")
project(":ComposeDialogs:Core").projectDir = file("library/core")
include(":ComposeDialogs:Modules:Info")
project(":ComposeDialogs:Modules:Info").projectDir = file("library/modules/info")
include(":ComposeDialogs:Modules:Input")
project(":ComposeDialogs:Modules:Input").projectDir = file("library/modules/input")

// TODO: defined the directories only
include(":ComposeDialogs:Modules:Ads")
project(":ComposeDialogs:Modules:Ads").projectDir = file("library/modules/ads")
include(":ComposeDialogs:Modules:Billing")
project(":ComposeDialogs:Modules:Billing").projectDir = file("library/modules/billing")
include(":ComposeDialogs:Modules:Color")
project(":ComposeDialogs:Modules:Color").projectDir = file("library/modules/color")
include(":ComposeDialogs:Modules:DateTime")
project(":ComposeDialogs:Modules:DateTime").projectDir = file("library/modules/datetime")
include(":ComposeDialogs:Modules:GDPR")
project(":ComposeDialogs:Modules:GDPR").projectDir = file("library/modules/gdpr")
include(":ComposeDialogs:Modules:List")
project(":ComposeDialogs:Modules:List").projectDir = file("library/modules/list")
include(":ComposeDialogs:Modules:Progress")
project(":ComposeDialogs:Modules:Progress").projectDir = file("library/modules/progress")

include(":demo")
project(":demo").projectDir = file("demo")
