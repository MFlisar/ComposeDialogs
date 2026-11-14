package com.michaelflisar.composedialogs.dialogs.date

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.BaseDialogState
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.Dialog
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogOptions
import com.michaelflisar.composedialogs.core.defaultDialogStyle
import com.michaelflisar.composedialogs.core.isLandscape
import com.michaelflisar.composedialogs.dialogs.date.internal.Material3DateUIProvider
import kotlinx.datetime.LocalDate

// begin-snippet: DialogDate::constructor
/**
 * Shows a dialog with a date picker.
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param date the selected date state
 * @param config the date picker's formatting, behavior, and supported year range
 */
@Composable
fun DialogDate(
    state: BaseDialogState,
    date: MutableState<LocalDate>,
    config: DatePickerConfig = DatePickerDefaults.config(),
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: DialogOptions = DialogDefaults.options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
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
        DatePickerMain(
            uiProvider = Material3DateUIProvider,
            date = date,
            config = config,
            landscape = landscape
        ) { selectedDateEnabled, onSelectedDateClick, todayEnabled, onTodayClick ->
            if (landscape) {
                Column {
                    if (icon != null) {
                        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                            icon()
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    if (title != null) {
                        ProvideTextStyle(MaterialTheme.typography.headlineSmall) {
                            Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                title()
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    DatePickerButtons(
                        uiProvider = Material3DateUIProvider,
                        date = date,
                        config = config,
                        landscape = true,
                        selectedDateEnabled = selectedDateEnabled,
                        onSelectedDateClick = onSelectedDateClick,
                        todayEnabled = todayEnabled,
                        onTodayClick = onTodayClick
                    )
                }
            } else {
                DatePickerButtons(
                    uiProvider = Material3DateUIProvider,
                    date = date,
                    config = config,
                    landscape = false,
                    selectedDateEnabled = selectedDateEnabled,
                    onSelectedDateClick = onSelectedDateClick,
                    todayEnabled = todayEnabled,
                    onTodayClick = onTodayClick
                )
            }
        }
    }
}
// end-snippet
