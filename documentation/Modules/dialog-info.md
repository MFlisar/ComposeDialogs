---
icon: material/puzzle
---

This shows a simple dialog with some informational text.

Check out the composable and it's documentation in the code snipplet below.

Generally following can be adjusted:

* an optional label for the information

#### Example

<!-- snippet: Demos::demo-info -->
```kt
if (state.visible) {
    DialogInfo(
        state = state,
        title = { Text("Dialog") },
        info = "Simple Info Dialog",
        icon = icon,
        style = style,
        onEvent = { event ->
            showInfo("Event $event")
        }
    )
}
```
<!-- endSnippet -->
  
#### Composable

<!-- snippet: DialogInfo::constructor -->
```kt
/**
 * Shows a dialog with an info text and an optional label for that info
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param info the information text for this dialog
 * @param infoLabel the optional label for the information text
 */
@Composable
fun DialogInfo(
    state: DialogState,
    // Custom - Required
    info: String,
    // Custom - Optional
    infoLabel: String = "",
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

| | | |                                                   |
|-|-|-|---------------------------------------------------|
| ![Screenshot](../screenshots/info/demo_info1.jpg) | ![Screenshot](../screenshots/info/demo_info2.jpg) | ![Screenshot](../screenshots/info/demo_info3.jpg) | ![Screenshot](../screenshots/info/demo_info4.jpg) |
