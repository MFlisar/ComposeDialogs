package com.michaelflisar.composedialogs.dialogs.frequency

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.michaelflisar.composedialogs.core.BaseDialogState
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.Dialog
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogOptions
import com.michaelflisar.composedialogs.core.defaultDialogStyle
import com.michaelflisar.composedialogs.dialogs.frequency.classes.Frequency
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month

// begin-snippet: DialogFrequency::constructor
/**
 * Shows a frequency dialog
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param frequency the frequency state of the dialog
 * @param texts the texts ([DialogFrequency.Texts]) that are used inside this dialog - use [DialogFrequencyDefaults.texts] to provide your own data
 * @param supportedTypes the supported frequency types - default is all types
 * @param firstDayOffset the first day of the week - default is [DayOfWeek.MONDAY]
 */
@Composable
fun DialogFrequency(
    state: BaseDialogState,
    // Custom - Required
    frequency: MutableState<Frequency>,
    // Custom - Optional
    texts: DialogFrequency.Texts = DialogFrequencyDefaults.texts(),
    supportedTypes: List<Frequency.Type> = Frequency.Type.entries,
    firstDayOffset: DayOfWeek = DayOfWeek.MONDAY,
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
    if (state.visible) {
        Dialog(
            state = state,
            title = title,
            icon = icon,
            style = style,
            buttons = buttons,
            options = options,
            onEvent = onEvent
        ) {
            DialogFrequencyContent(frequency, texts, supportedTypes, firstDayOffset)
        }
    }
}
