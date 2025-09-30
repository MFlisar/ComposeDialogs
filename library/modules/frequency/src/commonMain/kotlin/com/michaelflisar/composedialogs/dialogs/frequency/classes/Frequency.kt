package com.michaelflisar.composedialogs.dialogs.frequency.classes

import androidx.compose.runtime.Immutable
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

/**
 * Represents different frequency types for scheduling events.
 */
@Immutable
sealed class Frequency {

    /**
     * Represents the types of frequency intervals available for scheduling events.
     */
    enum class Type {
        Daily,
        Weekly,
        Monthly,
        Yearly
    }

    abstract val type: Type
    abstract val time: LocalTime
    abstract val interval: Int

    /* --8<-- [start: calcNextOccurrence] */
    /**
     * Calculates the next occurrence of the event based on the frequency settings.
     *
     * @param from The starting point to calculate the next occurrence from.
     * @param timeZone The time zone to consider for the calculation (defaults to the system's current time zone).
     * @param offset An optional offset to adjust the calculation (default is 0).
     * @return The next occurrence as a LocalDateTime.
     */
    @OptIn(ExperimentalTime::class)
    abstract fun calcNextOccurrence(from: LocalDate, timeZone: TimeZone = TimeZone.currentSystemDefault(), offset: Int = 0): LocalDateTime
    /* --8<-- [end: calcNextOccurrence] */

    /* --8<-- [start: calcNextOccurrence] */
    /**
     * Calculates the next [count] occurrences of the event based on the frequency settings.
     *
     * @param from The starting point to calculate the next occurrences from.
     * @param offset An optional offset to adjust the calculation (default is 0).
     * @param count The number of occurrences to calculate.
     * @param timeZone The time zone to consider for the calculation (defaults to the system's current time zone).
     * @return A list of the next occurrences as LocalDateTime objects.
     */
    @OptIn(ExperimentalTime::class)
    fun calcNextOccurrences(from: LocalDate, count: Int, offset: Int = 0, timeZone: TimeZone = TimeZone.currentSystemDefault()): List<LocalDateTime>
    /* --8<-- [end: calcNextOccurrence] */
    {
        val occurrences = mutableListOf<LocalDateTime>()
        while (occurrences.size < count) {
            val next = calcNextOccurrence(from, timeZone, offset + occurrences.size)
            occurrences.add(next)
        }
        return occurrences
    }

    internal fun updateTimeWithTime(date: LocalDate): LocalDateTime {
        return LocalDateTime(
            date.year,
            date.month,
            date.day,
            time.hour,
            time.minute,
            time.second,
            time.nanosecond
        )
    }

    @OptIn(ExperimentalTime::class)
    internal fun calcDate(from: LocalDate, timeZone: TimeZone, unit: DateTimeUnit, offset: Int): LocalDateTime {
        val dateTime = updateTimeWithTime(from)
        val instant = dateTime.toInstant(timeZone)
        val instantNext = instant.plus(interval * (1 + offset), unit, timeZone)
        val dateTimeNext = instantNext.toLocalDateTime(timeZone)
        return dateTimeNext
    }

    /**
     * Daily frequency at a specific time.
     *
     * @property time The time of day for the event.
     * @property interval The interval in days between occurrences.
     */
    @Immutable
    data class Daily(
        override val time: LocalTime,
        override val interval: Int
    ) : Frequency() {

        override val type = Type.Daily

        @OptIn(ExperimentalTime::class)
        override fun calcNextOccurrence(from: LocalDate, timeZone: TimeZone, offset: Int) = calcDate(from, timeZone, DateTimeUnit.DAY, offset)
    }

    /**
     * Weekly frequency on a specific day and time.
     *
     * @property dayOfWeek The day of the week for the event.
     * @property time The time of day for the event.
     * @property interval The interval in weeks between occurrences.
     */
    @Immutable
    data class Weekly(
        val dayOfWeek: DayOfWeek,
        override val time: LocalTime,
        override val interval: Int
    ) : Frequency() {

        override val type = Type.Weekly

        @OptIn(ExperimentalTime::class)
        override fun calcNextOccurrence(from: LocalDate, timeZone: TimeZone, offset: Int) : LocalDateTime {
            var from = from
            while (from.dayOfWeek != dayOfWeek) {
                from = from.minus(1, DateTimeUnit.DAY)
            }
            return calcDate(from, timeZone, DateTimeUnit.WEEK, offset)
        }
    }

    /**
     * Monthly frequency on a specific day and time.
     *
     * @property dayOfMonth The day of the month for the event.
     * @property time The time of day for the event.
     * @property interval The interval in months between occurrences.
     */
    @Immutable
    data class Monthly(
        val dayOfMonth: Int,
        override val time: LocalTime,
        override val interval: Int
    ) : Frequency() {

        override val type = Type.Monthly

        @OptIn(ExperimentalTime::class)
        override fun calcNextOccurrence(from: LocalDate, timeZone: TimeZone, offset: Int) : LocalDateTime {
            var from = from
            while (from.day != dayOfMonth) {
                from = from.minus(1, DateTimeUnit.DAY)
            }
            return calcDate(from, timeZone, DateTimeUnit.MONTH, offset)
        }
    }

    /**
     * Yearly frequency on a specific month, day, and time.
     *
     * @property month The month of the year for the event.
     * @property dayOfMonth The day of the month for the event.
     * @property time The time of day for the event.
     * @property interval The interval in years between occurrences.
     */
    @Immutable
    data class Yearly(
        val month: Month,
        val dayOfMonth: Int,
        override val time: LocalTime,
        override val interval: Int
    ) : Frequency() {

        override val type = Type.Yearly

        @OptIn(ExperimentalTime::class)
        override fun calcNextOccurrence(from: LocalDate, timeZone: TimeZone, offset: Int) : LocalDateTime {
            var from = from
            val targetInSameYear = LocalDate(from.year, month, dayOfMonth)
            if (from < targetInSameYear) {
                from = targetInSameYear.minus(1, DateTimeUnit.YEAR)
            } else if (from >= targetInSameYear) {
                from = targetInSameYear
            }
            return calcDate(from, timeZone, DateTimeUnit.YEAR, offset)
        }
    }

}