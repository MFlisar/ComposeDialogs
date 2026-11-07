package com.michaelflisar.composedialogs.dialogs.input

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.michaelflisar.composedialogs.core.BaseDialogState
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.Dialog
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogOptions
import com.michaelflisar.composedialogs.core.defaultDialogStyle

@Composable
fun <T : Number> DialogInputNumber(
    state: BaseDialogState,
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
    textStyle: TextStyle = DialogInputNumberDefaults.textStyle(),
    requestFocus: Boolean = false,
    selectionState: DialogInput.SelectionState = DialogInput.SelectionState.Default,
    onValueStateChanged: (valid: Boolean, value: T?) -> Unit = { _, _ -> },
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: DialogOptions = DialogDefaults.options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    Dialog(state, title, icon, style, buttons, options, onEvent = onEvent) {
        DialogInputNumberContent(
            value = value,
            label = label,
            inputPlaceholder = inputPlaceholder,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            enabled = enabled,
            clearable = clearable,
            prefix = prefix,
            suffix = suffix,
            validator = validator,
            textStyle = textStyle,
            requestFocus = requestFocus,
            selectionState = selectionState,
            onValueStateChanged = onValueStateChanged,
            onValidationChanged = { state.enableButton(DialogButtonType.Positive, it) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
