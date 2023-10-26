package com.michaelflisar.composedialogs.demo.demos

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogStyle
import com.michaelflisar.composedialogs.core.rememberDialogState
import com.michaelflisar.composedialogs.demo.DemoDialogButton
import com.michaelflisar.composedialogs.demo.DemoDialogRegion
import com.michaelflisar.composedialogs.demo.DemoDialogRow
import com.michaelflisar.composedialogs.demo.showToast
import com.michaelflisar.composedialogs.dialogs.date.DialogDate
import com.michaelflisar.composedialogs.dialogs.date.DialogDateDefaults
import com.michaelflisar.composedialogs.dialogs.time.DialogTime
import com.michaelflisar.composedialogs.dialogs.time.DialogTimeDefaults
import com.michaelflisar.composedialogs.dialogs.date.rememberDialogDate
import com.michaelflisar.composedialogs.dialogs.time.rememberDialogTime
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DateTimeDemos(style: DialogStyle, icon: (@Composable () -> Unit)?) {
    DemoDialogRegion("DateTime Dialogs")
    DemoDialogRow {
        DemoDialogDate1(style, icon, false)
    }
    DemoDialogRow {
        DemoDialogDate1(style, icon, true)
    }
    DemoDialogRow {
        DemoDialogTime1(style, icon, false)
        DemoDialogTime1(style, icon, true)
    }
}

@Composable
private fun RowScope.DemoDialogDate1(style: DialogStyle, icon: (@Composable () -> Unit)?, customSetup: Boolean) {
    val context = LocalContext.current
    val state = rememberDialogState()
    if (state.showing) {

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
                formatterWeekDayLabel = { it.getDisplayName(TextStyle.SHORT, Locale.getDefault()) },
                formatterSelectedDate = { it.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)) },
                formatterSelectedMonth = { it.getDisplayName(TextStyle.SHORT, Locale.getDefault()) },
                formatterSelectedYear = { it.toString() },
                formatterMonthSelectorList = { it.getDisplayName(TextStyle.FULL, Locale.getDefault()) },
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
                    context.showToast("Selected Date: ${date.value}")
                } else {
                    context.showToast("Event $it")
                }
            }
        )
    }
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
private fun RowScope.DemoDialogTime1(style: DialogStyle, icon: (@Composable () -> Unit)?, is24Hours: Boolean) {
    val context = LocalContext.current
    val state = rememberDialogState()
    if (state.showing) {

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
                    context.showToast("Selected Time: ${time.value}")
                } else {
                    context.showToast("Event $it")
                }
            }
        )
    }
    DemoDialogButton(
        state,
        Icons.Default.Schedule,
        "Time Dialog",
        "Shows a time picker dialog (24h = $is24Hours)"
    )
}
