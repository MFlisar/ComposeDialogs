---
icon: material/puzzle
---

This shows a dialog with a list of items. Rendering, selection mode and more is adjustable.

Check out the composable and it's documentation in the code snipplet below.

Generally following can be adjusted:

* list of items or loading list items asynchronously
* single selection / multi selection / clickable
* optional dividers
* filter and search functionality
* custom items

#### Example

```kotlin
val state = rememberDialogState()
val selected = remember { mutableStateOf<Int?>(null) }
val items = List(100) { "Item $it" }
DialogList(
    style = style,
    title = { Text("List Dialog") },
    icon = getIcon,
    buttons = buttons,
    description = "Some optional description",
    state = state,
    items = items,
    itemIdProvider = { items.indexOf(it) },
    selectionMode = DialogList.SelectionMode.SingleSelect(
        selected = selected,
        selectOnRadioButtonClickOnly = false
    ),
    itemContents = DialogList.ItemDefaultContent(
        text = { it }
    ),
    onEvent = {
        val info = if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
            "Selected list value: ${selected.value?.let { "Index = $it | Item = ${items[it]}" }}"
        } else {
            "Event $it"
        }
        // ...
    }
)
```

#### Composable

There are 2 ways to show a list, one by providing a list of items and one by providing an asynchronous item loader function.

##### List

<!-- snippet: DialogList::constructor1 -->
```kt
/**
 * Shows a dialog with a list and an optional filter option
 *
 * consider the overload with a lambda for the items parameter if items should be loaded lazily
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param items the list items
 * @param itemIdProvider the items to id lambda that is used to store selected item ids
 * @param itemContents the [DialogList.ItemContents] holding composables to customise the rendering of the list items - use [DialogList.ItemDefaultContent] or [DialogList.ItemContents] if you want to completely customise the items
 * @param selectionMode the [DialogList.SelectionMode]
 * @param divider if true, a divider is shown between the list items
 * @param description a custom text that will be shown as description at the top of the dialog
 * @param filter the [DialogList.Filter] - if it is null, filtering is disabled
 */
@Composable
fun <T> DialogList(
    state: DialogState,
    // Custom - Required
    items: List<T>,
    itemIdProvider: (item: T) -> Int,
    itemContents: DialogList.ItemContents<T>,
    selectionMode: DialogList.SelectionMode<T>,
    // Custom - Optional
    divider: Boolean = false,
    description: String = "",
    filter: DialogList.Filter<T>? = null,
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

##### Loader

<!-- snippet: DialogList::constructor2 -->
```kt
/**
 * Shows a dialog with a list and an optional filter option
 *
 * consider the overload with a list if the items are just a simple list of items
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param itemsLoader the lambda that will return the items for this dialog
 * @param itemIdProvider the items to id lambda that is used to store selected item ids
 * @param itemContents the [DialogList.ItemContents] holding composables to customise the rendering of the list items - use [DialogList.ItemDefaultContent] or [DialogList.ItemContents] if you want to completely customise the items
 * @param selectionMode the [DialogList.SelectionMode]
 * @param itemSaver the saver for the list items - if no itemSaver is provided, data won't be remembered as saveable and will be reloaded on recomposition (e.g. screen rotation)
 * @param loadingIndicator the composable that will be shown while items are loaded
 * @param divider if true, a divider is shown between the list items
 * @param description a custom text that will be shown as description at the top of the dialog
 * @param filter the [DialogList.Filter] - if it is null, filtering is disabled
 */
@Composable
fun <T> DialogList(
    state: DialogState,
    // Custom - Required
    itemsLoader: suspend () -> List<T>,
    itemIdProvider: (item: T) -> Int,
    itemContents: DialogList.ItemContents<T>,
    selectionMode: DialogList.SelectionMode<T>,
    // Custom - Optional
    itemSaver: Saver<MutableState<List<T>>, out Any>? = null,
    loadingIndicator: @Composable () -> Unit = {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    },
    divider: Boolean = false,
    description: String = "",
    filter: DialogList.Filter<T>? = null,
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
| ![Screenshot](../screenshots/list/demo_list1.jpg) | ![Screenshot](../screenshots/list/demo_list2.jpg) | ![Screenshot](../screenshots/list/demo_list3.jpg) | ![Screenshot](../screenshots/list/demo_list4.jpg) |
| ![Screenshot](../screenshots/list/demo_list5.jpg) | ![Screenshot](../screenshots/list/demo_list6.jpg) | ![Screenshot](../screenshots/list/demo_list7.jpg) |                                                   |
