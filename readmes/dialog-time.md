## Time Dialog

| Preview | Module |
| :- | :- |
| ![Preview](screenshots/demo_time1.jpg "Preview") | `time` |

This shows a time selector dialog. 24h mode is optional.

```kotlin
fun DialogTime(
    state: DialogState,
    // Custom - Required
    time: MutableState<LocalTime>,
    // Custom - Optional
    setup: DialogTime.Setup = DialogTimeDefaults.setup(),
    // Base Dialog - Optional
    title: String? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    specialOptions: SpecialOptions = DialogDefaults.defaultTimeDialogSpecialOptions(),
    onEvent: (event: DialogEvent) -> Unit = {}
)
```