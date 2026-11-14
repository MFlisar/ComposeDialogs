package com.michaelflisar.composedialogs.dialogs.date

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.date.ui.CalendarSelectionHeader
import com.michaelflisar.composedialogs.dialogs.date.ui.CalendarTodayButton
import kotlinx.datetime.LocalDate

/** Renders the selected-date and Today controls for [DatePickerMain]. */
@Composable
fun DatePickerButtons(
    uiProvider: DateUIProvider,
    date: MutableState<LocalDate>,
    config: DatePickerConfig,
    landscape: Boolean,
    selectedDateEnabled: Boolean,
    onSelectedDateClick: () -> Unit,
    todayEnabled: Boolean,
    onTodayClick: () -> Unit
) {
    if (landscape) {
        Column {
            CalendarSelectionHeader(
                state = date,
                config = config,
                enabled = selectedDateEnabled,
                onClick = onSelectedDateClick,
                uiProvider = uiProvider
            )
            config.buttonToday?.let { buttonToday ->
                Spacer(modifier = Modifier.height(8.dp))
                CalendarTodayButton(
                    buttonToday = buttonToday,
                    enabled = todayEnabled,
                    onClick = onTodayClick,
                    uiProvider = uiProvider
                )
            }
        }
    } else {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CalendarSelectionHeader(
                state = date,
                config = config,
                enabled = selectedDateEnabled,
                onClick = onSelectedDateClick,
                uiProvider = uiProvider
            )
            config.buttonToday?.let { buttonToday ->
                Spacer(modifier = Modifier.weight(1f))
                CalendarTodayButton(
                    buttonToday = buttonToday,
                    enabled = todayEnabled,
                    onClick = onTodayClick,
                    uiProvider = uiProvider
                )
            }
        }
    }
}
