---
icon: material/puzzle
---

This shows a simple loading dialog with a progress indicator.

Check out the composable and it's documentation in the code snipplet below.

Generally following can be adjusted:

* circular or linear progress

#### Example

<!-- snippet: Demos::demo-progress -->
```kt
if (state.visible) {
    DialogProgress(
        state = state,
        content = {
            Text("Working...")
        },
        progressStyle = DialogProgress.Style.Indeterminate(linear = false),
        icon = icon,
        title = { Text("Progress Dialog") },
        buttons = DialogDefaults.buttons(
            positive = DialogButton("Stop")
        ),
        style = style,
        onEvent = {
            if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                showInfo("Progress Dialog closed by button")
            } else {
                showInfo("Event $it")
            }
        }
    )
}
```
<!-- endSnippet -->

#### Composable

<!-- snippet: DialogProgress::constructor -->
```kt
/**
 * Shows a dialog with an optional label and a progress indicator
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param content the content of the progress
 * @param progressStyle the style of the progress indicator ([DialogProgress.Style])
 */
@Composable
fun DialogProgress(
    state: DialogState,
    // Custom - Required
    // ...
    // Custom - Optional
    content: (@Composable ColumnScope.() -> Unit)? = null,
    progressStyle: DialogProgress.Style = DialogProgress.Style.Indeterminate(),
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

| |                                                           |
|-|-----------------------------------------------------------|
| ![Screenshot](../screenshots/progress/demo_progress1.jpg) | ![Screenshot](../screenshots/progress/demo_progress2.jpg) |
