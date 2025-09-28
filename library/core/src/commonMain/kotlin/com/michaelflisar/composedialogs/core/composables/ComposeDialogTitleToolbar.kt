package com.michaelflisar.composedialogs.core.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogOptions
import com.michaelflisar.composedialogs.core.internal.ComposeDialogButton
import com.michaelflisar.composedialogs.core.internal.ComposeDialogImageButton
import com.michaelflisar.composedialogs.core.internal.TitleIcon
import com.michaelflisar.composedialogs.core.internal.TitleTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColumnScope.ComposeDialogTitleToolbar(
    modifier: Modifier = Modifier,
    title: @Composable (() -> Unit)?,
    icon: @Composable (() -> Unit)?,
    toolbarColor: Color,
    toolbarActionColor: Color,
    iconColor: Color,
    titleColor: Color,
    menuActions: @Composable (RowScope.() -> Unit)?,
    buttons: DialogButtons,
    state: DialogState,
    options: DialogOptions,
    dismissOnButtonPressed: () -> Unit,
    onEvent: (event: DialogEvent) -> Unit,
    // added in 3.0.1
    navigationIcon: (@Composable () -> Unit)?
) {
    Column(modifier = modifier) {
        TopAppBar(
            title = {
                TitleTitle(title, titleColor, Modifier)
            },
            navigationIcon = {
                if (navigationIcon != null) {
                    navigationIcon()
                } else {
                    ComposeDialogImageButton(
                        buttonType = DialogButtonType.Negative,
                        icon = Icons.Default.Close,
                        state = state,
                        options = options,
                        dismissOnButtonPressed = dismissOnButtonPressed,
                        onEvent = onEvent
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = toolbarColor,
                titleContentColor = titleColor,
                actionIconContentColor = titleColor,
                navigationIconContentColor = titleColor
            ),
            actions = menuActions ?: {
                ComposeDialogButton(
                    button = buttons.positive,
                    buttonType = DialogButtonType.Positive,
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = toolbarColor,
                        contentColor = toolbarActionColor
                    ),
                    state = state,
                    options = options,
                    dismissOnButtonPressed = dismissOnButtonPressed,
                    onEvent = onEvent
                )
            }
        )
        TitleIcon(icon, iconColor, Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColumnScope.ComposeDialogTitleToolbar(
    modifier: Modifier = Modifier,
    title: @Composable (() -> Unit)?,
    icon: @Composable (() -> Unit)?,
    toolbarColor: Color,
    toolbarActionColor: Color,
    iconColor: Color,
    titleColor: Color,
    menuActions: @Composable (RowScope.() -> Unit)?,
    buttons: DialogButtons,
    state: DialogState,
    options: DialogOptions,
    dismissOnButtonPressed: () -> Unit,
    onEvent: (event: DialogEvent) -> Unit,
) {
    ComposeDialogTitleToolbar(
        modifier = modifier,
        title = title,
        icon = icon,
        toolbarColor = toolbarColor,
        toolbarActionColor = toolbarActionColor,
        iconColor = iconColor,
        titleColor = titleColor,
        menuActions = menuActions,
        buttons = buttons,
        state = state,
        options = options,
        dismissOnButtonPressed = dismissOnButtonPressed,
        onEvent = onEvent,
        navigationIcon = null
    )

}