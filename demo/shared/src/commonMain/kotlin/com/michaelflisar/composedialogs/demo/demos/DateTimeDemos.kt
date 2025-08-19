package com.michaelflisar.composedialogs.demo.demos

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.rememberDialogState
import com.michaelflisar.composedialogs.demo.composables.DemoDialogButton
import com.michaelflisar.composedialogs.demo.composables.DemoDialogRegion
import com.michaelflisar.composedialogs.demo.composables.DemoDialogRow
import com.michaelflisar.composedialogs.dialogs.date.DialogDate
import com.michaelflisar.composedialogs.dialogs.date.DialogDateDefaults
import com.michaelflisar.composedialogs.dialogs.date.defaultFormatterSelectedDate
import com.michaelflisar.composedialogs.dialogs.date.defaultFormatterSelectedMonth
import com.michaelflisar.composedialogs.dialogs.date.defaultFormatterSelectedMonthInSelectorList
import com.michaelflisar.composedialogs.dialogs.date.defaultFormatterWeekDayLabel
import com.michaelflisar.composedialogs.dialogs.date.rememberDialogDate
import com.michaelflisar.composedialogs.dialogs.time.DialogTime
import com.michaelflisar.composedialogs.dialogs.time.DialogTimeDefaults
import com.michaelflisar.composedialogs.dialogs.time.rememberDialogTime
import kotlinx.datetime.DayOfWeek

@Composable
fun DateTimeDemos(
    style: ComposeDialogStyle, icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
) {
    DemoDialogRegion("DateTime Dialogs")
    DemoDialogRow {
        DemoDialogDate1(style, icon, showInfo, false)
    }
    DemoDialogRow {
        DemoDialogDate1(style, icon, showInfo, true)
    }
    DemoDialogRow {
        DemoDialogTime1(style, icon, showInfo, false)
        DemoDialogTime1(style, icon, showInfo, true)
    }
}

@Composable
private fun RowScope.DemoDialogDate1(
    style: ComposeDialogStyle,
    icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit,
    customSetup: Boolean
) {
    val state = rememberDialogState()
    /* --8<-- [start: demo-date] */
    if (state.visible) {

        // special state for date dialog
        val date = rememberDialogDate()
        // optional settings
        var setup = DialogDateDefaults.setup(
            dateCellHeight = 32.dp
        )
        if (customSetup) {
            setup = DialogDateDefaults.setup(
                buttonToday = { enabled, onClick ->
                    FilledIconButton(onClick = onClick, enabled = enabled) {
                        Icon(Icons.Default.Today, null)
                    }
                },
                firstDayOfWeek = DayOfWeek.SUNDAY,
                dateCellHeight = 32.dp,
                showNextPreviousMonthButtons = false,
                showNextPreviousYearButtons = false,
                // formats are just defined as they are already by default, but you
                // see how you could simply change them...
                formatterWeekDayLabel = { defaultFormatterWeekDayLabel(it) },
                formatterSelectedDate = {
                    defaultFormatterSelectedDate(it)
                },
                formatterSelectedMonth = {
                    defaultFormatterSelectedMonth(it)
                },
                formatterSelectedYear = { it.toString() },
                formatterMonthSelectorList = {
                    defaultFormatterSelectedMonthInSelectorList(it)
                },
                formatterYearSelectorList = { it.toString() }
            )
        }
        val dateRange = DialogDateDefaults.dateRange()

        DialogDate(
            state = state,
            date = date,
            setup = setup,
            dateRange = dateRange,
            icon = icon,
            title = { Text("Select Date") },
            style = style,
            onEvent = {
                if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                    showInfo("Selected Date: ${date.value}")
                } else {
                    showInfo("Event $it")
                }
            }
        )
    }
    /* --8<-- [end: demo-date] */
    DemoDialogButton(
        state,
        Icons.Default.CalendarMonth,
        "Date Dialog",
        "Shows a date picker dialog" + if (customSetup) {
            " with a custom setup (firstDayOfWeek = SUNDAY, today button is changed to an icon button, next/previous buttons are hidden for year and month)"
        } else ""
    )
}

@Composable
private fun RowScope.DemoDialogTime1(
    style: ComposeDialogStyle,
    icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit,
    is24Hours: Boolean
) {
    val state = rememberDialogState()
    /* --8<-- [start: demo-time] */
    if (state.visible) {

        // special state for time dialog
        val time = rememberDialogTime()
        // optional settings
        val setup = DialogTimeDefaults.setup(is24Hours = is24Hours)

        DialogTime(
            state = state,
            time = time,
            setup = setup,
            icon = icon,
            title = { Text("Select Time") },
            style = style,
            onEvent = {
                if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                    showInfo("Selected Time: ${time.value}")
                } else {
                    showInfo("Event $it")
                }
            }
        )
    }
    /* --8<-- [end: demo-time] */
    DemoDialogButton(
        state,
        Icons.Default.Schedule,
        "Time Dialog",
        "Shows a time picker dialog (24h = $is24Hours)"
    )
}
