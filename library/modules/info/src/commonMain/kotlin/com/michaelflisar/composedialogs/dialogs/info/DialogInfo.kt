package com.michaelflisar.composedialogs.dialogs.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.*
import com.michaelflisar.composedialogs.core.ComposeDialogStyle

/* --8<-- [start: full-constructor] */
/* --8<-- [start: constructor] */
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
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
)
/* --8<-- [end: constructor] */
{
    Dialog(state, title, icon, style, buttons, options, onEvent = onEvent) {
        Column {
            if (infoLabel.isNotEmpty()) {
                Text(modifier = Modifier.padding(bottom = 8.dp), text = infoLabel, style = MaterialTheme.typography.titleSmall)
            }
            Text(text = info)
        }
    }
}
/* --8<-- [end: full-constructor] */