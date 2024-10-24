## Custom Dialog

Of course you can use the complete framework for your own dialogs as well.

```kotlin
fun Dialog(
    state: DialogState,
    title: String? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    specialOptions: SpecialOptions = DialogDefaults.specialOptions(),
    onEvent: (event: DialogEvent) -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
)
```

Here's an example of a custom dialog:

```kotlin
Dialog(state, title, icon, style, buttons, options, onEvent = onEvent) {
    Text("Text in custom dialog")
    // ...
}
```