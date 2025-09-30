package com.michaelflisar.composedialogs.dialogs.frequency

import com.michaelflisar.composedialogs.dialogs.frequency.classes.Frequency
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class Test {

    @Test
    fun runFrequencyTests() {

        val time = LocalTime(12, 0)

        val frequencyDaily1 = Frequency.Daily(time, 1)
        val frequencyWeekly1 = Frequency.Weekly(DayOfWeek.MONDAY, time, 1)
        val frequencyMonthly1 = Frequency.Monthly(31, time, 1)
        val frequencyYearly1 = Frequency.Yearly(Month.JANUARY, 31, time, 1)

        val frequencyDaily3 = frequencyDaily1.copy(interval = 3)
        val frequencyWeekly3 = frequencyWeekly1.copy(interval = 3)
        val frequencyMonthly3 = frequencyMonthly1.copy(interval = 3)
        val frequencyYearly3 = frequencyYearly1.copy(interval = 3)

        // Friday, January 31, 2025
        val date = LocalDate(2025, Month.JANUARY, 31)

        // February only has 28 days in 2025 => check if this works correctly (with monthly)
        // April only has 30 days => check if this works correctly (with monthly)
        check(frequencyDaily1, date, LocalDateTime(LocalDate(2025, Month.FEBRUARY, 1), time))
        check(frequencyDaily3, date, LocalDateTime(LocalDate(2025, Month.FEBRUARY, 3), time))
        check(frequencyWeekly1, date, LocalDateTime(LocalDate(2025, Month.FEBRUARY, 3), time))
        check(frequencyWeekly3, date, LocalDateTime(LocalDate(2025, Month.FEBRUARY, 17), time))
        check(frequencyMonthly1, date, LocalDateTime(LocalDate(2025, Month.FEBRUARY, 28), time))
        check(frequencyMonthly3, date, LocalDateTime(LocalDate(2025, Month.APRIL, 30), time))
        check(frequencyYearly1, date, LocalDateTime(LocalDate(2026, Month.JANUARY, 31), time))
        check(frequencyYearly3, date, LocalDateTime(LocalDate(2028, Month.JANUARY, 31), time))

        check(frequencyYearly3, date, LocalDateTime(LocalDate(2028, Month.JANUARY, 2), time), shouldFail = true)

        // Test if monthly does not produce [Jan 31, Feb 28, Mar 28, Apr 28, ...] but [Jan 31, Feb 28, Mar 31, Apr 30, ...]
        // which it would if would calculate the next accourrance based on the previous occurrance
        val nextOccurrences = frequencyMonthly1.calcNextOccurrences(date, 5)
        assertEquals(nextOccurrences.size, 5)
        val expected = listOf(
            LocalDateTime(LocalDate(2025, Month.FEBRUARY, 28), time),
            LocalDateTime(LocalDate(2025, Month.MARCH, 31), time),
            LocalDateTime(LocalDate(2025, Month.APRIL, 30), time),
            LocalDateTime(LocalDate(2025, Month.MAY, 31), time),
            LocalDateTime(LocalDate(2025, Month.JUNE, 30), time)
        )
        for (i in expected.indices) {
            assertEquals(expected[i], nextOccurrences[i])
        }

        // Monday, January 1, 2025
        val date1 = LocalDate(2025, Month.JANUARY, 1)
        // Wednesday, December 31, 2025
        val date2 = LocalDate(2025, Month.DECEMBER, 31)

        check(frequencyYearly1, date1, LocalDateTime(LocalDate(2025, Month.JANUARY, 31), time))
        check(frequencyYearly1, date2, LocalDateTime(LocalDate(2026, Month.JANUARY, 31), time))
    }

    fun check(frequency: Frequency, date: LocalDate,result: LocalDateTime, shouldFail: Boolean = false) {
        val calculated = frequency.calcNextOccurrence(date)
        if (shouldFail) {
            assertNotEquals(result, calculated)
        } else {
            assertEquals(result, calculated)
        }
    }
}