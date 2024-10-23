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
import com.michaelflisar.composedialogs.core.Options
import com.michaelflisar.composedialogs.core.SpecialOptions
import com.michaelflisar.composedialogs.core.defaultDialogStyle
import com.michaelflisar.composedialogs.core.style.ComposeDialogStyle
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

 * @param inputPlaceholder a placeholder if the input is empty
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
    inputPlaceholder: String = "",
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    enabled: Boolean = true,
    clearable: Boolean = true,
    prefix: String = "",
    suffix: String = "",
    validator: DialogInputValidator = DialogInputNumber.rememberDefaultValidator(value.value),
    textStyle: TextStyle = LocalTextStyle.current,
    requestFocus: Boolean = false,
    selectionState: DialogInput.SelectionState = DialogInput.SelectionState.Default,
    onValueStateChanged: (valid: Boolean, value: T?) -> Unit = { _, _ -> },
    // Base Dialog - Optional
    title: String? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    specialOptions: SpecialOptions = DialogDefaults.defaultInputDialogSpecialOptions(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    Dialog(state, title, icon, style, buttons, options, specialOptions, onEvent = onEvent) {
        val modifier = when (style.type) {
            ComposeDialogStyle.Type.BottomSheet -> Modifier.fillMaxWidth()
            ComposeDialogStyle.Type.Dialog -> DialogStyleModifier
        }

        val stringInput = rememberSaveable { mutableStateOf(value.value.toString()) }

        LaunchedEffect(validator.isValid()) {
            state.enableButton(DialogButtonType.Positive, validator.isValid())
        }
        LaunchedEffect(stringInput.value) {
            DialogInputNumber.convert(value.value, stringInput.value)?.let {
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
        ) { valid, text ->
            onValueStateChanged(valid, DialogInputNumber.convert(value.value, text))
        }
    }
}

/**
 * convenient function for [DialogInputNumber]
 *
 * @param number the initial value for the input field
 *
 * @return a state holding the current input value
 */
@Composable
fun <T : Number> rememberDialogInputNumber(
    number: T
): MutableState<T> {
    return rememberSaveable { mutableStateOf(number) }
}

object DialogInputNumber {

    /**
     * helper function to convert a text input to a number based on the number type
     *
     * @param value the input dialog state from which the desired number type is derived
     * @param input the input string that should be converted to the number class T
     *
     * @return the input converted to the number class T or null
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Number> convert(value: T, input: String): T? {
        return when (value) {
            is Int -> input.toIntOrNull()
            is Double -> input.toDoubleOrNull()
            is Float -> input.toFloatOrNull()
            is Long -> input.toLongOrNull()
            else -> throw RuntimeException("No string to ${value::class.simpleName} converter defined!")
        } as T?
    }

    /**
     * helper function to convert a text input to a number based on the number type
     *
     * @param input the input string that should be converted to the number class T
     *
     * @return the input converted to the number class T or null
     */
    inline fun <reified T : Number> convert(input: String): T? {
        return when (T::class) {
            Int::class -> input.toIntOrNull()
            Double::class -> input.toDoubleOrNull()
            Float::class -> input.toFloatOrNull()
            Long::class -> input.toLongOrNull()
            else -> throw RuntimeException("No string to ${T::class.simpleName} converter defined!")
        } as T?
    }

    /**
     * default validator that simply ensures, that the input is a valid number of type T
     *
     * @param value the input dialog state from which the desired number type is derived
     *
     * @return a [DialogInputValidator]
     */
    @Composable
    fun <T : Number> rememberDefaultValidator(value: T) = rememberDialogInputValidator(
        validate = {
            val converted = convert(value, it)
            if (converted == null)
                defaultInvalidNumberError(value)
            else DialogInputValidator.Result.Valid
        }
    )

    /**
     * default error state for invalid numbers
     *
     * @param value the input dialog state from which the desired number type is derived
     *
     * @return [DialogInputValidator.Result.Error] state
     */
    fun <T : Number> defaultInvalidNumberError(value: T) =
        DialogInputValidator.Result.Error("Invalid ${value::class.simpleName}!")

    /**
     * default error state for invalid numbers
     *
     * @return [DialogInputValidator.Result.Error] state
     */
    inline fun <reified T : Number> defaultInvalidNumberError() =
        DialogInputValidator.Result.Error("Invalid ${T::class.simpleName}!")
}

