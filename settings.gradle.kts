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
// Library
// --------------

include(":composedialogs:core")
project(":composedialogs:core").projectDir = file("library/core")
include(":composedialogs:modules:info")
project(":composedialogs:modules:info").projectDir = file("library/modules/info")
include(":composedialogs:modules:input")
project(":composedialogs:modules:input").projectDir = file("library/modules/input")
include(":composedialogs:modules:number")
project(":composedialogs:modules:number").projectDir = file("library/modules/number")
include(":composedialogs:modules:color")
project(":composedialogs:modules:color").projectDir = file("library/modules/color")
include(":composedialogs:modules:date")
project(":composedialogs:modules:date").projectDir = file("library/modules/date")
include(":composedialogs:modules:time")
project(":composedialogs:modules:time").projectDir = file("library/modules/time")
include(":composedialogs:modules:list")
project(":composedialogs:modules:list").projectDir = file("library/modules/list")
include(":composedialogs:modules:progress")
project(":composedialogs:modules:progress").projectDir = file("library/modules/progress")
include(":composedialogs:modules:billing")
project(":composedialogs:modules:billing").projectDir = file("library/modules/billing")

include(":demo:android")
include(":demo:desktop")
