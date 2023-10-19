package com.michaelflisar.composedialogs.dialogs.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import com.michaelflisar.composedialogs.core.Dialog
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.DialogStyle
import com.michaelflisar.composedialogs.core.Options
import com.michaelflisar.composedialogs.dialogs.input.composables.DialogInputTextField

/**
 * Shows a dialog with an input field that only allows numeric characters and validates that the input holds a valid value for the desired data type
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param value the selected number
 * @param valueLabel the optional label of the input field
 * @param invalidNumberErrorText provide a function that returns a custom error message if the user input is not a valid number
 *
 * @param singleLine if true, the input field will only allow a single line
 * @param maxLines the max lines for the input field
 * @param minLines the min lines for the input field
 * @param enabled if true, the input field is enabled
 * @param clearable if true, the input field can be cleared by a trailing clear icon
 * @param prefix the prefix for the input field
 * @param suffix the prefix for the input field
 * @param textStyle the [TextStyle] for the input field
 * @param validator the [DialogInputValidator] for the input field - use [rememberDialogInputValidator]
 * @param requestFocus if true, the input field will request the focus when the dialog si shown (and open the keyboard)
 * @param selectionState if initial selection state ([DialogInput.SelectionState]) of the input field
 * @param onValueStateChanged an optional callback that will be called whenever the value of the input field changes
 */
@Composable
fun <T : Number> DialogInputNumber(
    // Base Dialog - State
    state: DialogState,
    // Custom - Required
    value: MutableState<T>,
    valueLabel: String = "",
    // Custom - Optional
    invalidNumberErrorText: (value: String) -> String = { "Invalid!" },
    inputPlaceholder: String = "",
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    enabled: Boolean = true,
    clearable: Boolean = true,
    prefix: String = "",
    suffix: String = "",
    textStyle: TextStyle = LocalTextStyle.current,
    requestFocus: Boolean = false,
    selectionState: DialogInput.SelectionState = DialogInput.SelectionState.Default,
    onValueStateChanged: (valid: Boolean, value: T?) -> Unit = { _, _ -> },
    // Base Dialog - Optional
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: DialogStyle = DialogDefaults.styleDialog(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    Dialog(state, title, icon, style, buttons, options, onEvent = onEvent) {
        val modifier = when (style) {
            is DialogStyle.BottomSheet -> Modifier.fillMaxWidth()
            is DialogStyle.Dialog -> Modifier
        }

        val stringInput = rememberSaveable { mutableStateOf(value.value.toString()) }

        val converter = when (value.value) {
            is Int -> { value: String -> value.toIntOrNull() }
            is Double -> { value: String -> value.toDoubleOrNull() }
            is Float -> { value: String -> value.toFloatOrNull() }
            is Long -> { value: String -> value.toLongOrNull() }
            else -> throw RuntimeException("No string to T converter defined!")
        } as (String) -> T?

        val validator = rememberDialogInputValidator(
            validate = {
                val converted = converter(it)
                if (converted == null)
                    DialogInputValidator.Result.Error(invalidNumberErrorText(it))
                else DialogInputValidator.Result.Valid
            }
        )

        LaunchedEffect(validator.isValid()) {
            state.enableButton(DialogButtonType.Positive, validator.isValid())
        }
        LaunchedEffect(stringInput.value) {
            converter(stringInput.value)?.let {
                value.value = it
            }
        }

        DialogInputTextField(
            modifier,
            stringInput,
            valueLabel,
            inputPlaceholder,
            singleLine,
            maxLines,
            minLines,
            KeyboardOptions(keyboardType = KeyboardType.Number),
            enabled,
            clearable,
            prefix,
            suffix,
            textStyle,
            validator,
            requestFocus,
            selectionState
        ) { valid, value ->
            onValueStateChanged(valid, converter(value))
        }
    }
}

/**
 * convenient function for [DialogInputNumber]
 *
 * @param value the initial value for the input field
 *
 * @return a state holding the current input value
 */
@Composable
fun <T : Number> rememberDialogInputNumber(
    number: T
): MutableState<T> {
    return rememberSaveable { mutableStateOf(number) }
}