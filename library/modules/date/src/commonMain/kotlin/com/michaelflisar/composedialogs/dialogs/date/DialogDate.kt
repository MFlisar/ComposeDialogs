package com.michaelflisar.composedialogs.dialogs.date

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.key
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
 * @param buttonToday optional content for the Today button; pass null to hide it
 */
@Composable
fun DialogDate(
    state: BaseDialogState,
    date: MutableState<LocalDate>,
    config: DatePickerConfig = DatePickerDefaults.config(),
    buttonToday: (@Composable () -> Unit)? = {
        BasicText(text = DatePickerDefaults.texts().today)
    },
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: DialogOptions = DialogDefaults.options(),
    onEvent: (event: DialogEvent) -> Unit = {},
)
// end-snippet
{
    val landscape = isLandscape()

    val pickerState = key(state.visible) {
        rememberDatePickerState(date, config)
    }

    Dialog(
        state = state,
        title = title?.takeIf { !landscape },
        icon = icon?.takeIf { !landscape },
        style = style,
        buttons = buttons,
        options = options,
        onEvent = onEvent
    ) {
        if (landscape) {
            Row {
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
                        state = pickerState,
                        config = config,
                        arrangeVertically = true,
                        horizontalSpacing = 8.dp,
                        buttonToday = buttonToday
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                DatePicker(
                    uiProvider = Material3DateUIProvider,
                    state = pickerState,
                    config = config,
                    modifier = Modifier
                )
            }
        } else {
            Column {
                DatePickerButtons(
                    uiProvider = Material3DateUIProvider,
                    state = pickerState,
                    config = config,
                    arrangeVertically = false,
                    horizontalSpacing = 8.dp,
                    buttonToday = buttonToday
                )
                Spacer(modifier = Modifier.height(8.dp))
                DatePicker(
                    uiProvider = Material3DateUIProvider,
                    state = pickerState,
                    config = config,
                    modifier = Modifier
                )
            }
        }
    }
}
