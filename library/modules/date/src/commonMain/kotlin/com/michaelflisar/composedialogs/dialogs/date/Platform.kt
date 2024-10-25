package com.michaelflisar.composedialogs.dialogs.date

import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.SpecialOptions
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

expect fun defaultFormatterWeekDayLabel(dayOfWeek: DayOfWeek): String
expect fun defaultFormatterSelectedDate(date: LocalDate): String
expect fun defaultFormatterSelectedMonth(month: Month): String
expect fun defaultFormatterSelectedMonthInSelectorList(month: Month): String