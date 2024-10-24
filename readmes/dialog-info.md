## Info Dialog

| Preview | | Module |
| :- | :- | :- |
| ![Preview](screenshots/demo_info1.jpg "Preview") | ![Preview](screenshots/demo_info2.jpg "Preview") | `info` |

This shows a simple dialog with some informational text.

```kotlin
fun DialogInfo(
    state: DialogState,
    // Custom - Required
    info: String,
    // Custom - Optional
    infoLabel: String = "",
    // Base Dialog - Optional
    title: String? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    specialOptions: SpecialOptions = DialogDefaults.defaultInfoDialogSpecialOptions(),
    onEvent: (event: DialogEvent) -> Unit = {}
)
```