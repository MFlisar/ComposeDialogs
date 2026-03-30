In general a custom dialog is made as simple as following (info dialog is exactly the following):

<!-- snippet: DialogInfo::full-constructor -->
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
{
    Dialog(state, title, icon, style, buttons, options, onEvent = onEvent) {
        Column {
            if (infoLabel.isNotEmpty()) {
                Text(modifier = Modifier.padding(bottom = 8.dp), text = infoLabel, style = MaterialTheme.typography.titleSmall)
            }
            Text(text = info)
        }
    }
}
```
<!-- endSnippet -->

So in the end following is the base structure for a new dialog:

```kotlin
@Composable
fun DialogInfo(
    state: DialogState,
    // Custom
    // custom data for you custom dialog
    // ....
    // Base Dialog - Optional
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    Dialog(state, title, icon, style, buttons, options, onEvent = onEvent) {
        Column {
            // custom content - whatever you want
        }
    }
}
```

This dialog will automatically support all the features of this library.

#### Important note

Sometimes you want show some scrollable content inside a dialog and don't want this content to stretch the dialog height in case, that the content does not show a lot of data. You can use following function from the core module in this case.

Both function will take care to show scrollbars if necessary and will not stretch the dialog height if the content inside it is small.

```kotlin
Dialog(state, title, icon, style, buttons, options, onEvent = onEvent) {

    // non lazy
    DialogContentScrollableColumn { 

    }

    // or lazy
    DialogContentScrollableLazyColumn { 
        
    }
}
```
