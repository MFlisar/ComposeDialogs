dependencyResolutionManagement {

    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }

    versionCatalogs {
        create("app") {
            from(files("gradle/app.versions.toml"))
        }
        create("androidx") {
            from(files("gradle/androidx.versions.toml"))
        }
        create("kotlinx") {
            from(files("gradle/kotlinx.versions.toml"))
        }
        create("deps") {
            from(files("gradle/deps.versions.toml"))
        }
    }
}

pluginManagement {

    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }
}

// --------------
// Functions
// --------------

fun includeModule(path: String, name: String) {
    include(name)
    project(name).projectDir = file(path)
}

// --------------
// Library
// --------------

includeModule("library/core", ":composedialogs:core")
includeModule("library/modules/info", ":composedialogs:modules:info")
includeModule("library/modules/input", ":composedialogs:modules:input")
includeModule("library/modules/number", ":composedialogs:modules:number")
includeModule("library/modules/color", ":composedialogs:modules:color")
includeModule("library/modules/date", ":composedialogs:modules:date")
includeModule("library/modules/time", ":composedialogs:modules:time")
includeModule("library/modules/list", ":composedialogs:modules:list")
includeModule("library/modules/progress", ":composedialogs:modules:progress")
includeModule("library/modules/billing", ":composedialogs:modules:billing")
includeModule("library/modules/menu", ":composedialogs:modules:menu")
includeModule("library/modules/frequency", ":composedialogs:modules:frequency")

// --------------
// Demo
// --------------

include(":demo:shared")
include(":demo:app:windows")
include(":demo:app:android")
