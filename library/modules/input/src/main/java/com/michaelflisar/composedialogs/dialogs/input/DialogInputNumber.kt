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
import com.michaelflisar.composedialogs.core.DialogIcon
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.DialogStyle
import com.michaelflisar.composedialogs.core.DialogTitleStyle
import com.michaelflisar.composedialogs.core.Options

@Composable
fun <T : Number> DialogInputNumber(
    // Base Dialog - State
    state: DialogState,
    // Custom - Required
    value: MutableState<T>,
    valueLabel: String,
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
    initialState: DialogInput.InitialState = DialogInput.InitialState.Default,
    onValueStateChanged: (valid: Boolean, value: T?) -> Unit = { _, _ -> },
    // Base Dialog - Optional
    title: String = "",
    titleStyle: DialogTitleStyle = DialogDefaults.titleStyle(),
    icon: DialogIcon? = null,
    style: DialogStyle = DialogDefaults.styleDialog(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    Dialog(state, title, titleStyle, icon, style, buttons, options, onEvent = onEvent) {
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
            println("stringInput = ${stringInput.value}")
            converter(stringInput.value)?.let {
                value.value = it
            }
        }

        DialogInput.InputText(
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
            initialState
        ) { valid, value ->
            onValueStateChanged(valid, converter(value))
        }
    }
}

@Composable
fun <T : Number> rememberDialogNumber(
    value: T
): MutableState<T> {
    return rememberSaveable { mutableStateOf(value) }
}