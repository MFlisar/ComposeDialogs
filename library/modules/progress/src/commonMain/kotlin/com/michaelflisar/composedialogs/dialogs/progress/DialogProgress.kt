package com.michaelflisar.composedialogs.dialogs.progress

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.core.BaseDialogState
import com.michaelflisar.composedialogs.core.Dialog
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.defaultDialogStyle
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.DialogOptions

// begin-snippet: DialogProgress::constructor
/**
 * Shows a dialog with an optional label and a progress indicator
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param content the content of the progress
 * @param progressStyle the style of the progress indicator ([DialogProgress.Style])
 */
@Composable
fun DialogProgress(
    state: BaseDialogState,
    // Custom - Required
    // ...
    // Custom - Optional
    content: (@Composable ColumnScope.() -> Unit)? = null,
    progressStyle: DialogProgress.Style = DialogProgress.Style.Indeterminate(),
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
    Dialog(state, title, icon, style, buttons, options, onEvent) {
        DialogProgressContent(content, progressStyle)
    }
}
