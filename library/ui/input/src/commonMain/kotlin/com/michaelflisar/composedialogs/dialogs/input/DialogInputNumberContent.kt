package com.michaelflisar.composedialogs.dialogs.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import com.michaelflisar.composedialogs.core.ui.ComposeDialogsUI
import com.michaelflisar.composedialogs.dialogs.input.composables.DialogInputTextField

/**
 * Displays a numeric input field and synchronizes valid values to [value].
 *
 * @param onValidationChanged called when validation state changes; dialog wrappers
 * can use it to enable or disable their positive button
 */
@Composable
fun <T : Number> DialogInputNumberContent(
    value: MutableState<T>,
    label: String = "",
    inputPlaceholder: String = "",
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    enabled: Boolean = true,
    clearable: Boolean = true,
    prefix: String = "",
    suffix: String = "",
    validator: DialogInputValidator = DialogInputNumber.rememberDefaultValidator(value.value),
    textStyle: TextStyle = ComposeDialogsUI.provider.textStyle(ComposeDialogsUI.TextRole.Body),
    requestFocus: Boolean = false,
    selectionState: DialogInput.SelectionState = DialogInput.SelectionState.Default,
    onValueStateChanged: (valid: Boolean, value: T?) -> Unit = { _, _ -> },
    onValidationChanged: (valid: Boolean) -> Unit = {},
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    val stringInput = rememberSaveable { mutableStateOf(value.value.toString()) }

    LaunchedEffect(validator.isValid()) {
        onValidationChanged(validator.isValid())
    }
    LaunchedEffect(stringInput.value) {
        DialogInputNumber.convert(value.value, stringInput.value)?.let {
            value.value = it
        }
    }

    DialogInputTextField(
        modifier,
        stringInput,
        label,
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
