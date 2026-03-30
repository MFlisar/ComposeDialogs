import com.michaelflisar.kmpdevtools.core.configs.LibraryConfig

dependencyResolutionManagement {

    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven("https://jitpack.io")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        // jewel + skiko
        maven("https://www.jetbrains.com/intellij-repository/releases")
        maven("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies/")
    }

    versionCatalogs {
        create("app") {
            from(files("gradle/app.versions.toml"))
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
        mavenLocal()
    }
}

// --------------
// Settings Plugin
// --------------

plugins {
    // version catalogue does not work here!
    id("io.github.mflisar.kmpdevtools.plugins-settings-gradle") version "7.4.2"
}
val settingsPlugin = plugins.getPlugin(com.michaelflisar.kmpdevtools.SettingsFilePlugin::class.java)

// --------------
// Library
// --------------

val libraryConfig = LibraryConfig.read(rootProject)
val libraryId = libraryConfig.libraryId()

// Library Modules
settingsPlugin.includeModules(libraryId, libraryConfig, includeDokka = true)

// --------------
// App
// --------------

if (System.getenv("CI") != "true") {
    include(":demo:shared")
    include(":demo:app:android")
    include(":demo:app:compose")
}