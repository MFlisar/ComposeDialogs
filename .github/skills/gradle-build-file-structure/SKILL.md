---
name: gradle-build-file-structure
description: Structure and clean up Kotlin Multiplatform Gradle build files consistently while preserving behavior and existing comments.
---

# Gradle build file structure

Use this skill when creating, migrating, reviewing, or reorganizing a project's `build.gradle.kts` files. The goal is a clear, predictable section order without changing build behavior or discarding useful comments.

## General rules

1. Inspect the target script, related module scripts, version catalogs, and project configuration before editing. Determine whether the script is a library, application, shared module, root build, or Dokka module.
2. Keep the existing plugin versions, plugin aliases, targets, dependencies, configuration values, and task behavior unless the task explicitly requests a behavior change. Do not copy stale dependencies or settings from a neighboring module.
3. Preserve existing explanatory comments, section comments, commented-out code, and dependency examples. Keep comments with the code they describe when moving sections. Add only useful headings; do not replace meaningful comments with generic placeholders or add empty placeholder sections.
4. Use the repository's existing section-divider style. Separate major sections with blank lines and keep related declarations together.
5. Keep imports explicit when that is the repository's convention; do not broaden or remove imports as an incidental formatting change.
6. Make formatting-only changes behavior-neutral, then validate the edited scripts with the narrowest applicable Gradle configuration or task.

## Kotlin Multiplatform library modules

Use this order, omitting only sections that genuinely do not apply:

1. Imports.
2. `plugins`, with aliases grouped by purpose and short comments such as `kmp + app/library`, `org.jetbrains.kotlin`, `org.jetbrains.compose`, `docs, publishing, validation`, `build tools`, and `others`. Include only plugins actually used by the module.
3. `Setup`: read the module config, declare enabled targets, and configure Android or other module-specific settings.
4. `Kotlin`: compiler options when present, then a `Targets` subsection, then a `Sources` subsection.
5. Within `sourceSets`, declare custom source sets and their target support before dependency blocks. Group dependencies by purpose where that helps readability, retaining existing comments and commented-out examples.
6. A final `Publish` section for publishable modules.

Use multiline `Targets(...)` declarations for readability and list only the targets the module actually enables. Do not add a compiler option, custom source set, Android block, or dependency group unless the module needs it.

The standard outline is:

```kotlin
import com.michaelflisar.kmpdevtools.BuildFileUtil
import com.michaelflisar.kmpdevtools.Targets
import com.michaelflisar.kmpdevtools.configs.*
import com.michaelflisar.kmpdevtools.core.Target

plugins {
    // kmp + app/library
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    // org.jetbrains.kotlin
    alias(libs.plugins.jetbrains.kotlin.compose)
    // org.jetbrains.compose
    alias(libs.plugins.jetbrains.compose)
    // docs, publishing, validation
    alias(libs.plugins.dokka)
    alias(libs.plugins.vanniktech.maven.publish.base)
    alias(libs.plugins.binary.compatibility.validator)
    // build tools
    alias(mflisar.plugins.kmpdevtools.buildplugin)
}

// ------------------------
// Setup
// ------------------------

val module = LibraryModuleConfig.read(project)
val buildTargets = Targets(
    Target.ANDROID,
    Target.IOS,
    Target.JVM,
    Target.MACOS,
    Target.WASM
)

// -------------------
// Kotlin
// -------------------

kotlin {

    //-------------
    // Targets
    //-------------

    buildTargets.setupTargetsLibrary(module)
    android {
        // Configure the Android target when enabled.
    }

    // -------
    // Sources
    // -------

    sourceSets {

        // ---------------------
        // custom source sets
        // ---------------------

        // Declare only the custom source sets this module uses.

        // ---------------------
        // dependencies
        // ---------------------

        commonMain.dependencies {
            // Keep existing dependency groups and comments here.
        }
    }
}

// -------------------
// Publish
// -------------------

BuildFileUtil.setupMavenPublish(module)
```

The outline is illustrative, not a literal file to copy: remove inapplicable targets and configuration, and replace explanatory sample comments with the module's actual comments and code. Do not leave placeholder comments such as `// Configure the Android target when enabled.` in a real build file.

For every publishable library module, use exactly this final section, with no `if` condition or additional publish-specific comment:

```kotlin
// -------------------
// Publish
// -------------------

BuildFileUtil.setupMavenPublish(module)
```

## Other build file types

- **Multiplatform applications:** keep imports and grouped plugin aliases first, then `Setup`, followed by `Kotlin` with targets and source sets, then app-specific `Configurations` such as Android, Desktop, Wasm, icons, or launchers. Do not add the library `Publish` section unless that app is itself configured for publishing.
- **Android-only applications:** use imports, grouped plugins, `Setup`, `Configurations` for the Android block, and a separate `Dependencies` section.
- **Shared modules:** follow the library ordering where applicable, but omit publishing and place module-specific setup (for example BuildKonfig) in a clearly named configuration section before `Kotlin`.
- **Root build files:** keep the root `plugins` declarations grouped by purpose; do not add module-level `Setup`, `Kotlin`, or `Publish` sections.
- **Dokka module:** preserve the repository's canonical Dokka script exactly; do not add duplicate documentation setup.

## Verification

Run `projects` to verify settings and module configuration when Gradle is available. For build behavior changes, run the narrowest task that exercises the edited module. Report validation blockers plainly; do not hide unrelated pre-existing failures by broadening the edit.
