package com.michaelflisar.composedialogs.demo.demos

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.rememberDialogState
import com.michaelflisar.democomposables.DemoButton
import com.michaelflisar.democomposables.layout.DemoRegion
import com.michaelflisar.democomposables.layout.DemoRow
import com.michaelflisar.composedialogs.dialogs.menu.DialogMenu
import com.michaelflisar.composedialogs.dialogs.menu.MenuItem

@Composable
fun MenuDemos(
    style: ComposeDialogStyle, icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
) {
    DemoRegion("Menu Dialogs")
    DemoRow {
        DemoMenu(style, icon, showInfo)
    }
}

@Composable
private fun RowScope.DemoMenu(
    style: ComposeDialogStyle, icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
) {
    val state = rememberDialogState()
    /* --8<-- [start: demo] */
    if (state.visible) {
        /* --8<-- [start: demo-menu-items] */
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
        /* --8<-- [end: demo-menu-items] */
        /* --8<-- [start: demo-menu] */
        DialogMenu(
            style = style,
            title = { Text("Menu Dialog") },
            icon = icon,
            items = items,
            state = state
        )
        /* --8<-- [end: demo-menu] */
    }
    DemoButton(
        Icons.Default.Info,
        "Simple Menu Dialog",
        "Shows a menu dialog"
    ) {
        state.show()
    }
}