package com.michaelflisar.composedialogs.core.style

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogContentScrollableColumn
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.Options
import com.michaelflisar.composedialogs.core.SpecialOptions
import com.michaelflisar.composedialogs.core.copied.AlertDialogContent
import com.michaelflisar.composedialogs.core.views.ComposeDialogButton

internal class DialogStyle : ComposeDialogStyle {

    override val type = ComposeDialogStyle.Type.Dialog

    @Composable
    override fun Show(
        title: String?,
        icon: (@Composable () -> Unit)?,
        buttons: DialogButtons,
        options: Options,
        specialOptions: SpecialOptions,
        state: DialogState,
        onEvent: (event: DialogEvent) -> Unit,
        content: @Composable (ColumnScope.() -> Unit)
    ) {
        val dialogState = androidx.compose.ui.window.rememberDialogState(
            position = specialOptions.position,
            width = specialOptions.width,
            height = specialOptions.height
        )
        DialogWindow(
            visible = state.showing,
            onCloseRequest = {
                state.dismiss(onEvent)
            },
            title = title ?: "",
            state = dialogState,
            content = {
                AlertDialogContent(
                    confirmButton = {
                        ComposeDialogButton(
                            buttons.positive,
                            DialogButtonType.Positive,
                            options,
                            state,
                            requestDismiss = {
                                state.dismiss()
                            },
                            onEvent
                        )
                    },
                    dismissButton = if (buttons.negative.text.isNotEmpty()) {
                        {
                            ComposeDialogButton(
                                buttons.negative,
                                DialogButtonType.Negative,
                                options,
                                state,
                                requestDismiss = {
                                    state.dismiss()
                                },
                                onEvent
                            )
                        }
                    } else null,
                    icon = icon,
                    title = null,
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape,
                    containerColor = MaterialTheme.colorScheme.background,
                    iconContentColor = MaterialTheme.colorScheme.onBackground,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    textContentColor = MaterialTheme.colorScheme.onBackground,
                    tonalElevation = 0.dp,
                    buttonContentColor = MaterialTheme.colorScheme.primary,
                    contentShouldFillHeight = true,
                    textPadding = PaddingValues(bottom = 8.dp),
                    text = {
                        if (options.wrapContentInScrollableContainer) {
                            DialogContentScrollableColumn(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                content = content
                            )
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                content()
                            }
                        }
                    }
                )
            }
        )
    }
}