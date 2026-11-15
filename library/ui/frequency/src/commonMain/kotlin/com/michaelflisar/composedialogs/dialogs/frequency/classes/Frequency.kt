package com.michaelflisar.composedialogs.dialogs.frequency.classes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.datetime.DateTimePeriod
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
import kotlin.time.Clock

/**
 * Represents different frequency types for scheduling events.
 */
@Immutable
sealed class Frequency {

    object Saver : androidx.compose.runtime.saveable.Saver<MutableState<Frequency>, String> {
        override fun restore(value: String): MutableState<Frequency> {
            return mutableStateOf(Frequency.deserialize(value))
        }

        override fun SaverScope.save(value: MutableState<Frequency>): String {
            return value.value.serialize()
        }
    }

    fun serialize(): String {
        return when (this) {
            is Daily -> "D|${time}|${interval}"
            is Weekly -> "W|${dayOfWeek}|${time}|${interval}"
            is Monthly -> "M|${dayOfMonth}|${time}|${interval}"
            is Yearly -> "Y|${month}|${dayOfMonth}|${time}|${interval}"
        }
    }

    companion object {

        fun deserialize(value: String): Frequency {
            val parts = value.split("|")
            return when (parts[0]) {
                "D" -> Daily(
                    time = LocalTime.parse(parts[1]),
                    interval = parts[2].toInt()
                )

                "W" -> Weekly(
                    dayOfWeek = DayOfWeek.valueOf(parts[1]),
                    time = LocalTime.parse(parts[2]),
                    interval = parts[3].toInt()
                )

                "M" -> Monthly(
                    dayOfMonth = parts[1].toInt(),
                    time = LocalTime.parse(parts[2]),
                    interval = parts[3].toInt()
                )

                "Y" -> Yearly(
                    month = Month.valueOf(parts[1]),
                    dayOfMonth = parts[2].toInt(),
                    time = LocalTime.parse(parts[3]),
                    interval = parts[4].toInt()
                )

                else -> error("Unknown Frequency type")
            }
        }
    }

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

    // begin-snippet: Frequency::calcNextOccurrence
    /**
     * Calculates the first occurrence strictly after [after], based on the frequency settings.
     *
     * @param after The local date and time after which to calculate the next occurrence.
     * @param timeZone The time zone to consider for the calculation (defaults to the system's current time zone).
     * @return The next occurrence as a LocalDateTime.
     */
    abstract fun calcNextOccurrence(
        after: LocalDateTime,
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ): LocalDateTime
    // end-snippet

    /**
     * Calculates the next occurrence after the current time.
     *
     * @param timeZone The time zone to consider for the calculation (defaults to the system's current time zone).
     * @return The next occurrence as a LocalDateTime.
     */
    fun calcNextOccurrence(
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ): LocalDateTime {
        val now = Clock.System.now().toLocalDateTime(timeZone)
        return calcNextOccurrence(now, timeZone)
    }

    // begin-snippet: Frequency::duration
    /**
     * Calendar period between occurrences. Month and year periods depend on the date they are applied to.
     *
     * For month-end and leap-day frequencies, use [calcNextOccurrence] to build a sequence so the
     * selected day is preserved when a month or year is shorter.
     */
    abstract val duration: DateTimePeriod
    // end-snippet

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

    internal fun isAfter(
        candidate: LocalDateTime,
        after: LocalDateTime,
        timeZone: TimeZone,
    ): Boolean {
        return candidate.toInstant(timeZone) > after.toInstant(timeZone)
    }

    internal fun dateInMonth(year: Int, month: Month, dayOfMonth: Int): LocalDate {
        val firstOfMonth = LocalDate(year, month, 1)
        val lastDay = firstOfMonth.plus(DatePeriod(months = 1))
            .minus(1, DateTimeUnit.DAY)
            .day
        return LocalDate(year, month, minOf(dayOfMonth, lastDay))
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
        override val interval: Int,
    ) : Frequency() {

        init {
            require(interval > 0) { "interval must be positive" }
        }

        override val type = Type.Daily
        override val duration = DateTimePeriod(days = interval)

        override fun calcNextOccurrence(after: LocalDateTime, timeZone: TimeZone): LocalDateTime {
            val today = updateTimeWithTime(after.date)
            return if (isAfter(today, after, timeZone)) {
                today
            } else {
                updateTimeWithTime(after.date.plus(DatePeriod(days = interval)))
            }
        }
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
        override val interval: Int,
    ) : Frequency() {

        init {
            require(interval in 1..(Int.MAX_VALUE / 7)) { "interval must be positive and representable" }
        }

        override val type = Type.Weekly
        override val duration = DateTimePeriod(days = interval * 7)

        override fun calcNextOccurrence(
            after: LocalDateTime,
            timeZone: TimeZone,
        ): LocalDateTime {
            val daysUntilTarget = (dayOfWeek.ordinal - after.dayOfWeek.ordinal + 7) % 7
            val targetDate = after.date.plus(daysUntilTarget, DateTimeUnit.DAY)
            val target = updateTimeWithTime(targetDate)
            return if (isAfter(target, after, timeZone)) {
                target
            } else {
                updateTimeWithTime(targetDate.plus(DatePeriod(days = interval * 7)))
            }
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
        override val interval: Int,
    ) : Frequency() {

        init {
            require(dayOfMonth in 1..31) { "dayOfMonth must be between 1 and 31" }
            require(interval > 0) { "interval must be positive" }
        }

        override val type = Type.Monthly
        override val duration = DateTimePeriod(months = interval)

        override fun calcNextOccurrence(
            after: LocalDateTime,
            timeZone: TimeZone,
        ): LocalDateTime {
            val currentMonth = LocalDate(after.year, after.month, 1)
            var monthOffset = 0
            while (true) {
                val targetMonth = currentMonth.plus(DatePeriod(months = monthOffset))
                val targetDate = dateInMonth(targetMonth.year, targetMonth.month, dayOfMonth)
                val candidate = updateTimeWithTime(targetDate)
                if (isAfter(candidate, after, timeZone)) {
                    return candidate
                }
                monthOffset += interval
            }
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
        override val interval: Int,
    ) : Frequency() {

        init {
            require(dayOfMonth in 1..31) { "dayOfMonth must be between 1 and 31" }
            require(interval > 0) { "interval must be positive" }
        }

        override val type = Type.Yearly
        override val duration = DateTimePeriod(years = interval)

        override fun calcNextOccurrence(
            after: LocalDateTime,
            timeZone: TimeZone,
        ): LocalDateTime {
            var yearOffset = 0
            while (true) {
                val targetYear = after.year + yearOffset
                val targetDate = dateInMonth(targetYear, month, dayOfMonth)
                val candidate = updateTimeWithTime(targetDate)
                if (isAfter(candidate, after, timeZone)) {
                    return candidate
                }
                yearOffset += interval
            }
        }
    }

}

/**
 * convenient function for [Frequency]
 *
 * @param frequency the frequency state of the dialog
 *
 * @return a state holding the current frequency value
 */
@Composable
fun rememberFrequency(
    frequency: Frequency,
): MutableState<Frequency> {
    return rememberSaveable(saver = Frequency.Saver) { mutableStateOf(frequency) }
}