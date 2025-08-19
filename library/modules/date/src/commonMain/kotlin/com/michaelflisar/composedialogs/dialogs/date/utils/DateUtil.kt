package com.michaelflisar.composedialogs.dialogs.date.utils

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

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

    @OptIn(ExperimentalTime::class)
    fun now(): LocalDateTime {
        val now = Clock.System.now()
        return now.toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun today() = now().date

    fun firstDateOfMonth(year: Int, month: Int): LocalDate {
        return LocalDate(year, month, 1)
    }

    fun lastDateOfMonth(year: Int, month: Int): LocalDate {
        val firstDateOfMonth = firstDateOfMonth(year, month)
        var tmp = firstDateOfMonth.plus(31, DateTimeUnit.DAY)
        while (tmp.month != firstDateOfMonth.month) {
            tmp = tmp.minus(1, DateTimeUnit.DAY)
        }
        return tmp
    }

    fun getSortedWeekDays(firstDayOfWeek: DayOfWeek): List<DayOfWeek> {
        val weekdays = DayOfWeeks.toMutableList()
        while (weekdays.first() != firstDayOfWeek) {
            val first = weekdays.removeAt(0)
            weekdays.add(first)
        }
        return weekdays
    }
}