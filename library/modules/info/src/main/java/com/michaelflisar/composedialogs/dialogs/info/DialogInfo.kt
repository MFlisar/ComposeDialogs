package com.michaelflisar.composedialogs.dialogs.info

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.core.Dialog
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.DialogStyle
import com.michaelflisar.composedialogs.core.Options

/**
 * Shows a dialog with an info text and an optional label for that info
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param info the information text for this dialog
 * @param infoLabel the optional label for the information text
 */
@Composable
fun DialogInfo(
    state: DialogState,
    // Custom - Required
    info: String,
    // Custom - Optional
    infoLabel: String = "",
    // Base Dialog - Optional
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: DialogStyle = DialogDefaults.styleDialog(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    Dialog(state, title, icon, style, buttons, options, onEvent = onEvent) {
        if (infoLabel.isNotEmpty()) {
            Text(text = infoLabel, style = MaterialTheme.typography.titleSmall)
        }
        Text(text = info)
    }
}