package com.michaelflisar.composedialogs.dialogs.frequency

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.michaelflisar.composedialogs.dialogs.frequency.internal.Material3FrequencyUIProvider
import com.michaelflisar.composedialogs.dialogs.time.TimePickerDefaults
import com.michaelflisar.composedialogs.dialogs.time.TimePickerSetup
import kotlinx.datetime.DayOfWeek

// begin-snippet: DialogFrequency::constructor
/**
 * Shows a frequency dialog
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param frequency the frequency state of the dialog - use [com.michaelflisar.composedialogs.dialogs.frequency.classes.rememberFrequency] to create a state for this parameter
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
    timeSetup: TimePickerSetup = TimePickerDefaults.setup(),
)
// end-snippet
{
    val positiveButtonEnabledBeforeValidation = remember(state) {
        mutableStateOf<Boolean?>(null)
    }
    if (state.visible) {
        val intervalIsValid = remember(state) {
            mutableStateOf(frequency.value.interval > 0)
        }

        LaunchedEffect(intervalIsValid.value) {
            if (intervalIsValid.value) {
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
                supportedTypes = supportedTypes,
                firstDayOffset = firstDayOffset,
                frequency = frequency,
                intervalInputType = intervalInputType,
                timeSetup = timeSetup,
                onIntervalValidityChanged = { intervalIsValid.value = it }
            )
        }
    }
}