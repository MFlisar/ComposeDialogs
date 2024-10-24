## Input Dialog

| Preview | | Module |
| :- | :- | :- |
| ![Preview](../screenshots/dark/demo_input1.jpg "Preview") | ![Preview](../screenshots/dark/demo_input2.jpg "Preview") | `input` |

This shows a dialog with a `InputField`. All its parameters are exposed via the compose function as you can see below, which allows you to simply adjust the `InputField`s behaviour. Additinally you can attach a validator which ensures, that the dialog will only return a valid input and can't be closed otherwise.

```kotlin
fun DialogInput(
    // Base Dialog - State
    state: DialogState,
    // Custom - Required
    input: MutableState<String>,
    inputLabel: String = "",
    // Custom - Optional
    inputPlaceholder: String = "",
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    enabled: Boolean = true,
    clearable: Boolean = true,
    prefix: String = "",
    suffix: String = "",
    textStyle: TextStyle = LocalTextStyle.current,
    validator: DialogInputValidator = rememberDialogInputValidator(),
    requestFocus: Boolean = false,
    selectionState: DialogInput.SelectionState = DialogInput.SelectionState.Default,
    onTextStateChanged: (valid: Boolean, text: String) -> Unit = { _, _ -> },
    // Base Dialog - Optional
    title: String? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    specialOptions: SpecialOptions = DialogDefaults.defaultInputDialogSpecialOptions(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
)
```

```kotlin
fun <T : Number> DialogInputNumber(
    // Base Dialog - State
    state: DialogState,
    // Custom - Required
    value: MutableState<T>,
    valueLabel: String = "",
    // Custom - Optional
    inputPlaceholder: String = "",
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    enabled: Boolean = true,
    clearable: Boolean = true,
    prefix: String = "",
    suffix: String = "",
    validator: DialogInputValidator = DialogInputNumber.rememberDefaultValidator(value.value),
    textStyle: TextStyle = LocalTextStyle.current,
    requestFocus: Boolean = false,
    selectionState: DialogInput.SelectionState = DialogInput.SelectionState.Default,
    onValueStateChanged: (valid: Boolean, value: T?) -> Unit = { _, _ -> },
    // Base Dialog - Optional
    title: String? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    specialOptions: SpecialOptions = DialogDefaults.defaultInputDialogSpecialOptions(),
    onEvent: (event: DialogEvent) -> Unit = {}
)
```