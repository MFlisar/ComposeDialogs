package com.michaelflisar.composedialogs.dialogs.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.*

/**
 * Shows a dialog with an info text and an optional label for that info
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param items a list of menu items
 */
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
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {},
) {
    val selectedSubMenu = remember { mutableStateOf<List<MenuItem.SubMenu>>(emptyList()) }
    val visibleItems = remember(selectedSubMenu.value) {
        derivedStateOf {
            val list = selectedSubMenu.value.lastOrNull()?.items
            if (list != null) {
                listOf(MenuItem.BackMenu) + list
            } else items
        }
    }

    BackHandler(enabled = selectedSubMenu.value.isNotEmpty()) {
        selectedSubMenu.value = selectedSubMenu.value.dropLast(1)
    }

    Dialog(state, title, icon, style, buttons, options, onEvent = onEvent) {
        DialogContentScrollableColumn(
            modifier = Modifier
                .padding(vertical = 8.dp)
        ) {
            visibleItems.value.forEach {
                when (it) {
                    MenuItem.BackMenu -> {
                        Row {
                            IconButton(
                                onClick = {
                                    selectedSubMenu.value = selectedSubMenu.value.dropLast(1)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    }

                    is MenuItem.Custom -> {
                        it.content()
                    }

                    MenuItem.Divider -> {
                        HorizontalDivider(
                            modifier = Modifier.padding(
                                horizontal = 8.dp,
                                vertical = 4.dp
                            )
                        )
                    }

                    is MenuItem.Item -> {
                        MenuRow(
                            title = it.title,
                            description = it.description,
                            icon = it.icon,
                            onMenuClicked = {
                                it.onClick()
                                state.dismiss()
                            }
                        )
                    }

                    is MenuItem.SubMenu -> {
                        MenuRow(
                            title = it.title,
                            description = it.description,
                            icon = it.icon,
                            endIcon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Open SubMenu"
                                )
                            },
                            onMenuClicked = {
                                selectedSubMenu.value += it
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MenuRow(
    title: String,
    description: String?,
    icon: @Composable (() -> Unit)?,
    endIcon: @Composable (() -> Unit)? = null,
    onMenuClicked: () -> Unit,
) {
    val paddingVertical = 8.dp
    val paddingHorizontal = 16.dp

    val maxLinesText = 2
    val maxLinesSupportingText = 2

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .height(IntrinsicSize.Min)
            .clip(MaterialTheme.shapes.small)
            .clickable {
                onMenuClicked()
            }
            .padding(horizontal = paddingHorizontal),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Icon
        icon?.invoke()

        // Content
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = paddingVertical)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = maxLinesText,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (description?.isNotEmpty() == true) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = maxLinesSupportingText,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Icon end
        endIcon?.invoke()
    }
}