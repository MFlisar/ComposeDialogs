### About

[![Release](https://jitpack.io/v/MFlisar/ComposeDialogs.svg)](https://jitpack.io/#MFlisar/ComposeDialogs)
![License](https://img.shields.io/github/license/MFlisar/ComposeDialogs)

This library offers you an easily extendible compose framework for modal dialogs and allows to show them as dialogs or as bottom sheet.

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
        title = "Info Dialog",
        info = "Some information...",
        // Optional - all options can be set up with custom attributes, following are just the default examples
        style = DialogDefaults.styleBottomSheet(), // or DialogDefaults.styleDialog() => both have quite some settings and all default dialog settings...
        icon = DialogDefaults.icon(rememberVectorPainter(Icons.Default.Home)),
        buttons = DialogDefaults.dialogButtons(
            positive = dialogButton(stringResource(android.R.string.ok)),
            negative = dialogButton("")
        ),
        options = DialogDefaults.options(
            dismissOnButtonClick = true,
            wrapContentInScrollableContainer = false
        ),
        onEvent: (event: DialogEvent) -> Unit = { event: DialogEvent ->
            // optional event handler for all dialog events
        }
    )
}

// show the dialog inside a button press event or similar
Button(onClick = { state.show() }) {
    Text("Show Dialog")
}
```

### Settings and advanced usage

Check out the dialog state and the dialogs to find out what settings you can use and especially the demo app for a working example.

**Dialog State (simple with a boolean flag or complex with a data object)**

In case of the simple state `true` means that the dialog is visible and `false` that it's not. In case of the complex state holding an object means the dialog is visible and `null` means it's not visible.

In case of the complex state simply use `state.show(data)` to show the dialog and then inside your dialog call `val data = state.requireData()` to get the data from the state.

https://github.com/MFlisar/ComposeDialogs/blob/d743c8923c7478967d11825b07be5a079f21b4eb/library/core/src/main/java/com/michaelflisar/composedialogs/core/Dialog.kt#L32-L38

https://github.com/MFlisar/ComposeDialogs/blob/d743c8923c7478967d11825b07be5a079f21b4eb/library/core/src/main/java/com/michaelflisar/composedialogs/core/Dialog.kt#L58-L66
  
### Existing dialogs

**Info Dialog**

...

**Input Dialog**

...