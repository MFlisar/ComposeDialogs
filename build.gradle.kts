// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        mavenCentral()
        jcenter()
        google()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:" + tools.versions.gradle.get())
        classpath("com.github.dcendents:android-maven-gradle-plugin:" + tools.versions.maven.get())
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:" + tools.versions.kotlin.get())
        classpath("org.jetbrains.kotlin:kotlin-serialization:" + tools.versions.kotlin.get())
    }
}


allprojects {
    repositories {
        mavenCentral()
        jcenter()
        google()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }
}