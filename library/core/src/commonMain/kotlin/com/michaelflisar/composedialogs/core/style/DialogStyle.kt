package com.michaelflisar.composedialogs.core.style

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.DialogProperties
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.Options
import com.michaelflisar.composedialogs.core.SpecialOptions
import com.michaelflisar.composedialogs.core.internal.ComposeAlertDialog

internal class DialogStyle(
    swipeDismissable: Boolean,
    // DialogProperties
    dialogProperties: DialogProperties,
    // AlertDialog Settings
    shape: Shape,
    containerColor: Color,
    iconContentColor: Color,
    titleContentColor: Color,
    textContentColor: Color,
    tonalElevation: Dp
) : ComposeDialogStyle2 {

    override val type = ComposeDialogStyle2.Type.Dialog

    private val data = ComposeAlertDialog.Data(
        swipeDismissable = swipeDismissable,
        dialogProperties = dialogProperties,
        shape = shape,
        containerColor = containerColor,
        iconContentColor = iconContentColor,
        titleContentColor = titleContentColor,
        textContentColor = textContentColor,
        tonalElevation = tonalElevation
    )

    @Composable
    override fun Show(
        title: (@Composable () -> Unit)?,
        icon: @Composable (() -> Unit)?,
        buttons: DialogButtons,
        options: Options,
        specialOptions: SpecialOptions,
        state: DialogState,
        onEvent: (event: DialogEvent) -> Unit,
        content: @Composable (ColumnScope.() -> Unit)
    ) {
        ComposeAlertDialog(data, title, icon, buttons, options, specialOptions, state, onEvent, content)
    }
}