package com.michaelflisar.composedialogs.dialogs.time

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.michaelflisar.composedialogs.core.*
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import kotlinx.datetime.LocalTime

// begin-snippet: DialogTime::constructor
/**
 * Shows a dialog with a time input
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param time the selected time
 * @param setup the [DialogTime.Setup] - use [DialogTimeDefaults.setup] to provide your own data
 */
@Composable
fun DialogTime(
    state: BaseDialogState,
    // Custom - Required
    time: MutableState<LocalTime>,
    // Custom - Optional
    setup: DialogTime.Setup = DialogTimeDefaults.setup(),
    // Base Dialog - Optional
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: DialogOptions = DialogDefaults.options(),
    onEvent: (event: DialogEvent) -> Unit = {}
)
// end-snippet
{
    Dialog(state, title, icon, style, buttons, options, onEvent = onEvent) {
        DialogTimeContent(time, setup)
    }
}
