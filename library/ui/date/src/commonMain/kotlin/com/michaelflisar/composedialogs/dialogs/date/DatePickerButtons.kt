package com.michaelflisar.composedialogs.dialogs.date

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.date.ui.CalendarSelectionHeader
import com.michaelflisar.composedialogs.dialogs.date.ui.CalendarTodayButton

/**
 * Renders controls that navigate the shared [DatePickerState].
 *
 * @param buttonToday optional Today button content; pass null to hide it
 */
@Composable
fun DatePickerButtons(
    uiProvider: DateUIProvider,
    state: DatePickerState,
    config: DatePickerConfig,
    arrangeVertically: Boolean,
    horizontalSpacing: Dp,
    buttonToday: (@Composable () -> Unit)? = {
        BasicText(text = DatePickerDefaults.texts().today)
    }
) {
    if (arrangeVertically) {
        Column {
            CalendarSelectionHeader(
                state = state.date,
                config = config,
                enabled = state.selectedDateEnabled,
                onClick = state::onSelectedDateClick,
                uiProvider = uiProvider
            )
            buttonToday?.let { buttonTodayContent ->
                Spacer(modifier = Modifier.height(8.dp))
                CalendarTodayButton(
                    buttonToday = buttonTodayContent,
                    enabled = state.todayEnabled,
                    onClick = state::onTodayClick,
                    uiProvider = uiProvider
                )
            }
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CalendarSelectionHeader(
                state = state.date,
                config = config,
                enabled = state.selectedDateEnabled,
                onClick = state::onSelectedDateClick,
                uiProvider = uiProvider
            )
            Spacer(modifier = Modifier.width(horizontalSpacing))
            buttonToday?.let { buttonTodayContent ->
                Spacer(modifier = Modifier.weight(1f))
                CalendarTodayButton(
                    buttonToday = buttonTodayContent,
                    enabled = state.todayEnabled,
                    onClick = state::onTodayClick,
                    uiProvider = uiProvider
                )
            }
        }
    }
}
