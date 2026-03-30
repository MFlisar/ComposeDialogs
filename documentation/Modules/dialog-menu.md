---
icon: material/puzzle
---

This shows a menu dialog that even supports unlimited nesting levels.

Check out the composable and it's documentation in the code snipplet below.

Generally following can be adjusted:

* menu items with optional icons
* unlimited level of nesting
* also supports divider items and custom items

#### Example

<!-- snippet: Demos::demo-menu -->
```kt
DialogMenu(
    style = style,
    title = { Text("Menu Dialog") },
    icon = icon,
    items = items,
    state = state
)
```
<!-- endSnippet -->

Here's a full example of a menu that can be rendered:

<!-- snippet: Demos::demo-menu-items -->
```kt
val items = listOf(
    MenuItem.Item(
        title = "Item 1",
        description = "Description 1",
        icon = { Icon(Icons.Default.Info, null) }) {
        showInfo("Item 1 clicked")
    },
    MenuItem.Item(
        title = "Item 2",
        description = "Description 2",
        icon = { Icon(Icons.Default.Info, null) }) {
        showInfo("Item 2 clicked")
    },
    MenuItem.Divider,
    MenuItem.SubMenu(
        title = "Sub Menu 1",
        description = "Description",
        icon = { Icon(Icons.Default.Info, null) },
        items = listOf(
            MenuItem.Item(
                title = "Sub Item 1",
                description = "Description 1",
                icon = { Icon(Icons.Default.Info, null) }) {
                showInfo("Sub Item 1 clicked")
            },
            MenuItem.Item(
                title = "Sub Item 2",
                description = "Description 2",
                icon = { Icon(Icons.Default.Info, null) }) {
                showInfo("Sub Item 2 clicked")
            },
            MenuItem.Region(
                "Region X"
            ),
            MenuItem.Item(
                title = "Sub Item 3",
                description = "Description 3",
                icon = { Icon(Icons.Default.Info, null) }) {
                showInfo("Sub Item 3 clicked")
            },
            MenuItem.SubMenu(
                title = "Sub Sub Menu 4",
                description = "Description",
                icon = { Icon(Icons.Default.Info, null) },
                items = listOf(
                    MenuItem.Item(
                        title = "Sub Sub Item 1",
                        description = "Description",
                        icon = { Icon(Icons.Default.Info, null) }) {
                        showInfo("Sub Sub Item 1 clicked")
                    },
                    MenuItem.Item(
                        title = "Sub Sub Item 2",
                        description = "Description",
                        icon = { Icon(Icons.Default.Info, null) }) {
                        showInfo("Sub Sub Item 2 clicked")
                    },
                    MenuItem.Item(
                        title = "Sub Sub Item 3",
                        description = "Description",
                        icon = { Icon(Icons.Default.Info, null) }) {
                        showInfo("Sub Sub Item 3 clicked")
                    },
                    MenuItem.Item(
                        title = "Sub Sub Item 4",
                        description = "Description",
                        icon = { Icon(Icons.Default.Info, null) }) {
                        showInfo("Sub Sub Item 4 clicked")
                    },
                    MenuItem.Item(
                        title = "Sub Sub Item 5",
                        description = "Description",
                        icon = { Icon(Icons.Default.Info, null) }) {
                        showInfo("Sub Sub Item 5 clicked")
                    },
                    MenuItem.Item(
                        title = "Sub Sub Item 6",
                        description = "Description",
                        icon = { Icon(Icons.Default.Info, null) }) {
                        showInfo("Sub Sub Item 6 clicked")
                    },
                    MenuItem.Item(
                        title = "Sub Sub Item 7",
                        description = "Description",
                        icon = { Icon(Icons.Default.Info, null) }) {
                        showInfo("Sub Sub Item 7 clicked")
                    },
                )
            )
        )
    ),
    //MenuItem.Custom {
    //    Text("Custom Content", color = Color.Red)
    //}
)
```
<!-- endSnippet -->

#### Composable

<!-- snippet: DialogMenu::constructor -->
```kt
/**
 * Shows a dialog holdinh a list of menu items
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param items a list of menu items
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DialogMenu(
    state: DialogState,
    // Custom - Required
    items: List<MenuItem>,
    // Base Dialog - Optional
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogButtons.DISABLED,
    options: DialogOptions = DialogDefaults.options(),
    onEvent: (event: DialogEvent) -> Unit = {},
)
```
<!-- endSnippet -->

#### Screenshots

|                                                   |
|---------------------------------------------------|
| ![Screenshot](../screenshots/menu/demo_menu1.jpg) |
