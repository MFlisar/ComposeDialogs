dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }
    versionCatalogs {

        val kotlin = "1.9.22"
        val ksp = "1.9.22-1.0.17"
        val coroutines = "1.7.3"
        val gradle = "8.3.1"

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
            version("ksp", ksp)
        }
        create("app") {
            version("compileSdk", "34")
            version("minSdk", "21")
            version("targetSdk", "34")
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
include(":ComposeDialogs:Modules:Number")
project(":ComposeDialogs:Modules:Number").projectDir = file("library/modules/number")
include(":ComposeDialogs:Modules:Color")
project(":ComposeDialogs:Modules:Color").projectDir = file("library/modules/color")
include(":ComposeDialogs:Modules:Date")
project(":ComposeDialogs:Modules:Date").projectDir = file("library/modules/date")
include(":ComposeDialogs:Modules:Time")
project(":ComposeDialogs:Modules:Time").projectDir = file("library/modules/time")
include(":ComposeDialogs:Modules:List")
project(":ComposeDialogs:Modules:List").projectDir = file("library/modules/list")
include(":ComposeDialogs:Modules:Progress")
project(":ComposeDialogs:Modules:Progress").projectDir = file("library/modules/progress")
include(":ComposeDialogs:Modules:Billing")
project(":ComposeDialogs:Modules:Billing").projectDir = file("library/modules/billing")

include(":demo")
project(":demo").projectDir = file("demo")
