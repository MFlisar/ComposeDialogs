package com.michaelflisar.composedialogs.dialogs.input

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import com.michaelflisar.composedialogs.core.ui.DialogUI
import com.michaelflisar.composedialogs.dialogs.input.composables.DialogInputTextField

/**
 * Displays the input field independently of the dialog container.
 */
@Composable
fun DialogInputContent(
    value: MutableState<String>,
    label: String = "",
    inputPlaceholder: String = "",
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    enabled: Boolean = true,
    clearable: Boolean = true,
    prefix: String = "",
    suffix: String = "",
    textStyle: TextStyle = DialogUI.provider.textStyle(DialogUI.TextRole.Body),
    validator: DialogInputValidator = rememberDialogInputValidator(),
    requestFocus: Boolean = false,
    selectionState: DialogInput.SelectionState = DialogInput.SelectionState.Default,
    onTextStateChanged: (valid: Boolean, text: String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    DialogInputTextField(
        modifier,
        value,
        label,
        inputPlaceholder,
        singleLine,
        maxLines,
        minLines,
        keyboardOptions,
        enabled,
        clearable,
        prefix,
        suffix,
        textStyle,
        validator,
        requestFocus,
        selectionState,
        onTextStateChanged
    )
}
