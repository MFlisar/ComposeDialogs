## Custom Dialog

Of course you can use the complete framework for your own dialogs as well.

https://github.com/MFlisar/ComposeDialogs/blob/632089378053cdd060a5b90cf505a6e43e01f2b3/library/core/src/commonMain/kotlin/com/michaelflisar/composedialogs/core/Dialog.kt#L25-L35

Here's an example of a custom dialog:

```kotlin
Dialog(state, title, icon, style, buttons, options, onEvent = onEvent) {
    Text("Text in custom dialog")
    // ...
}
```
