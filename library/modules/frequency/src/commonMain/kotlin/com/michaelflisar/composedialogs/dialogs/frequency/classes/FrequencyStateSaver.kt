package com.michaelflisar.composedialogs.dialogs.frequency.classes

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month

object FrequencyStateSaver : Saver<MutableState<Frequency>, String> {
    override fun restore(value: String): MutableState<Frequency> {
        val parts = value.split("|")
        return mutableStateOf(
            when (parts[0]) {
                "D" -> Frequency.Daily(
                    time = LocalTime.parse(parts[1]),
                    interval = parts[2].toInt()
                )
                "W" -> Frequency.Weekly(
                    dayOfWeek = DayOfWeek.valueOf(parts[1]),
                    time = LocalTime.parse(parts[2]),
                    interval = parts[3].toInt()
                )
                "M" -> Frequency.Monthly(
                    dayOfMonth = parts[1].toInt(),
                    time = LocalTime.parse(parts[2]),
                    interval = parts[3].toInt()
                )
                "Y" -> Frequency.Yearly(
                    month = Month.valueOf(parts[1]),
                    dayOfMonth = parts[2].toInt(),
                    time = LocalTime.parse(parts[3]),
                    interval = parts[4].toInt()
                )
                else -> error("Unknown Frequency type")
            }
        )
    }

    override fun SaverScope.save(value: MutableState<Frequency>): String {
        return when (val freq = value.value) {
            is Frequency.Daily -> "D|${freq.time}|${freq.interval}"
            is Frequency.Weekly -> "W|${freq.dayOfWeek}|${freq.time}|${freq.interval}"
            is Frequency.Monthly -> "M|${freq.dayOfMonth}|${freq.time}|${freq.interval}"
            is Frequency.Yearly -> "Y|${freq.month}|${freq.dayOfMonth}|${freq.time}|${freq.interval}"
        }
    }
}
