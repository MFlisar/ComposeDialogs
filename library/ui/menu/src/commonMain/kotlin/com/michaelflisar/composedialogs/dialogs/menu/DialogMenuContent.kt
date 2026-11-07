package com.michaelflisar.composedialogs.dialogs.menu

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.ui.ComposeDialogsUI
import com.michaelflisar.composedialogs.dialogs.menu.composables.MenuRow

/**
 * Displays menu items and manages submenu navigation independently of a dialog.
 *
 * @param items the root menu items
 * @param onDismiss invoked by menu items configured to dismiss their containing UI
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DialogMenuContent(
    items: List<MenuItem>,
    onDismiss: () -> Unit = {}
) {
    val selectedSubMenu = remember { mutableStateOf<List<MenuItem.SubMenu>>(emptyList()) }
    val visibleItems = remember(selectedSubMenu.value) {
        derivedStateOf {
            val list = selectedSubMenu.value.lastOrNull()?.items
            if (list != null) listOf(MenuItem.BackMenu) + list else items
        }
    }

    BackHandler(enabled = selectedSubMenu.value.isNotEmpty()) {
        selectedSubMenu.value = selectedSubMenu.value.dropLast(1)
    }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        visibleItems.value.forEach {
            when (it) {
                MenuItem.BackMenu -> Row {
                    ComposeDialogsUI.provider.IconButton(
                        onClick = {
                            selectedSubMenu.value = selectedSubMenu.value.dropLast(1)
                        },
                        enabled = true,
                        modifier = Modifier
                    ) {
                        ComposeDialogsUI.provider.Icon(
                            icon = ComposeDialogsUI.IconType.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier,
                            tint = ComposeDialogsUI.provider.color(ComposeDialogsUI.ColorRole.OnSurface)
                        )
                    }
                }

                is MenuItem.Custom -> it.content()

                MenuItem.Divider -> ComposeDialogsUI.provider.HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )

                is MenuItem.Item -> MenuRow(
                    title = it.title,
                    description = it.description,
                    icon = it.icon,
                    onMenuClicked = {
                        it.onClick()
                        if (it.dismissOnClick) onDismiss()
                        Unit
                    }
                )

                is MenuItem.Region -> MenuRow(
                    title = it.title,
                    description = it.description,
                    icon = null,
                    bold = true,
                    onMenuClicked = null
                )

                is MenuItem.SubMenu -> MenuRow(
                    title = it.title,
                    description = it.description,
                    icon = it.icon,
                    endIcon = {
                        ComposeDialogsUI.provider.Icon(
                            icon = ComposeDialogsUI.IconType.ArrowForward,
                            contentDescription = "Open SubMenu",
                            modifier = Modifier,
                            tint = ComposeDialogsUI.provider.color(ComposeDialogsUI.ColorRole.OnSurface)
                        )
                    },
                    onMenuClicked = {
                        selectedSubMenu.value += it
                        Unit
                    }
                )
            }
        }
    }
}
