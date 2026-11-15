package com.michaelflisar.composedialogs.dialogs.frequency.classes

import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.Clock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Test {

    @Test
    fun dailyOccurrenceUsesTodayWhenItsTimeIsStillAhead() {
        val frequency = Frequency.Daily(LocalTime(23, 5), interval = 1)

        assertEquals(
            LocalDateTime(2025, Month.JANUARY, 31, 23, 5),
            frequency.calcNextOccurrence(LocalDateTime(2025, Month.JANUARY, 31, 23, 0))
        )
        assertEquals(
            LocalDateTime(2025, Month.FEBRUARY, 1, 23, 5),
            frequency.calcNextOccurrence(LocalDateTime(2025, Month.JANUARY, 31, 23, 6))
        )
    }

    @Test
    fun noArgumentOverloadCalculatesAfterNow() {
        val timeZone = TimeZone.currentSystemDefault()
        val next = Frequency.Daily(LocalTime(23, 59, 59), interval = 1)
            .calcNextOccurrence()

        assertTrue(next.toInstant(timeZone) > Clock.System.now())
    }

    @Test
    fun weeklyOccurrenceUsesNextMatchingDay() {
        val frequency = Frequency.Weekly(
            dayOfWeek = DayOfWeek.MONDAY,
            time = LocalTime(23, 5),
            interval = 1
        )

        assertEquals(
            LocalDateTime(2025, Month.FEBRUARY, 3, 23, 5),
            frequency.calcNextOccurrence(LocalDateTime(2025, Month.JANUARY, 31, 23, 6))
        )
        assertEquals(
            LocalDateTime(2025, Month.FEBRUARY, 3, 23, 5),
            frequency.calcNextOccurrence(LocalDateTime(2025, Month.FEBRUARY, 3, 23, 0))
        )
    }

    @Test
    fun monthlyDayThirtyOneDoesNotReturnAnOccurrenceBeforeAfter() {
        val frequency = Frequency.Monthly(
            dayOfMonth = 31,
            time = LocalTime(12, 0),
            interval = 1
        )

        assertEquals(
            LocalDateTime(2025, Month.MARCH, 31, 12, 0),
            frequency.calcNextOccurrence(LocalDateTime(2025, Month.MARCH, 1, 12, 0))
        )
        assertEquals(
            LocalDateTime(2025, Month.MARCH, 31, 12, 0),
            frequency.calcNextOccurrence(LocalDateTime(2025, Month.MARCH, 30, 12, 0))
        )

        var after = LocalDateTime(2025, Month.JANUARY, 31, 12, 0)
        val expected = listOf(
            LocalDateTime(2025, Month.FEBRUARY, 28, 12, 0),
            LocalDateTime(2025, Month.MARCH, 31, 12, 0),
            LocalDateTime(2025, Month.APRIL, 30, 12, 0),
            LocalDateTime(2025, Month.MAY, 31, 12, 0),
            LocalDateTime(2025, Month.JUNE, 30, 12, 0)
        )
        val actual = expected.map {
            frequency.calcNextOccurrence(after).also { next -> after = next }
        }
        assertEquals(expected, actual)
    }

    @Test
    fun yearlyFebruaryTwentyNinthClampsInNonLeapYears() {
        val frequency = Frequency.Yearly(
            month = Month.FEBRUARY,
            dayOfMonth = 29,
            time = LocalTime(12, 0),
            interval = 1
        )

        assertEquals(
            LocalDateTime(2025, Month.FEBRUARY, 28, 12, 0),
            frequency.calcNextOccurrence(LocalDateTime(2025, Month.JANUARY, 1, 12, 0))
        )
        assertEquals(
            LocalDateTime(2026, Month.FEBRUARY, 28, 12, 0),
            frequency.calcNextOccurrence(LocalDateTime(2025, Month.FEBRUARY, 28, 12, 0))
        )
    }

    @Test
    fun frequenciesExposeCalendarDurations() {
        assertEquals(DateTimePeriod(days = 3), Frequency.Daily(LocalTime(12, 0), 3).duration)
        assertEquals(DateTimePeriod(days = 14), Frequency.Weekly(DayOfWeek.MONDAY, LocalTime(12, 0), 2).duration)
        assertEquals(DateTimePeriod(months = 2), Frequency.Monthly(31, LocalTime(12, 0), 2).duration)
        assertEquals(DateTimePeriod(years = 4), Frequency.Yearly(Month.JANUARY, 31, LocalTime(12, 0), 4).duration)
    }

    @Test
    fun intervalsApplyAfterTheCurrentOccurrenceHasPassed() {
        assertEquals(
            LocalDateTime(2025, Month.FEBRUARY, 3, 12, 0),
            Frequency.Daily(LocalTime(12, 0), 3)
                .calcNextOccurrence(LocalDateTime(2025, Month.JANUARY, 31, 12, 1))
        )
        assertEquals(
            LocalDateTime(2025, Month.JANUARY, 20, 12, 0),
            Frequency.Weekly(DayOfWeek.MONDAY, LocalTime(12, 0), 2)
                .calcNextOccurrence(LocalDateTime(2025, Month.JANUARY, 6, 12, 1))
        )
        assertEquals(
            LocalDateTime(2025, Month.MARCH, 31, 12, 0),
            Frequency.Monthly(31, LocalTime(12, 0), 2)
                .calcNextOccurrence(LocalDateTime(2025, Month.JANUARY, 31, 12, 1))
        )
        assertEquals(
            LocalDateTime(2028, Month.JANUARY, 31, 12, 0),
            Frequency.Yearly(Month.JANUARY, 31, LocalTime(12, 0), 3)
                .calcNextOccurrence(LocalDateTime(2025, Month.JANUARY, 31, 12, 1))
        )
    }
}
