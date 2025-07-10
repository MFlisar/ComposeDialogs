---
icon: material/pencil
---

In general a custom dialog is made is simple as following (info dialog is exactly the following):

```kotlin
--8<-- "../../library/modules/info/src/commonMain/kotlin/com/michaelflisar/composedialogs/dialogs/info/DialogInfo.kt:full-constructor"
```

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