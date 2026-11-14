package com.michaelflisar.composedialogs.dialogs.date

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

@Immutable
class DatePickerConfig internal constructor(
    val years: IntRange,
    val buttonToday: (@Composable () -> Unit)?,
    val firstDayOfWeek: DayOfWeek,
    val formatterWeekDayLabel: @Composable (date: DayOfWeek) -> String,
    val formatterSelectedDate: @Composable (date: LocalDate) -> String,
    val formatterSelectedMonth: @Composable (month: Month) -> String,
    val formatterSelectedYear: @Composable (year: Int) -> String,
    val formatterMonthSelectorList: @Composable (month: Month) -> String,
    val formatterYearSelectorList: @Composable (year: Int) -> String,
    val dateCellHeight: Dp,
    val showNextPreviousMonthButtons: Boolean,
    val showNextPreviousYearButtons: Boolean
)
