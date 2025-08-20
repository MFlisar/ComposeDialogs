// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.gradle.maven.publish.plugin) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.binary.compatibility.validator) apply false
    alias(deps.plugins.kmplibrary.buildplugin) apply false
}

allprojects {

    // this task fails in github action - so I disable it...
    tasks.matching { task ->
        task.name.contains("javaDocReleaseGeneration", ignoreCase = true)
    }.configureEach {
        enabled = false
    }
}

// exclude all demo projects from CI builds
subprojects {
    if (project.path.contains(":demo:", ignoreCase = true) && System.getenv("CI") == "true") {
        tasks.configureEach {
            enabled = false
        }
    }
}

// ------------------------
// Build mkdocs
// ------------------------

buildscript {
    dependencies {
        classpath(deps.kmplibrary.docs)
    }
}

com.michaelflisar.kmplibrary.docs.registerBuildDocsTasks(
    tasks = tasks,
    project = project,
    relativeModulesPath = "library",
    relativeDemosPath = "demo"
)