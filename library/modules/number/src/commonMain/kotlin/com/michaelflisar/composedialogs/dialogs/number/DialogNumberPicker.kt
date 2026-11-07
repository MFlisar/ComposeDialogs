package com.michaelflisar.composedialogs.dialogs.number

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.michaelflisar.composedialogs.core.BaseDialogState
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.Dialog
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.defaultDialogStyle
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.DialogOptions

// begin-snippet: DialogNumberPicker::constructor
/**
 * Shows a dialog with a number picker
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param value the number state for this dialog
 * @param setup the [NumberPickerSetup]
 * @param iconDown the icon for the decrease button
 * @param iconUp the icon for the increase button
 * @param formatter the formatter for the text of this picker
 * @param textStyle the [TextStyle] for the text of this picker
 * @param onValueStateChanged a callback that will be called whenever the value of the number picker changes
 */
@Composable
fun <T : Number> DialogNumberPicker(
    // Base Dialog - State
    state: BaseDialogState,
    // Custom - Required
    value: MutableState<T>,
    setup: NumberPickerSetup<T>,
    iconDown: @Composable () -> Unit = { DialogNumberPickerDefaults.IconDown() },
    iconUp: @Composable () -> Unit = { DialogNumberPickerDefaults.IconUp() },
    iconDown2: @Composable () -> Unit = { DialogNumberPickerDefaults.IconDown2() },
    iconUp2: @Composable () -> Unit = { DialogNumberPickerDefaults.IconUp2() },
    formatter: @Composable (value: T) -> String = { it.toString() },
    // Custom - Optional
    textStyle: TextStyle = DialogNumberPickerDefaults.textStyle(),
    onValueStateChanged: (value: T) -> Unit = { },
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
        DialogNumberPickerContent(
            value = value,
            setup = setup,
            iconDown = iconDown,
            iconUp = iconUp,
            iconDown2 = iconDown2,
            iconUp2 = iconUp2,
            formatter = formatter,
            textStyle = textStyle,
            onValueStateChanged = onValueStateChanged,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
