package com.michaelflisar.composedialogs.dialogs.date

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import com.michaelflisar.composedialogs.core.BaseDialogState
import com.michaelflisar.composedialogs.core.Dialog
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.defaultDialogStyle
import com.michaelflisar.composedialogs.core.isLandscape
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.DialogOptions
import kotlinx.datetime.LocalDate

// begin-snippet: DialogDate::constructor
/**
 * Shows a dialog with an input field
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param date the selected date state
 * @param dateRange the supported [DialogDate.Range] - use [DialogDateDefaults.dateRange] to provide your own data
 * @param setup the [DialogDate.Setup] - use [DialogDateDefaults.setup] to provide your own data
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DialogDate(
    state: BaseDialogState,
    // Custom - Required
    date: MutableState<LocalDate>,
    // Custom - Optional
    dateRange: DialogDate.Range = DialogDateDefaults.dateRange(),
    setup: DialogDate.Setup = DialogDateDefaults.setup(),
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
    val landscape = isLandscape()
    Dialog(
        state,
        title?.takeIf { !landscape },
        icon?.takeIf { !landscape },
        style,
        buttons,
        options,
        onEvent = onEvent
    ) {
        DialogDateContent(date, dateRange, setup, title, icon)
    }
}
