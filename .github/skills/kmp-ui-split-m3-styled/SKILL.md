---
name: kmp-ui-split-m3-styled
description: Split a Kotlin Multiplatform library's UI implementation into shared Material 3 and Styled backends with correct provider, source-set, and dependency wiring.
---

# KMP UI split: M3 and Styled

Use this skill to extract a Kotlin Multiplatform library from a toolkit-specific UI implementation or to set up two interchangeable UI backends: Material 3 and Styled.

## Architecture

- Keep one toolkit-neutral UI-provider interface in the dedicated `ui/core-interface` module's `commonMain`. This module may depend on Compose runtime/UI primitives, but must not depend on Material 3 or Styled. All feature UI modules use this interface for toolkit-specific rendering; do not create a Material 3/Styled module pair for every feature.
- Keep provider-interface methods abstract and do not use interface default implementations or default parameter values; Kotlin/Wasm does not support these interface defaults reliably. Put optional/default behavior in top-level or extension functions instead.
- Keep shared models, state, algorithms, and UI code that only uses Compose primitives in `core/commonMain`.
- Provide complete Material 3 and Styled implementations under `implementation/material3` and `implementation/styled`. Separate provider-only modules may be added if consumers need to select providers independently.
- Treat Styled as a complete design system, not a reduced Material 3 fallback. Use its `StyledTheme` tokens (such as `typography`, `colorScheme`, `padding`, and sizing) and Styled components; Styled often provides equivalent or broader theming capabilities than `MaterialTheme`. Do not implement the Styled backend by reusing Material 3 theme values.
- In the Styled implementation, do not hard-code component dimensions or touch-target sizes. Prefer Styled components and platform-aware `StyledTheme` sizing, padding, and spacing tokens so each platform can choose appropriate dimensions.
- Create one user-facing implementation module per toolkit, such as `implementation/material3` and `implementation/styled`. Each automatically selects its own provider at compile time. Consumers normally depend on exactly one of these modules and do not call a registration API.
- The `ui/core-interface` module must compile on its own. Add its `coreOnlyMain` source directory to `commonMain` with a fallback binding that reports a clear error if no provider is available. Implementation modules must not include this fallback.
- For an auto-registering implementation, compile both the shared core sources and its selected provider sources into the implementation module. Do not also put the corresponding core or provider KLIBs on that module's Wasm dependency graph: duplicate fully qualified declarations can fail during executable IR linking with `IrPropertySymbolImpl is already bound`. A Gradle exclusion alone may not remove a dependency recorded in a provider KLIB's transitive dependency closure.
- Keep a manual registration API such as `ComposeDialogsUI.set(provider)` for custom providers or applications that need to choose a provider at runtime. Standard Material 3/Styled implementation modules must still select their default provider at compile time, so consumers of those modules do not need to register one. Provider objects must be public if consumers call this API.
- If provider-only modules are published and have `api(core-interface)` dependencies, make sure the interface artifact is also published and resolvable. A documentation-only `internal` setting does not necessarily make a module private or unpublished; check the target repository's publishing conventions.

## Gradle source-set and dependency layout

Keep source wiring and dependencies in a single `commonMain {}` block. Declare source directories before `dependencies {}`. Use actual project paths and Maven coordinates from the target repository.

When a module has no custom source sets, retain the section with the `// --` placeholder:

```kotlin
// ---------------------
// custom source sets
// ---------------------

// --

// ---------------------
// dependencies
// ---------------------
```

### Core UI interface module

The interface module compiles the fallback binding with its contract:

```kotlin
commonMain {
    kotlin.srcDir("src/coreOnlyMain/kotlin")

    dependencies {
        api(libs.compose.runtime)
        api(libs.compose.ui)
    }
}
```

The regular library `core` module is separate and may retain toolkit dependencies until its own UI is providerized. Feature content modules that only need the provider contract should depend on `ui/core-interface`, not on `core`.

### Provider implementation modules

Each provider module depends on the UI interface and contains its toolkit implementation. Re-export dependencies required by the provider's public API. Keep toolkit-specific dependencies as implementation dependencies unless their types appear in the provider's public API.

```kotlin
commonMain.dependencies {
    api(project(":<library>:ui:core-interface"))

    // Re-export dependencies required by the public provider API.
    api(libs.compose.foundation)
    api(libs.compose.resources)
    api(libs.coroutines.core)

    implementation(libs.compose.material3)
}
```

The Styled provider follows the same structure, replacing the Material 3 dependency with the Styled toolkit dependency.

### Automatically registered implementation modules

Compile the library core `commonMain` sources and the `ui/core-interface` `commonMain` sources directly into each user-facing implementation module. This lets that module provide its own binding in the same compilation as the interface. Do not include the interface module's `coreOnlyMain` fallback. Do not add a project dependency on either `core` or the interface module; the provider KLIB can bring a second copy of core/interface declarations into the Wasm linker even when an exclusion is declared.

Declare the core API dependencies and selected toolkit dependencies directly, matching the visibility and dependency scope of the core and provider modules:

```kotlin
commonMain {
    kotlin.srcDir(project(":<library>:core").file("src/commonMain/kotlin"))
    kotlin.srcDir(project(":<library>:ui:core-interface").file("src/commonMain/kotlin"))

    dependencies {
        api(libs.compose.resources)
        api(libs.compose.foundation)
        api(libs.coroutines.core)
        implementation(libs.compose.material3)
        implementation(libs.compose.material.icons.extended)
    }
}
```

Use the corresponding Styled provider sources and toolkit dependencies in `implementation/styled`. Custom provider implementations follow the same compile-time binding pattern and can also be installed explicitly with `ComposeDialogsUI.set(provider)`.

The implementation module defines its own binding in the same package and with the same name as the core fallback, bound to the provider object compiled from the selected provider sources. This is compile-time default selection, not runtime discovery.

### Libraries with multiple feature modules

- Have every feature module use the dedicated UI interface; keep Material 3 and Styled APIs out of shared feature code.
- Implement toolkit-specific UI operations once per provider module, shared by all features.
- Use one default-binding aggregator per toolkit. It compiles the shared core and selected provider sources once and defines the toolkit's default binding once.
- Choose one core strategy for the final Wasm dependency graph: either link one shared core KLIB, or compile core sources into the aggregator without linking feature/provider KLIBs that transitively carry core. Do not rely on a Gradle exclusion alone to remove declarations embedded in a KLIB dependency closure.

### Consumer and demo dependencies

An end-user demo for one toolkit should depend on its user-facing implementation module only, demonstrating compile-time provider selection. A demo intended to show manual provider selection instead depends on `ui/core-interface` and provider-only modules, then calls `ComposeDialogsUI.set(provider)`. Do not combine the two auto-registering implementation modules in one consumer.

## Extracting an existing toolkit-specific library

1. Inspect the module tree, source sets, public APIs, dependencies, tests, and all toolkit-specific imports and calls.
2. Keep models, state, algorithms, and UI shared by feature content modules in core where appropriate. Move toolkit-specific theme access, widgets, icons, and visual defaults used by `library/ui/*` content modules into provider implementations. The core dialog wrapper may retain Material 3 dependencies and APIs when required; do not make core toolkit-neutral unless that is explicitly in scope.
3. Add toolkit-neutral methods to the dedicated UI provider interface for every toolkit-specific operation still required by shared code. Keep methods abstract and omit default method bodies and parameter values; use top-level or extension functions for defaults to remain compatible with Kotlin/Wasm.
4. Create complete Material 3 and Styled provider implementations. Preserve public packages and signatures where possible; expose provider objects if manual registration is a supported use case.
5. Add the core-only fallback binding to `ui/core-interface` and a useful error when a UI operation is attempted without an implementation.
6. Configure each provider-only module with `api(core-interface)` and its required API/runtime dependencies.
7. Configure each auto-registering implementation module to compile core `commonMain` and interface `commonMain` sources together, bind its provider in that compilation, and declare the core API and toolkit dependencies directly. Do not depend on the core or interface project KLIBs from this module.
8. For multiple feature modules, centralize provider implementations and default binding; ensure the aggregator and feature-module KLIBs do not contribute duplicate core or provider declarations.
9. Remove Material 3 dependencies and imports from the shared `ui/core-interface` and feature content modules under `library/ui/*`. Core may remain Material 3-dependent for its dialog wrappers and public APIs; do not broaden the migration to core unless requested. Search the full UI module sources and Gradle configuration for remaining references.
10. Build core, both provider modules, both user-facing implementation modules, and representative consumers for configured common and platform targets. For Wasm, also build the executable/link task (for example, `compileDevelopmentExecutableKotlinWasmJs`) or run the browser development task; compiling only `compileKotlinWasmJs` does not exercise executable IR linking. Verify the final Wasm KLIB dependency graph does not reintroduce duplicate core/provider declarations.

## Implementation workflow

1. Inspect the target repository's module paths, source-set conventions, publication setup, and whether the task is a new setup or an extraction.
2. Identify the shared provider interface and every feature that needs it. Keep one provider abstraction shared across feature modules.
3. Configure the `ui/core-interface` module and both provider implementations, including the interface-only fallback and public dependencies.
4. Configure the Material 3 and Styled user-facing implementation modules to compile core and interface sources together, with direct API/toolkit dependencies and no duplicate core/interface KLIB dependency.
5. Update demos to show either the normal single-implementation dependency or explicit manual registration, according to their purpose.
6. Compile the core, provider modules, implementation modules, and consumers. Inspect generated dependency metadata for duplicate core dependencies and resolve all target-specific issues.
