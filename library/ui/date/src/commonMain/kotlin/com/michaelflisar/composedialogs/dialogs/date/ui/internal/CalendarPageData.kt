package com.michaelflisar.composedialogs.dialogs.date.ui.internal

import androidx.compose.runtime.Immutable
import com.michaelflisar.composedialogs.dialogs.date.ui.DatePickerRange
import kotlin.math.floor

@Immutable
internal class CalendarPageData(
    page: Int,
    dateRange: DatePickerRange
) {
    val yearsOffset = floor(page.toFloat() / 12f).toInt()
    val year = dateRange.years.first + yearsOffset
    val month = page - yearsOffset * 12 + 1
}