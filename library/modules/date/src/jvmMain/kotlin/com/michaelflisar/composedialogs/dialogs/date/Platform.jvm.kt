package com.michaelflisar.composedialogs.dialogs.date

import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.SpecialOptions
import com.michaelflisar.composedialogs.core.specialOptions
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Locale

actual fun DialogDefaults.defaultDateDialogSpecialOptions(): SpecialOptions {
    return specialOptionsDateDialog()
}

actual fun defaultFormatterWeekDayLabel(dayOfWeek: DayOfWeek): String {
    return dayOfWeek.getDisplayName(
        TextStyle.SHORT,
        Locale.getDefault()
    )
}

actual fun defaultFormatterSelectedDate(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
    return date.toJavaLocalDate().format(formatter)
}

actual fun defaultFormatterSelectedMonth(month: Month): String {
    return month.getDisplayName(
        TextStyle.SHORT,
        Locale.getDefault()
    )
}

actual fun defaultFormatterSelectedMonthInSelectorList(month: Month): String {
    return month.getDisplayName(
        TextStyle.FULL,
        Locale.getDefault()
    )
}
