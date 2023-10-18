package com.michaelflisar.composedialogs.dialogs.datetime.utils

import android.text.format.DateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

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

    private val CalendarDays = listOf(
        Calendar.MONDAY,
        Calendar.TUESDAY,
        Calendar.WEDNESDAY,
        Calendar.THURSDAY,
        Calendar.FRIDAY,
        Calendar.SATURDAY,
        Calendar.SUNDAY
    )

    @Suppress("DEPRECATION")
    fun getFormattedDate(format: String, year: Int, month: Int, day: Int): String {
        return DateFormat.format(format, Date(year - 1900, month - 1, day)).toString()
    }

    @Suppress("DEPRECATION")
    fun getMonthAndYearInfo(year: Int, month: Int): String {
        return DateFormat.format("MMM yyyy", Date(year - 1900, month - 1, 1)).toString()
    }


    fun getWeekDayInfo(day: DayOfWeek): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, CalendarDays[DayOfWeeks.indexOf(day)])
        return DateFormat.format("EEE", calendar.time).toString()
    }

    fun today(): LocalDate {
        val calendar = Calendar.getInstance()
        return createLocalDate(calendar)
    }

    fun createLocalDate(calendar: Calendar): LocalDate {
        return LocalDate.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun firstDateOfMonth(year: Int, month: Int): LocalDate {
        val cal = Calendar.getInstance().apply {
            timeInMillis = 0
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        return createLocalDate(cal)
    }

    fun lastDateOfMonth(year: Int, month: Int): LocalDate {
        val cal = Calendar.getInstance().apply {
            timeInMillis = 0
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        cal.add(Calendar.MONTH, 1)
        cal.add(Calendar.DAY_OF_YEAR, -1)
        return createLocalDate(cal)
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