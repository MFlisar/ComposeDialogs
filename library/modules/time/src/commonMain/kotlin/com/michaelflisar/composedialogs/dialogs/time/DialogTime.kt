package com.michaelflisar.composedialogs.dialogs.time

import com.michaelflisar.composedialogs.dialogs.time.internal.Material3TimeUIProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import com.michaelflisar.composedialogs.core.*
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.dialogs.time.utils.TimeUtil
import kotlinx.datetime.LocalTime

// begin-snippet: DialogTime::constructor
/**
 * Shows a dialog with a time input
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param time the selected time
 * @param setup the [TimePickerSetup] - use [TimePickerDefaults.setup] to provide your own data
 * @param texts localized labels for the hour, minute, and AM/PM controls
 * @param uiProvider the UI implementation used to render the time controls
 */
@Composable
fun DialogTime(
    state: BaseDialogState,
    // Custom - Required
    time: MutableState<LocalTime>,
    // Custom - Optional
    setup: TimePickerSetup = TimePickerDefaults.setup(),
    // Base Dialog - Optional
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: DialogOptions = DialogDefaults.options(),
    onEvent: (event: DialogEvent) -> Unit = {},
    texts: TimePickerTexts = TimePickerDefaults.texts(),
    uiProvider: TimeUIProvider = Material3TimeUIProvider
)
// end-snippet
{
    val positiveButtonEnabledBeforeValidation = remember(state) {
        mutableStateOf<Boolean?>(null)
    }
    val onValidationChanged: (Boolean) -> Unit = remember(state) {
        { isValid ->
            if (isValid) {
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
        TimePicker(
            time = time,
            is24Hours = setup.is24Hours,
            minuteStep = setup.minuteStep,
            hourPickType = setup.hourPickType,
            minutePickType = setup.minutePickType,
            texts = texts,
            uiProvider = uiProvider,
            onValidationChanged = onValidationChanged
        )
    }
}

@Stable
object DialogTime {

    val LocalTimeSaver = Saver<MutableState<LocalTime>, Int>(
        save = { it.value.toSecondOfDay() },
        restore = { mutableStateOf(LocalTime.fromSecondOfDay(it)) }
    )
}

/**
 * convenient function for [DialogTime]
 *
 * @param time the initial time
 *
 * @return a state holding the current time values
 */
@Composable
fun rememberDialogTime(
    time: LocalTime = TimeUtil.now().time
): MutableState<LocalTime> {
    return rememberSaveable(
        saver = DialogTime.LocalTimeSaver
    ) { mutableStateOf(time) }
}