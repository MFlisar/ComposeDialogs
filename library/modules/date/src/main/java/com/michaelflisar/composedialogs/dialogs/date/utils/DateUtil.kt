package com.michaelflisar.composedialogs.dialogs.date.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

internal object DateUtil {

    private val DayOfWeeks = listOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY,
        DayOfWeek.SUNDAY
    )

    fun today(): LocalDate {
        return LocalDate.now()
    }

    fun firstDateOfMonth(year: Int, month: Int): LocalDate {
        return LocalDate.of(year, month, 1)
    }

    fun lastDateOfMonth(year: Int, month: Int): LocalDate {
        val firstDateOfMonth = firstDateOfMonth(year, month)
        return firstDateOfMonth.with(TemporalAdjusters.lastDayOfMonth())
    }

    fun getSortedWeekDays(firstDayOfWeek: DayOfWeek): List<DayOfWeek> {
        val weekdays = DayOfWeeks.toMutableList()
        while (weekdays.first() != firstDayOfWeek) {
            val first = weekdays.removeFirst()
            weekdays.add(first)
        }
        return weekdays
    }
}