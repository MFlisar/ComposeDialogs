package com.michaelflisar.composedialogs.core.style

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.DialogStyleDesktopOtions
import com.michaelflisar.composedialogs.core.Options
import com.michaelflisar.composedialogs.core.internal.ComposeDialogButtons
import com.michaelflisar.composedialogs.core.internal.ComposeDialogContent
import com.michaelflisar.composedialogs.core.internal.ComposeDialogTitle

class DialogStyleDesktop(
    val desktopOptions: DialogStyleDesktopOtions,
    // Style
    private val iconColor: Color,
    private val titleColor: Color,
    private val contentColor: Color
) : ComposeDialogStyle {

    override val type = ComposeDialogStyle.Type.Dialog

    @Composable
    override fun Show(
        title: (@Composable () -> Unit)?,
        icon: (@Composable () -> Unit)?,
        buttons: DialogButtons,
        options: Options,
        state: DialogState,
        onEvent: (event: DialogEvent) -> Unit,
        content: @Composable () -> Unit
    ) {
        val dialogState = androidx.compose.ui.window.rememberDialogState(
            position = desktopOptions.position,
            width = desktopOptions.width,
            height = desktopOptions.height
        )
        DialogWindow(
            visible = state.showing,
            onCloseRequest = {
                state.dismiss(onEvent)
            },
            title = desktopOptions.dialogTitle,
            state = dialogState,
            content = {
                Column(
                    modifier = Modifier.fillMaxSize().padding(all = 16.dp)
                ) {

                    // Icon + Title
                    ComposeDialogTitle(null, icon, iconColor = iconColor, titleColor = titleColor)

                    // Content
                    ComposeDialogContent(content, contentColor, fill = true)

                    // Buttons
                    ComposeDialogButtons(
                        buttons = buttons,
                        options = options,
                        state = state,
                        dismissOnButtonPressed = {
                            state.dismiss()
                        },
                        onEvent = onEvent
                    )
                }
            }
        )
    }
}