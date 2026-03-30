---
icon: material/puzzle
---

This shows a dialog with a `InputField`. All its parameters are exposed via the compose function as you can see below, which allows you to simply adjust the `InputFields` behaviour. Additinally you can attach a validator which ensures, that the dialog will only return a valid input and can't be closed otherwise.

Check out the composable and it's documentation in the code snipplet below.

Generally following can be adjusted:

* texts
* number of lines
* `TextField` options like number of lines, keyboard options, selection state...
* an optional `DialogInputValidator` can be provided

#### Example

##### DialogInput

<!-- snippet: Demos::demo-input -->
```kt
if (state.visible) {

    // special state for input dialog
    val input = rememberDialogInput(text)

    // input dialog
    DialogInput(
        state = state,
        title = { Text("Input Dialog") },
        value = input,
        label = "Text",
        icon = icon,
        style = style,
        onEvent = {
            if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                // we should probably handle the input value in this case
                showInfo("Submitted Input: ${input.value}")
            } else {
                showInfo("Event $it")
            }
        },
        validator = rememberDialogInputValidator(
            validate = {
                if (it.isNotEmpty())
                    DialogInputValidator.Result.Valid
                else
                    DialogInputValidator.Result.Error("Empty input is not allowed!")
            }
        ),
        onTextStateChanged = { valid, _ ->
            state.enableButton(DialogButtonType.Positive, valid)
        }
    )
}
```
<!-- endSnippet -->

##### DialogInputNumber

<!-- snippet: Demos::demo-input-number -->
```kt
// would work with Int, Long, Double and Float (all options of Number!)
val number = 123
val state = rememberDialogState()
if (state.visible) {

    // special state for input dialog
    val value = rememberDialogInputNumber(number)

    // number dialog
    DialogInputNumber(
        state = state,
        title = { Text("Input Integer Dialog") },
        value = value,
        label = "Integer",
        icon = icon,
        style = style,
        onEvent = {
            if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                // we should probably handle the input value in this case
                showInfo("Submitted Input: ${value.value}")
            } else {
                showInfo("Event $it")
            }
        }
    )
}
```
<!-- endSnippet -->
	
#### Composable

There are 2 main composables for this dialog, one for a string and for a numerical input.

##### DialogInput

<!-- snippet: DialogInput::constructor -->
```kt
/**
 * Shows a dialog with an input field
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param value the selected text
 * @param label the optional label of the input field
 * @param inputPlaceholder the placeholder the for the input field
 * @param singleLine if true, the input field will only allow a single line
 * @param maxLines the max lines for the input field
 * @param minLines the min lines for the input field
 * @param keyboardOptions the [KeyboardOptions] for the input field
 * @param enabled if true, the input field is enabled
 * @param clearable if true, the input field can be cleared by a trailing clear icon
 * @param prefix the prefix for the input field
 * @param suffix the prefix for the input field
 * @param textStyle the [TextStyle] for the input field
 * @param validator the [DialogInputValidator] for the input field - use [rememberDialogInputValidator]
 * @param requestFocus if true, the input field will request the focus when the dialog si shown (and open the keyboard)
 * @param selectionState if initial selection state ([DialogInput.SelectionState]) of the input field
 * @param onTextStateChanged an optional callback that will be called whenever the value of the input field changes
 */
@Composable
fun DialogInput(
    // Base Dialog - State
    state: DialogState,
    // Custom - Required
    value: MutableState<String>,
    label: String = "",
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
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: DialogOptions = DialogDefaults.options(),
    onEvent: (event: DialogEvent) -> Unit = {}
)
```
<!-- endSnippet -->

##### DialogInputNumber

<!-- snippet: DialogInputNumber::constructor -->
```kt
/**
 * Shows a dialog with an input field that only allows numeric characters and validates that the input holds a valid value for the desired data type
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param value the selected number
 * @param label the optional label of the input field

 * @param inputPlaceholder a placeholder if the input is empty
 * @param singleLine if true, the input field will only allow a single line
 * @param maxLines the max lines for the input field
 * @param minLines the min lines for the input field
 * @param enabled if true, the input field is enabled
 * @param clearable if true, the input field can be cleared by a trailing clear icon
 * @param prefix the prefix for the input field
 * @param suffix the prefix for the input field
 * @param textStyle the [TextStyle] for the input field
 * @param validator the [DialogInputValidator] for the input field - use [rememberDialogInputValidator]
 * @param requestFocus if true, the input field will request the focus when the dialog si shown (and open the keyboard)
 * @param selectionState if initial selection state ([DialogInput.SelectionState]) of the input field
 * @param onValueStateChanged an optional callback that will be called whenever the value of the input field changes
 */
@Composable
fun <T : Number> DialogInputNumber(
    // Base Dialog - State
    state: DialogState,
    // Custom - Required
    value: MutableState<T>,
    label: String = "",
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
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: DialogOptions = DialogDefaults.options(),
    onEvent: (event: DialogEvent) -> Unit = {}
)
```
<!-- endSnippet -->

#### Screenshots

| |                                                     |
|-|-----------------------------------------------------|
| ![Screenshot](../screenshots/input/demo_input1.jpg) | ![Screenshot](../screenshots/input/demo_input2.jpg) |
