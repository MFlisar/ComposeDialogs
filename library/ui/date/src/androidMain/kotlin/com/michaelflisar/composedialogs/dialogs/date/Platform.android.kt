package com.michaelflisar.composedialogs.dialogs.date

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.toJavaDayOfWeek
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaMonth
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Locale

actual fun defaultFormatterWeekDayLabel(dayOfWeek: DayOfWeek): String {
    return dayOfWeek.toJavaDayOfWeek().getDisplayName(
        TextStyle.SHORT,
        Locale.getDefault()
    )
}

actual fun defaultFormatterSelectedDate(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
    return date.toJavaLocalDate().format(formatter)
}

actual fun defaultFormatterSelectedMonth(month: Month): String {
    return month.toJavaMonth().getDisplayName(
        TextStyle.SHORT,
        Locale.getDefault()
    )
}

actual fun defaultFormatterSelectedMonthInSelectorList(month: Month): String {
    return month.toJavaMonth().getDisplayName(
        TextStyle.FULL,
        Locale.getDefault()
    )
}
