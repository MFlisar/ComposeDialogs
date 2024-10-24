## Custom Dialog

Of course you can use the complete framework for your own dialogs as well.

```kotlin
fun Dialog(
    state: DialogState,
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: DialogStyle = DialogDefaults.styleDialog(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    specialOptions: SpecialOptions = SpecialOptions(),
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