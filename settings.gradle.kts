import com.michaelflisar.kmpdevtools.SettingsFileUtil
import com.michaelflisar.kmpdevtools.core.Config

dependencyResolutionManagement    {

    repositories {

        // main
        mavenCentral()
        google()
        gradlePluginPortal()

        // mine
        maven("https://raw.githubusercontent.com/MFlisar/maven-repository/refs/heads/main/artifacts")
        // local
        mavenLocal()

        // jetbrains
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        // jewel + skiko
        maven("https://www.jetbrains.com/intellij-repository/releases")
        maven("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies/")
        // jitpack
        maven("https://jitpack.io")
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

        // main
        mavenCentral()
        google()
        gradlePluginPortal()

        // mine
        maven("https://raw.githubusercontent.com/MFlisar/maven-repository/refs/heads/main/artifacts")
        // local
        mavenLocal()

        // jitpack
        maven("https://jitpack.io")
    }
}

// --------------
// Settings Plugin
// --------------

plugins {
    // version catalogue does not work here!
    id("io.github.mflisar.kmpdevtools.plugins-settings-gradle") version "9.3.3"
}

// --------------
// Library
// --------------

val libraryConfig = Config.read(rootDir)
val libraryName = libraryConfig.libraryName()

// Library Modules
SettingsFileUtil.includeModules(settings, libraryName, libraryConfig)
SettingsFileUtil.includeDokkaModule(settings)

// --------------
// App
// --------------

if (System.getenv("CI") != "true" || System.getenv("INCLUDE_DEMO_MODULES") == "true") {
   SettingsFileUtil.includeModulesInFolder(settings, "demo")
}
