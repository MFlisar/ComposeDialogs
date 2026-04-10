---
icon: material/puzzle
---

This shows a number **picker dialog**. You can always use the `InputDialog` for numbers as well and change its options to accept numbers only and even attach a validator. But this one is meant for picking numbers with the help of one or two increase and decrease buttons.

Check out the composable and it's documentation in the code snipplet below.

Generally following can be adjusted:

* min/max/step values
* custom formatter

#### Example

<!-- snippet: Demos::demo-number -->
```kt
// would work with Int, Long, Double and Float (all options of Number!)
val number = 5
val state = rememberDialogState()
if (state.visible) {

    // special state for input dialog
    val value = rememberDialogNumber(number)

    // number dialog
    DialogNumberPicker(
        state = state,
        title = { Text("Integer Picker Dialog") },
        value = value,
        icon = icon,
        style = style,
        onEvent = {
            if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                // we should probably handle the input value in this case
                showInfo("Submitted Input: ${value.value}")
            } else {
                showInfo("Event $it")
            }
        },
        setup = NumberPickerSetup(
            min = 0, max = 100, stepSize = 5
        )
    )
}
```
<!-- endSnippet -->

#### Composable

<!-- snippet: DialogNumberPicker::constructor -->
```kt
/**
 * Shows a dialog with a number picker
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param value the number state for this dialog
 * @param setup the [NumberPickerSetup]
 * @param iconDown the icon for the decrease button
 * @param iconUp the icon for the increase button
 * @param formatter the formatter for the text of this picker
 * @param textStyle the [TextStyle] for the text of this picker
 * @param onValueStateChanged a callback that will be called whenever the value of the number picker changes
 */
@Composable
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
    formatter: @Composable (value: T) -> String = { it.toString() },
    // Custom - Optional
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    onValueStateChanged: (value: T) -> Unit = { },
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

| | |                                                       |
|-|-|-------------------------------------------------------|
| ![Screenshot](../screenshots/number/demo_number1.jpg) | ![Screenshot](../screenshots/number/demo_number2.jpg) | ![Screenshot](../screenshots/number/demo_number3.jpg) |
