package com.michaelflisar.composedialogs.dialogs.datetime.utils

import android.text.format.DateFormat
import com.michaelflisar.composedialogs.dialogs.datetime.classes.SimpleDate
import java.util.Calendar
import java.util.Date

internal object DateUtil {

    @Suppress("DEPRECATION")
    fun getFormattedDate(format: String, year: Int, month: Int, day: Int): String {
        return DateFormat.format(format, Date(year - 1900, month - 1, day)).toString()
    }

    @Suppress("DEPRECATION")
    fun getMonthAndYearInfo(year: Int, month: Int): String {
        return DateFormat.format("MMM yyyy", Date(year - 1900, month - 1, 1)).toString()
    }


    fun getWeekDayInfo(day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, day)
        return DateFormat.format("EEE", calendar.time).toString()
    }

    fun today(): SimpleDate {
        val calendar = Calendar.getInstance()
        return SimpleDate(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.DAY_OF_WEEK)
        )
    }

    fun createSimpleDate(calendar: Calendar): SimpleDate {
        return SimpleDate(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.DAY_OF_WEEK)
        )
    }

    fun firstDateOfMonth(year: Int, month: Int): SimpleDate {
        val cal = Calendar.getInstance().apply {
            timeInMillis = 0
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        return createSimpleDate(cal)
    }

    fun lastDateOfMonth(year: Int, month: Int): SimpleDate {
        val cal = Calendar.getInstance().apply {
            timeInMillis = 0
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        cal.add(Calendar.MONTH, 1)
        cal.add(Calendar.DAY_OF_YEAR, -1)
        return createSimpleDate(cal)
    }

    fun getSortedWeekDays(firstDayOfWeek: Int): List<Int> {
        val weekdays = mutableListOf(
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY,
            Calendar.SATURDAY,
            Calendar.SUNDAY,
        )
        while (weekdays.first() != firstDayOfWeek) {
            val first = weekdays.removeFirst()
            weekdays.add(first)
        }
        return weekdays
    }
}