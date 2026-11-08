package com.michaelflisar.composedialogs.dialogs.frequency

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import com.michaelflisar.composedialogs.core.BaseDialogState
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.Dialog
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogOptions
import com.michaelflisar.composedialogs.core.defaultDialogStyle
import com.michaelflisar.composedialogs.dialogs.frequency.classes.Frequency
import com.michaelflisar.composedialogs.dialogs.frequency.classes.FrequencyStateSaver
import com.michaelflisar.composedialogs.dialogs.time.TimePickerDefaults
import com.michaelflisar.composedialogs.dialogs.time.TimePickerSetup
import com.michaelflisar.composedialogs.dialogs.time.rememberDialogTime
import com.michaelflisar.composedialogs.dialogs.frequency.internal.Material3FrequencyUIProvider
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month

// begin-snippet: DialogFrequency::constructor
/**
 * Shows a frequency dialog
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param frequency the frequency state of the dialog
 * @param texts the texts ([FrequencyPickerTexts]) that are used inside this dialog - use [FrequencyPickerDefaults.texts] to provide your own data
 * @param supportedTypes the supported frequency types - default is all types
 * @param firstDayOffset the first day of the week - default is [DayOfWeek.MONDAY]
 * @param intervalInputType how the interval is entered
 * @param timeSetup the setup used by the embedded time picker
 */
@Composable
fun DialogFrequency(
    state: BaseDialogState,
    // Custom - Required
    frequency: MutableState<Frequency>,
    // Custom - Optional
    texts: FrequencyPickerTexts = FrequencyPickerDefaults.texts(),
    supportedTypes: List<Frequency.Type> = Frequency.Type.entries,
    firstDayOffset: DayOfWeek = DayOfWeek.MONDAY,
    // Base Dialog - Optional
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: DialogOptions = DialogDefaults.options(),
    onEvent: (event: DialogEvent) -> Unit = {},
    intervalInputType: FrequencyIntervalInputType = FrequencyIntervalInputType.Input,
    timeSetup: TimePickerSetup = TimePickerDefaults.setup()
)
// end-snippet
{
    val positiveButtonEnabledBeforeValidation = remember(state) {
        mutableStateOf<Boolean?>(null)
    }
    if (state.visible) {

        val selectedType = rememberSaveable {
            mutableStateOf(frequency.value.type.toPickerType())
        }
        val selectedTime = rememberDialogTime(frequency.value.time)
        val selectedDayOfWeek = rememberSaveable {
            mutableStateOf(
                when (val f = frequency.value) {
                    is Frequency.Weekly -> f.dayOfWeek
                    else -> DayOfWeek.MONDAY
                }
            )
        }
        val selectedDayOfMonth = rememberSaveable {
            mutableStateOf(
                when (val f = frequency.value) {
                    is Frequency.Monthly -> f.dayOfMonth
                    is Frequency.Yearly -> f.dayOfMonth
                    else -> 1
                }
            )
        }
        val selectedMonthOfYear = rememberSaveable {
            mutableStateOf(
                when (val f = frequency.value) {
                    is Frequency.Yearly -> f.month
                    else -> Month.JANUARY
                }
            )
        }
        val selectedInterval = rememberSaveable { mutableStateOf<Int?>(frequency.value.interval) }
        val intervalIsValid = selectedInterval.value?.let { it > 0 } == true

        LaunchedEffect(intervalIsValid) {
            if (intervalIsValid) {
                positiveButtonEnabledBeforeValidation.value?.let { wasEnabled ->
                    state.enableButton(DialogButtonType.Positive, wasEnabled)
                    positiveButtonEnabledBeforeValidation.value = null
                }
            } else if (positiveButtonEnabledBeforeValidation.value == null) {
                positiveButtonEnabledBeforeValidation.value =
                    state.isButtonEnabled(DialogButtonType.Positive)
                state.enableButton(DialogButtonType.Positive, false)
            }
        }

        // update frequency if something changes
        LaunchedEffect(
            selectedType.value,
            selectedTime.value,
            selectedDayOfWeek.value,
            selectedDayOfMonth.value,
            selectedMonthOfYear.value,
            selectedInterval.value
        ) {
            val interval = selectedInterval.value
            if (interval == null || interval <= 0) {
                return@LaunchedEffect
            }
            frequency.value = when (selectedType.value) {
                FrequencyPickerType.Daily -> Frequency.Daily(
                    interval = interval,
                    time = selectedTime.value
                )

                FrequencyPickerType.Weekly -> Frequency.Weekly(
                    interval = interval,
                    dayOfWeek = selectedDayOfWeek.value,
                    time = selectedTime.value
                )

                FrequencyPickerType.Monthly -> Frequency.Monthly(
                    interval = interval,
                    dayOfMonth = selectedDayOfMonth.value,
                    time = selectedTime.value
                )

                FrequencyPickerType.Yearly -> Frequency.Yearly(
                    interval = interval,
                    month = selectedMonthOfYear.value,
                    dayOfMonth = selectedDayOfMonth.value,
                    time = selectedTime.value
                )
            }
        }

        Dialog(
            state = state,
            title = title,
            icon = icon,
            style = style,
            buttons = buttons,
            options = options,
            onEvent = { event ->
                if (event.dismissed) {
                    positiveButtonEnabledBeforeValidation.value?.let { wasEnabled ->
                        state.enableButton(DialogButtonType.Positive, wasEnabled)
                        positiveButtonEnabledBeforeValidation.value = null
                    }
                }
                onEvent(event)
            }
        ) {
            FrequencyPicker(
                uiProvider = Material3FrequencyUIProvider,
                texts = texts,
                supportedTypes = supportedTypes.map { it.toPickerType() },
                firstDayOffset = firstDayOffset,
                selectedType = selectedType,
                selectedTime = selectedTime,
                selectedDayOfWeek = selectedDayOfWeek,
                selectedDayOfMonth = selectedDayOfMonth,
                selectedMonthOfYear = selectedMonthOfYear,
                selectedInterval = selectedInterval,
                intervalInputType = intervalInputType,
                timeSetup = timeSetup
            )
        }
    }
}

private fun Frequency.Type.toPickerType() = when (this) {
    Frequency.Type.Daily -> FrequencyPickerType.Daily
    Frequency.Type.Weekly -> FrequencyPickerType.Weekly
    Frequency.Type.Monthly -> FrequencyPickerType.Monthly
    Frequency.Type.Yearly -> FrequencyPickerType.Yearly
}

/**
 * convenient function for [DialogFrequency]
 *
 * @param frequency the frequency state of the dialog
 *
 * @return a state holding the current frequency value
 */
@Composable
fun rememberDialogFrequency(
    frequency: Frequency
): MutableState<Frequency> {
    return rememberSaveable(saver = FrequencyStateSaver) { mutableStateOf(frequency) }
}
