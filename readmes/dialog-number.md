## Number Dialog

| Preview | | Module |
| :- | :- | :- |
| ![Preview](screenshots/demo_number1.jpg "Preview") | ![Preview](screenshots/demo_number3.jpg "Preview") | `number` |

This shows a number **picker** dialog. You can always use the *Input Dialog*  for numbers as well and change its options to accept numbers only and even attach an validator. But this one is meant for picking numbers with the help of one or two increase and decrease buttons.

```kotlin
fun <T : Number> DialogNumberPicker(
    // Base Dialog - State
    state: DialogState,
    // Custom - Required
    value: MutableState<T>,
    setup: NumberPickerSetup<T>,
    iconDown: @Composable () -> Unit = {
        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null)
    },
    iconUp: @Composable () -> Unit = {
        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
    },
    iconDown2: @Composable () -> Unit = {
        Icon(imageVector = Icons.Default.KeyboardDoubleArrowLeft, contentDescription = null)
    },
    iconUp2: @Composable () -> Unit = {
        Icon(imageVector = Icons.Default.KeyboardDoubleArrowRight, contentDescription = null)
    },
    formatter: (value: T) -> String = { it.toString() },
    // Custom - Optional
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    onValueStateChanged: (value: T) -> Unit = { },
    // Base Dialog - Optional
    title: String? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    specialOptions: SpecialOptions = DialogDefaults.defaultNumberDialogSpecialOptions(),
    onEvent: (event: DialogEvent) -> Unit = {}
)
```