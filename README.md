### About

[![Release](https://jitpack.io/v/MFlisar/ComposeDialogs.svg)](https://jitpack.io/#MFlisar/ComposeDialogs)
![License](https://img.shields.io/github/license/MFlisar/ComposeDialogs)

This library offers you an easily extendible compose framework for modal dialogs and allows to show them as a **dialog** or a **bottom sheet**.

Made for **Compose M3**.

### Dependencies

| Dependency |        Version |
|:-------------------------------------------------------------------- |---------------:|
| [Compose BOM](https://developer.android.com/jetpack/compose/bom/bom) |   `2023.10.00` |
| Material3 | `1.1.2` |

Compose Mappings for BOM file: [Mapping](https://developer.android.com/jetpack/compose/bom/bom-mapping)

### Gradle (via [JitPack.io](https://jitpack.io/))

1. add jitpack to your project's `build.gradle`:
```groovy
repositories {
    maven { url "https://jitpack.io" }
}
```

2. add the compile statement to your module's `build.gradle`:
```groovy
dependencies {

  // core module
  implementation "com.github.MFlisar:ComposeDialogs:core:<LATEST-VERSION>"
  
  // ui modules
  implementation "com.github.MFlisar:ComposeDialogs:dialog-info:<LATEST-VERSION>"
  implementation "com.github.MFlisar:ComposeDialogs:dialog-input:<LATEST-VERSION>"
  implementation "com.github.MFlisar:ComposeDialogs:dialog-list:<LATEST-VERSION>"
  implementation "com.github.MFlisar:ComposeDialogs:dialog-progress:<LATEST-VERSION>"
  implementation "com.github.MFlisar:ComposeDialogs:dialog-time:<LATEST-VERSION>"
  implementation "com.github.MFlisar:ComposeDialogs:dialog-date:<LATEST-VERSION>"
  implementation "com.github.MFlisar:ComposeDialogs:dialog-color:<LATEST-VERSION>"
}
```

### Example

It works as simple as following:

```kotlin

// create and remember a state
val state = rememberDialogState()

// show a dialog if necessary
if (state.showing)
{
    DialogInfo(
        state = state,
        // Custom - Required
        info: String,
        // Custom - Optional
        infoTitle: String = "",
        // Base Dialog -  Optional - all options can be set up with custom attributes, following are just the default examples
        title: String = "",
        titleStyle: DialogTitleStyle = DialogDefaults.titleStyle(), // or DialogDefaults.titleStyleSmall() => both have a few settings...
        icon: DialogIcon? = null, // use DialogIcon.Painter(...) or DialogIcon.Vector(...) to add an icon
        style: DialogStyle = DialogDefaults.styleDialog(), // DialogDefaults.styleBottomSheet() => both have a few settings...
        buttons: DialogButtons = DialogDefaults.buttons(),
        options: Options = Options(),
        onEvent: (event: DialogEvent) -> Unit = {
            // optional event handler for all dialog events
        }
    )
}

// show the dialog inside a button press event or similar
Button(onClick = { state.show() }) {
    Text("Show Dialog")
}
```

### Screenshots

...

### Settings and advanced usage

Check out the dialog state and the dialogs to find out what settings you can use and especially the demo app for a working example.

**Dialog State (simple with a boolean flag or complex with a data object)**

In case of the simple state `true` means that the dialog is visible and `false` that it's not. In case of the complex state holding an object means the dialog is visible and `null` means it's not visible.

https://github.com/MFlisar/ComposeDialogs/blob/7dcafb6f5731bcdd9f744f538988cd3d567bafc6/library/core/src/main/java/com/michaelflisar/composedialogs/core/DialogState.kt#L77-L83

In case of the complex state simply use `state.show(data)` to show the dialog and then inside your dialog call `val data = state.requireData()` to get the data from the state. I provide an overload for all supported data types (types that are supported by `Bundle`). For all other cases you must provide a custom saver.

https://github.com/MFlisar/ComposeDialogs/blob/7dcafb6f5731bcdd9f744f538988cd3d567bafc6/library/core/src/main/java/com/michaelflisar/composedialogs/core/DialogState.kt#L131-L138

https://github.com/MFlisar/ComposeDialogs/blob/7dcafb6f5731bcdd9f744f538988cd3d567bafc6/library/core/src/main/java/com/michaelflisar/composedialogs/core/DialogState.kt#L39-L47
  
### Existing dialogs

**Info Dialog**

...

**Input Dialog**

...

**Date Dialog**

...

**Time Dialog**

...

**Color Dialog**

...

**List Dialog**

...

**Progress Dialog**

...

**Custom Dialog**

...