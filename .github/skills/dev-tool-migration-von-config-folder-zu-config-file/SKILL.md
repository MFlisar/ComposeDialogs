---
name: dev-tool-migration-von-config-folder-zu-config-file
description: Migrate a Kotlin Multiplatform library to kmp-devtools 9.3.0, replacing legacy configs/*.yml with .config.yml, preserving structured Gradle build files and comments, migrating settings and APIs, splitting demo targets, and replacing GitHub workflows.
---

# Dev tool migration von config folder zu config file

Use this skill when a project is moving from separate files under `configs/` to a root `.config.yml`, especially when upgrading kmp-devtools.

## Migration procedure

1. Inspect the complete legacy config set, the root `.config.yml` template, the kmp-devtools version in `settings.gradle.kts` and the version catalog, all Gradle scripts that read config, demo module paths, and current workflow files. Preserve unrelated worktree changes.
2. Verify the config and API schema for the exact kmp-devtools version being used. This skill's concrete settings and workflow templates target **9.3.0**. Do not assume a copied `.config.yml` is current or that similarly named fields have the same meaning between versions. For another explicitly requested version, verify its schema and APIs and update all version references consistently.
3. Merge legacy values into the new config:
   - General Java and developer values go under `config`.
   - Library namespace, repository name, release year, and license go under `project`.
   - Legacy Maven group ID goes under `publish.group-id`; primary artifact ID, modules, and XCFrameworks go under `modules`.
   - Include all required top-level sections: `publish`, `config`, `apps`, `project`, `modules`, and `documentation`. In `documentation`, set the root `path` and `relative-paths.partials` and `relative-paths.screenshots`; `relative-paths.main-screenshot` is optional.
   - Use this standard demo app block and module layout. Migrate a combined JVM/Wasm demo module to this layout as described in step 7, unless the user explicitly asks to retain it. `module-paths` must name the actual projects used by `AppModuleConfig.read(project)` and by Android namespace configuration:
     ```yaml
     apps:
       - name: Toolbox Demo
         namespace: "<project-specific-demo-namespace>"
         version-name: 1.0.0
         module-paths:
           - demo/app/android
           - demo/app/jvm
           - demo/app/wasm
           - demo/shared
         # optional - if set, the app will be published to the homepage and linked in the readme
         publish:
           url-path: demo
           description: Explore the interactive demo
           module-path: demo/app/wasm
     ```
   - Keep the `publish` section in this shape across projects. Only `group-id` is project-specific and must match the project's Maven group ID; keep the GitHub artifact destination and release/test targets unchanged unless the user asks otherwise:
     ```yaml
     publish:
       group-id: "<project-specific-group-id>"
       github:
         user: MFlisar
         repo: maven-repository
         branch: main
         path: artifacts
       release-target: maven-central # [none, maven-central, github]
       test-target: github # [none, maven-central, github]
     ```
   - Retain release and test targets from the template unless the old workflows clearly establish different behavior.
4. Do not add legacy fields unsupported by the new schema. In particular, if old README screenshot options have no equivalent in the new config model, omit them and report that they cannot be migrated as config.
5. Migrate `settings.gradle.kts` using the **Settings migration** section below, including when the user has not already replaced it. Replace `.github/workflows` using the exact three templates in **Workflow replacement** below. Update Gradle APIs to those supported by the selected kmp-devtools version. For the 9.x API used in this migration, use `LibraryModuleConfig.read(project)`, `AppModuleConfig.read(project)`, `AndroidLibraryConfig.create(...)`, and `setupBuildKonfig(project = project, config = module.config, ...)` instead of their legacy variants. The shared Dokka setup is `BuildFileUtil.setupDokka(project)` with the build plugin applied.
   - In kmp-devtools 9.3, `AndroidLibraryConfig.create(...)` accepts a `LibraryModuleConfig.Library`, not an `AppModuleConfig`. If an app module also configures an Android library target, read both configs for that project: use `AppModuleConfig.read(project)` for app targets and `LibraryModuleConfig.read(project)` to create its Android library config.
   - Keep `library/dokka/build.gradle.kts` exactly in this form; do not add duplicate Dokka configuration:
     ```kotlin
     import com.michaelflisar.kmpdevtools.BuildFileUtil

     plugins {
         kotlin("jvm") apply false
         alias(libs.plugins.dokka)
         alias(mflisar.plugins.kmpdevtools.buildplugin)
     }

     BuildFileUtil.setupDokka(project)
     ```
   - Keep library module build files consistently sectioned and readable. Order applicable content as imports, a grouped `plugins` block, a `Setup` section for module/config/target declarations, a `Kotlin` section with distinct `Targets` and `Sources` subsections, source-set dependencies, and a final `Publish` section. Use the repository's existing section-divider style and group plugin aliases with short comments such as `kmp + app/library`, `org.jetbrains.kotlin`, `org.jetbrains.compose`, `docs, publishing, validation`, and `build tools`; include only plugins and sections relevant to that module.
   - Preserve existing explanatory and grouping comments, including comments inside plugin groups, source-set rules, and dependency groups, when reorganizing or migrating build files. Do not silently drop commented-out dependency examples or replace meaningful comments with generic placeholders. Keep dependency groups (for example Kotlin, Compose + AndroidX, and library dependencies) visibly separated where they already exist or are useful.
   - Every publishable library module must end with this exact section, with no condition or extra publish-specific comment:
     ```kotlin
     // -------------------
     // Publish
     // -------------------

     BuildFileUtil.setupMavenPublish(module)
     ```
6. For kmp-devtools 9.3, migrate legacy boolean `Targets(...)` declarations to enum entries: include only enabled targets, mapping `android` to `Target.ANDROID`, `iOS` to `Target.IOS`, `windows` to `Target.JVM`, `macOS` to `Target.MACOS`, and `wasm` to `Target.WASM`. Add `import com.michaelflisar.kmpdevtools.core.Target`; do not invent `Target.WINDOWS`. Replace related `Platform` constants in source-set rules with their `Target` equivalents, then remove the `Platform` import when no longer used. Replace boolean accessors such as `buildTargets.macOS` with `buildTargets.isEnabled(Target.MACOS)`.
7. Split a combined JVM/Wasm demo module into `demo/app/jvm` and `demo/app/wasm`, unless the user explicitly asks to retain it. Follow the user's Gradle templates and preserve the repository's grouped section comments:
   - Move `src/jvmMain` and its resources unchanged into the JVM module and `src/wasmJsMain` and its resources (including `index.html`) unchanged into the Wasm module. Preserve any common sources in `demo/shared`. Move desktop `.ico` files and local desktop preference files into the JVM module.
   - Both modules apply Kotlin Multiplatform, Kotlin Compose, JetBrains Compose, and the kmp-devtools build plugin. Read `AppModuleConfig.read(project)` and keep `-Xexpect-actual-classes` when present. Use `implementation(project(":demo:shared"))` in `commonMain`; do not copy stale dependencies from another library.
   - JVM uses `Targets(Target.JVM)`, `setupTargetsApp(module)`, and `compose.desktop.currentOs` in `jvmMain`, preserving the existing Material exclusion. Keep the original main class and `ico = "icon.ico"` in `DesktopAppConfig`; call `BuildFileUtil.setupWindowsApp(...)` inside `compose.desktop.application`.
   - Apply the kmpicon plugin in JVM and configure `kmpIcon.setup.sourceModule = "demo/app/android"`, `sourceFile = "src/main/ic_launcher-playstore.png"`, and `generateIco.file = "icon.ico"` when using the Android launcher icon. Verify that the source image exists. If there is no existing `.ico`, run `:demo:app:jvm:copyIconTask` and verify the generated file.
   - Apply the Launch4j plugin only to JVM and call `BuildFileUtil.registerLaunch4JTask(appModuleConfig = module, desktopAppConfig = desktopConfig)` to register thin/fat launchers. Add its version-catalog entry if missing and declare `alias(libs.plugins.launch4j) apply false` in the root script; kmp-devtools already brings Launch4j classes onto the classpath, and resolving its version only in a child module can fail with an unknown-classpath-version error.
   - Wasm uses `Targets(Target.WASM)` and `setupTargetsApp(module, wasmAppConfig = wasmConfig)`. Preserve `WasmAppConfig(moduleName = "demo", outputFileName = "demo.js")` and the matching HTML script name. Do not retain Android library/parcelize plugins, Android config, desugaring, desktop config, icon generation, or Launch4j in this module.
   - Android must depend directly on `project(":demo:shared")` instead of the removed combined module. Update IDE run-configuration project paths to JVM/Wasm, `.config.yml` app paths to android/jvm/wasm/shared, and `apps.publish.module-path` to `demo/app/wasm`. Search for all remaining references to the old module.
8. As final demo cleanup, delete the entire old `demo/app/compose/` directory after its sources, resources, icon, IDE run configuration, and build configuration have been migrated to the new modules. This explicit removal includes generated files inside that old module directory; do not remove build directories belonging to other demo modules unless requested. Delete ProGuard/consumer rules only when they contain no active rules and are no longer needed.
9. Update the Gradle wrapper to **9.8.2** as part of the migration, unless the user explicitly requests another version. Set `gradle/wrapper/gradle-wrapper.properties` to exactly the standard four base/path settings followed by `distributionUrl=https\://services.gradle.org/distributions/gradle-9.8.2-bin.zip`, without extra generated properties. For an explicitly requested wrapper version, use its matching distribution URL and retain the same exact five-line shape.
10. As final config cleanup, confirm no scripts still read the old files and no references to them remain, then delete the obsolete files and the now-empty `configs/` directory itself.
11. Verify config loading, module inclusion, stale API references, moved files, and wrapper version with the narrowest applicable checks. Compare the entire `settings.gradle.kts` with the exact settings template below, including formatting and comments. Run `projects` to verify settings and the module graph. Check that the catalog, settings plugin, and all workflow refs use the same kmp-devtools version, and that `.github/workflows` contains exactly the three specified files with the template contents. For a demo split, compile both entry points (`:demo:app:jvm:compileKotlinJvm` and `:demo:app:wasm:compileKotlinWasmJs`), check Android's new direct shared dependency, and verify the icon and registered launcher tasks. Report any remaining failures that come from separate, pre-existing migration work rather than silently broadening scope.

## Settings migration

Replace the consuming project's **entire** `settings.gradle.kts` with the complete template below. This is the standard settings file for these libraries, not a partial example to merge into the old file. The helper functions derive the library name and modules from `.config.yml`, so no project-specific substitution is needed.

- Copy all imports, repositories, catalog declarations, helper calls, comments, blank lines, indentation, and spacing exactly. Do not retain additional old settings blocks or repository/catalog declarations, reformat the template, or hardcode a library name.
- Set the literal settings-plugin version to `9.3.0` and the `[versions]` entry `kmpdevtools` in `gradle/mflisar.versions.toml` to `9.3.0`. Keep the build-plugin alias referencing that catalog entry. Version catalogs cannot supply the version in the settings `plugins` block.
- If another kmp-devtools version is explicitly requested, change only the literal plugin version after verifying compatibility and align the catalog and workflow refs. Otherwise use this file unchanged.
- If settings have already been replaced by the user, compare the whole file against this template and leave it untouched when it already matches.

```kotlin
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
    id("io.github.mflisar.kmpdevtools.plugins-settings-gradle") version "9.3.0"
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
```

Create `.config.yml` before invoking Gradle: both settings and build plugins need it. Align the catalog and workflow versions before running Gradle, because the 9.3.0 settings plugin synchronizes mismatching version references and aborts that invocation.

## Workflow replacement

This migration intentionally replaces the **entire workflow set**, not only files named `kmp-devtools-*`. Inspect `.github/workflows`, delete every existing workflow file there (including differently named `.yml` and `.yaml` files), and create exactly the three files below. Do not keep old release/tag/push workflows, merge their jobs into these templates, or introduce extra workflows. Preserve `.github/skills`, other `.github` content, and unrelated worktree changes outside the workflow replacement scope.

These are complete, portable consumer templates; they must travel with this skill and must not depend on access to ComposePreferences or on downloading templates from an unverified upstream path. For 9.3.0, copy their contents exactly, including filenames, triggers, run names, permissions, inputs, job names, `.config.yml` path, version refs, and secret forwarding. If another version is explicitly requested, only replace all three `@9.3.0` refs consistently after verifying compatibility.

### `.github/workflows/delegating-publish.yml`

```yaml
name: Publish
run-name: "Publish ${{ inputs.version }} (${{ contains(inputs.version, '-') && 'test / pre-release' || 'release' }}, target: ${{ inputs.target }})"

on:
  workflow_dispatch:
    inputs:
      version:
        description: "Version: X.Y.Z = release (release-target), X.Y.Z-suffix = pre-release (test-target)"
        required: true
        type: string
      target:
        description: "Target: auto = from config (release-target / test-target)"
        required: true
        type: choice
        options: [auto, maven-central, github]
        default: auto

permissions:
  contents: write

jobs:
  publish:
    uses: MFlisar/kmp-devtools/.github/workflows/reuseable-publish.yml@9.3.0
    with:
      version: ${{ inputs.version }}
      target: ${{ inputs.target }}
      config-file: .config.yml
    secrets: inherit
```

### `.github/workflows/delegating-update-github-pages.yml`

```yaml
name: Update GitHub Pages
run-name: "[${{ github.event_name }}] Update GitHub Pages"

on:
  push:
    branches: [main, master]

permissions:
  contents: read
  pages: write
  id-token: write

jobs:
  update-github-pages:
    uses: MFlisar/kmp-devtools/.github/workflows/reuseable-update-github-pages.yml@9.3.0
    with:
      config-file: .config.yml
```

### `.github/workflows/delegating-update-markdown-docs.yml`

```yaml
name: Update Markdown Docs
run-name: "[${{ github.event_name }}] Update Markdown Docs"

on:
  push:
    branches: [main, master]

permissions:
  contents: write

jobs:
  update-markdown-docs:
    uses: MFlisar/kmp-devtools/.github/workflows/reuseable-update-markdown-docs.yml@9.3.0
    with:
      config-file: .config.yml
```

Publishing is now started manually through `workflow_dispatch`, not through a tag or published-release event. Release and pre-release destinations come from `.config.yml`; the `target` input can override the destination. Preserve `secrets: inherit` for publishing only; do not copy secret values into the skill, config, or workflows. GitHub Pages must use GitHub Actions as its deployment source in repository settings; flag that external prerequisite if it is not configured rather than changing the workflow templates.
