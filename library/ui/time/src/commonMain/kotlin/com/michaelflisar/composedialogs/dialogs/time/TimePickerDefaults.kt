package com.michaelflisar.composedialogs.dialogs.time

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.michaelflisar.composedialogs.time.resources.Res
import com.michaelflisar.composedialogs.time.resources.composedialogs_time_am
import com.michaelflisar.composedialogs.time.resources.composedialogs_time_hour
import com.michaelflisar.composedialogs.time.resources.composedialogs_time_invalid_hour
import com.michaelflisar.composedialogs.time.resources.composedialogs_time_invalid_minute
import com.michaelflisar.composedialogs.time.resources.composedialogs_time_minute
import com.michaelflisar.composedialogs.time.resources.composedialogs_time_pm
import org.jetbrains.compose.resources.stringResource

@Immutable
class TimePickerConfig internal constructor(
    val is24Hours: Boolean,
    val minuteStep: Int,
    val inputType: TimePickerInputType
) {
    init {
        require(minuteStep in 1..60) { "minuteStep must be between 1 and 60" }
    }
}

@Immutable
class TimePickerTexts internal constructor(
    val hour: String,
    val minute: String,
    val am: String,
    val pm: String,
    val invalidHour: String,
    val invalidMinute: String
)

@Stable
object TimePickerDefaults {
    @Composable
    fun config(
        is24Hours: Boolean = is24HourFormat(),
        minuteStep: Int = 1,
        inputType: TimePickerInputType = TimePickerInputType.Input
    ) = TimePickerConfig(
        is24Hours = is24Hours,
        minuteStep = minuteStep,
        inputType = inputType
    )

    @Composable
    fun texts(
        hour: String = stringResource(Res.string.composedialogs_time_hour),
        minute: String = stringResource(Res.string.composedialogs_time_minute),
        am: String = stringResource(Res.string.composedialogs_time_am),
        pm: String = stringResource(Res.string.composedialogs_time_pm),
        invalidHour: String = stringResource(Res.string.composedialogs_time_invalid_hour),
        invalidMinute: String = stringResource(Res.string.composedialogs_time_invalid_minute)
    ) = TimePickerTexts(
        hour = hour,
        minute = minute,
        am = am,
        pm = pm,
        invalidHour = invalidHour,
        invalidMinute = invalidMinute
    )
}
