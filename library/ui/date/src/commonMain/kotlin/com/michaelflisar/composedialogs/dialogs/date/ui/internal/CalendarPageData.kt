package com.michaelflisar.composedialogs.dialogs.date.ui.internal

import androidx.compose.runtime.Immutable
import kotlin.math.floor

@Immutable
internal class CalendarPageData(
    page: Int,
    years: IntRange
) {
    val yearsOffset = floor(page.toFloat() / 12f).toInt()
    val year = years.first + yearsOffset
    val month = page - yearsOffset * 12 + 1
}