import com.michaelflisar.kmpdevtools.BuildFileUtil

plugins {
    kotlin("jvm") apply false
    alias(libs.plugins.dokka)
    alias(mflisar.plugins.kmpdevtools.buildplugin)
}

BuildFileUtil.setupDokka(project)