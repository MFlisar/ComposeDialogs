import com.michaelflisar.kmpdevtools.SettingsFileUtil
import com.michaelflisar.kmpdevtools.core.configs.LibraryConfig

dependencyResolutionManagement    {

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
        create("mflisar") {
            from(files("gradle/mflisar.versions.toml"))
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
    id("io.github.mflisar.kmpdevtools.plugins-settings-gradle") version "7.9.2"
}

// --------------
// Library
// --------------

val libraryConfig = LibraryConfig.read(rootDir)
val libraryName = libraryConfig.libraryName()

// Library Modules
SettingsFileUtil.includeModules(settings, libraryName, libraryConfig)
SettingsFileUtil.includeDokkaModule(settings)

// --------------
// App
// --------------

if (System.getenv("CI") != "true") {
    SettingsFileUtil.includeModulesInFolder(settings, "demo")
}