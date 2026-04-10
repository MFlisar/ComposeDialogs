plugins {
    // kmp + app/library
    alias(libs.plugins.jetbrains.kotlin.multiplatform) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    // org.jetbrains.kotlin
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.compose) apply false
    alias(libs.plugins.jetbrains.kotlin.parcelize) apply false
    alias(libs.plugins.jetbrains.kotlin.serialization) apply false
    // org.jetbrains.compose
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.jetbrains.compose.hotreload) apply false
    // docs, publishing, validation
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.vanniktech.maven.publish.base) apply false
    alias(libs.plugins.binary.compatibility.validator) apply false
    // build tools
    alias(mflisar.plugins.changelog.gradleplugin) apply false
    alias(mflisar.plugins.kmpicon.plugin) apply false
    alias(mflisar.plugins.kmpdevtools.buildplugin) // apply, because this plugin does register gradle tasks in the root project!
    alias(libs.plugins.buildkonfig) apply false
    // others
    // --
}