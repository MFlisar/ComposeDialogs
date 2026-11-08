package com.michaelflisar.composedialogs.dialogs.time

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.michaelflisar.composedialogs.time.resources.Res
import com.michaelflisar.composedialogs.time.resources.composedialogs_time_am
import com.michaelflisar.composedialogs.time.resources.composedialogs_time_hour
import com.michaelflisar.composedialogs.time.resources.composedialogs_time_invalid_hour
import com.michaelflisar.composedialogs.time.resources.composedialogs_time_invalid_minute
import com.michaelflisar.composedialogs.time.resources.composedialogs_time_minute
import com.michaelflisar.composedialogs.time.resources.composedialogs_time_pm
import org.jetbrains.compose.resources.stringResource

@Stable
object DialogTimeDefaults {
    /**
     * time setup
     *
     * @param is24Hours if true 24h mode is enabled otherwise 12h mode will be used
     * @param minuteStep the allowed step between minute values, from 1 to 60
     * @param hourPickType selects whether hours are entered or selected from a dropdown
     * @param minutePickType selects whether minutes are entered or selected from a dropdown
     */
    @Composable
    fun setup(
        is24Hours: Boolean = is24HourFormat(),
        minuteStep: Int = 1,
        hourPickType: TimePickerInputType = TimePickerInputType.Input,
        minutePickType: TimePickerInputType = TimePickerInputType.Input
    ) = DialogTime.Setup(
        is24Hours = is24Hours,
        minuteStep = minuteStep,
        hourPickType = hourPickType,
        minutePickType = minutePickType
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