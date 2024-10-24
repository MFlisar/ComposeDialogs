## Date Dialog

| Preview | | Module |
| :- | :- | :- |
| ![Preview](screenshots/demo_calendar1.jpg "Preview") | ![Preview](screenshots/demo_calendar2.jpg "Preview") | `date` |

This shows a date selector dialog. First day of week, labels, and style can be adjusted to your needs.

```kotlin
fun DialogDate(
    state: DialogState,
    // Custom - Required
    date: MutableState<LocalDate>,
    // Custom - Optional
    dateRange: DialogDate.Range = DialogDateDefaults.dateRange(),
    setup: DialogDate.Setup = DialogDateDefaults.setup(),
    // Base Dialog - Optional
    title: String? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    specialOptions: SpecialOptions = DialogDefaults.defaultDateDialogSpecialOptions(),
    onEvent: (event: DialogEvent) -> Unit = {}
)
```