package com.michaelflisar.composedialogs.dialogs.date.ui

import kotlin.math.floor

class CalendarPageData(
    page: Int,
    dateRange: DatePickerRange
) {
    val yearsOffset = floor(page.toFloat() / 12f).toInt()
    val year = dateRange.years.first + yearsOffset
    val month = page - yearsOffset * 12 + 1
}