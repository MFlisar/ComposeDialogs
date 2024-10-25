package com.michaelflisar.composedialogs.dialogs.date

import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.SpecialOptions
import com.michaelflisar.composedialogs.core.specialOptions
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.format

actual fun defaultFormatterWeekDayLabel(dayOfWeek: DayOfWeek): String {
    return dayOfWeek.name
}

actual fun defaultFormatterSelectedDate(date: LocalDate): String {
    return date.format(LocalDate.Formats.ISO)
}

actual fun defaultFormatterSelectedMonth(month: Month): String {
    return month.name
}

actual fun defaultFormatterSelectedMonthInSelectorList(month: Month): String {
    return month.name
}