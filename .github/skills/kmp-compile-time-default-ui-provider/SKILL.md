---
name: kmp-compile-time-default-ui-provider
description: Configure a Kotlin Multiplatform core with an empty default UI provider and toolkit implementation modules that select their provider at compile time.
---

# KMP compile-time default UI provider

Use this skill when a Kotlin Multiplatform library must compile as a standalone core with no UI backend, while an end-user implementation dependency supplies a default backend automatically. The provider is selected by the source files included in each compilation, not by runtime discovery.

## Required behavior

- The core module must compile on its own. Its `commonMain` includes `src/coreOnlyMain/kotlin`, where a nullable, empty default provider is declared.
- Each user-facing implementation module must compile the shared core `src/commonMain/kotlin` sources together with its toolkit implementation. It must not include the core-only fallback source directory.
- The core fallback and implementation binding use the same package and `DialogDefaultUIProvider` object name. Separate source selection gives each compilation exactly one binding.
- Adding one implementation module as a consumer dependency selects its backend without requiring the consumer to call a registration API.
- Do not use a service loader, global registry, platform startup hook, or other runtime auto-discovery mechanism for this default selection. Kotlin Multiplatform has no single mechanism with consistent support across all targets.

## Required provider declarations

Keep the core-only binding in `library/core/src/coreOnlyMain/kotlin/com/michaelflisar/composedialogs/implementation/DialogUIProvider.kt`:

```kotlin
internal object DialogDefaultUIProvider {
    val impl: ComposeDialogsUI.Provider? = null
}
```

In the Material 3 implementation source, keep the default binding and concrete provider as separate objects, in this order and structure:

```kotlin
internal object DialogDefaultUIProvider {
    val impl: DialogUI.Provider = Material3DialogUIProvider
}

private object Material3DialogUIProvider : DialogUI.Provider {
    // Implement the provider contract here.
}
```

Use the corresponding private toolkit-specific provider object in other implementation modules, while retaining the `DialogDefaultUIProvider` binding shape. Do not make the implementation provider public unless it is independently part of the supported public API.

## Gradle source-set wiring

Add the core-only fallback directory to the core module's `commonMain`:

```kotlin
commonMain {
    kotlin.srcDir("src/coreOnlyMain/kotlin")
    dependencies {
        // Core dependencies
    }
}
```

In each user-facing implementation module, include the shared core sources directly in `commonMain`:

```kotlin
commonMain {
    kotlin.srcDir(project(":composedialogs:core").file("src/commonMain/kotlin"))
    dependencies {
        // Dependencies required by the shared core and selected implementation
    }
}
```

Do not add `src/coreOnlyMain/kotlin` to the implementation source set. Do not depend on both the core-only fallback and the toolkit binding in the same compilation: they declare the same fully qualified object and are alternatives, not layered providers.

Keep source-directory declarations before the `dependencies {}` block and follow the repository's existing Gradle formatting. Preserve its custom-source-set placeholder when there are no custom source sets:

```kotlin
// ---------------------
// custom source sets
// ---------------------

// --

// ---------------------
// dependencies
// ---------------------
```

## Implementation checklist

1. Inspect the provider contract, package names, source-set hierarchy, and implementation module dependency graph before editing.
2. Verify the core compilation includes the nullable fallback and does not depend on a toolkit-specific UI implementation.
3. Verify each user-facing implementation compilation includes shared core sources and exactly one non-null default binding to its toolkit provider.
4. Verify the implementation source set excludes `coreOnlyMain`; avoid duplicate definitions of `DialogDefaultUIProvider`.
5. Build the core module and the implementation module independently. Where a demo or sample exists, build it with only the intended implementation dependency to verify that no manual provider setup is needed.
