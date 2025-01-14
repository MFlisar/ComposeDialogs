---
icon: material/keyboard
---

#### Basic examples

```kotlin
// create and remember a state
val state = rememberDialogState()

// Create a dialog composable
if (state.visible) {
    DialogInfo(
        state = state,
        info = "Some "
    )
}

// show the dialog inside a button press event or similar
Button(onClick = { state.show() }) {
    Text("Show Dialog")
}
```

Alternatively you can hold any `saveable` state inside dialog state. If the dialog state holds any data, it will consider itself as visible.

```kotlin
// create and remember a state (e.g. selected list index as in this example)
val state = rememberDialogState<Int>(data = null)

// show a dialog if necessary
if (state.showing)
{
    val data = state.requireData() // in this example the data is the list index
    DialogInfo(
        state = state,
        info = "Data = $data"
    )
}

// a list that uses the dialog
val items = 1..100
LazyColumn {
    items.forEach {
        item(key = it) {
            Button(onClick = { state.show(it) }) {
                Text("Item $it")
            }
        }
    }
}
```

#### Dialog styling/customisation and events

All dialog (also custom ones) do support styling like:

* optional icon (including style like `CenterTop` and `Begin`)
* dialog style (dialog, bottom sheet, fullscreen)
* custom buttons (zero, one or two) with custom texts
* a options variable to set up features like `dismissOnButtonClick`, `dismissOnBackPress` and `dismissOnClickOutside`
* a event callback for the close event and button click event

Check out the advanced region on the side for more details.

#### Dialogs

Check out all the modules to find out more about the available implementations.