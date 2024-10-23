package com.michaelflisar.composedialogs.dialogs.date.classes

import com.michaelflisar.composedialogs.dialogs.date.DialogDate
import kotlin.math.floor

internal class CalendarPageData(
    page: Int,
    dateRange: DialogDate.Range
) {
    val yearsOffset = floor(page.toFloat() / 12f).toInt()
    val year = dateRange.years.first + yearsOffset
    val month = page - yearsOffset * 12 + 1
}