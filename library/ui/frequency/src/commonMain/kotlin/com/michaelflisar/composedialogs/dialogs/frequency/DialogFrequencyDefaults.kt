package com.michaelflisar.composedialogs.dialogs.frequency

import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.dialogs.frequency.classes.Frequency
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month

object DialogFrequencyDefaults {

    @Composable
    fun texts(
        interval: String = "Interval",
        type: String = "Type",
        every: String = "Every",
        monthOfYear: String = "Month of Year",
        dayOfMonth: String = "Day of Month",
        dayOfWeek: String = "Day of Week",
        time: String = "Time",
        nameOfType: (day: Frequency.Type, interval: Int) -> String = { type, interval ->
            val singular = when (type) {
                Frequency.Type.Daily -> "day"
                Frequency.Type.Weekly -> "week"
                Frequency.Type.Monthly -> "month"
                Frequency.Type.Yearly -> "year"
            }
            val plural = singular + "s"
            if (interval == 1) singular else plural
        },
        nameOfDayOfWeek: (day: DayOfWeek) -> String = { it.name.lowercase().replaceFirstChar { it.uppercase() } },
        nameOfMonth: (month: Month) -> String = { it.name.lowercase().replaceFirstChar { it.uppercase() }  },
    ): DialogFrequency.Texts {
        return DialogFrequency.Texts(
            interval = interval,
            type = type,
            every = every,
            monthOfYear = monthOfYear,
            dayOfMonth = dayOfMonth,
            dayOfWeek = dayOfWeek,
            time = time,
            nameOfType = nameOfType,
            nameOfDayOfWeek = nameOfDayOfWeek,
            nameOfMonth = nameOfMonth
        )
    }
}