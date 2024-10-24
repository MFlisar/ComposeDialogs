## Progress Dialog

| Preview | | Module |
| :- | :- | :- |
| ![Preview](screenshots/demo_progress1.jpg "Preview") | ![Preview](screenshots/demo_progress2.jpg "Preview") | `progress` |

This shows a simple loading dialog with a progress indicator.

```kotlin
fun DialogProgress(
    state: DialogState,
    // Custom - Required
    // ...
    // Custom - Optional
    content: (@Composable ColumnScope.() -> Unit)? = null,
    progressStyle: DialogProgress.Style = DialogProgress.Style.Indeterminate(),
    // Base Dialog - Optional
    title: String? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    specialOptions: SpecialOptions = DialogDefaults.defaultProgressDialogSpecialOptions(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
)
```