package com.michaelflisar.composedialogs.dialogs.color

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import com.michaelflisar.composedialogs.core.DialogButton
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEventType
import com.michaelflisar.composedialogs.core.rememberDialogState
import com.michaelflisar.composedialogs.dialogs.input.DialogInput
import com.michaelflisar.composedialogs.dialogs.input.DialogInputValidator
import com.michaelflisar.composedialogs.dialogs.input.rememberDialogInputValidator

internal object Material3ColorUIProvider : ColorUIProvider {
    @Composable
    override fun Text(
        text: String,
        modifier: Modifier,
        style: ColorTextStyle,
        color: Color?,
        fontWeight: FontWeight?,
    ) {
        val textStyle = when (style) {
            ColorTextStyle.TitleSmall -> MaterialTheme.typography.titleSmall
            ColorTextStyle.BodySmall -> MaterialTheme.typography.bodySmall
            ColorTextStyle.BodyMedium -> MaterialTheme.typography.bodyMedium
        }
        androidx.compose.material3.Text(
            text = text,
            modifier = modifier,
            style = textStyle,
            color = color ?: LocalContentColor.current,
            fontWeight = fontWeight
        )
    }

    @Composable
    override fun Slider(
        modifier: Modifier,
        value: Float,
        onValueChange: (Float) -> Unit,
    ) {
        androidx.compose.material3.Slider(
            modifier = modifier,
            value = value,
            onValueChange = onValueChange
        )
    }

    @Composable
    override fun Card(modifier: Modifier, content: @Composable () -> Unit) {
        androidx.compose.material3.Card(modifier = modifier) {
            content()
        }
    }

    @Composable
    override fun Input(
        modifier: Modifier,
        value: String,
        onValueChange: (String) -> Unit,
        isError: Boolean,
    ) {
        androidx.compose.material3.OutlinedTextField(
            modifier = modifier,
            value = value,
            onValueChange = onValueChange,
            isError = isError
        )
    }

    @Composable
    override fun TabButton(
        modifier: Modifier,
        onClick: () -> Unit,
        content: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit,
    ) {
        androidx.compose.material3.TextButton(
            modifier = modifier,
            onClick = onClick,
            content = content
        )
    }

    @Composable
    override fun Icon(icon: ColorPickerIcon, contentDescription: String?, tint: Color) {
        val imageVector = when (icon) {
            ColorPickerIcon.Check -> Icons.Default.Check
            ColorPickerIcon.Edit -> Icons.Default.Edit
        }
        androidx.compose.material3.Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint.takeIf { it != Color.Unspecified } ?: LocalContentColor.current
        )
    }

    @Composable
    override fun colorPresetShape(): Shape = MaterialTheme.shapes.small

    @Composable
    override fun InputDialog(
        visible: Boolean,
        title: String,
        value: MutableState<String>,
        isInputValid: (String) -> Boolean,
        invalidInputMessage: String,
        confirmText: String,
        dismissText: String,
        onConfirm: () -> Unit,
        onDismissRequest: () -> Unit,
    ) {
        if (visible) {
            val state = rememberDialogState(visible = true)
            val validator = rememberDialogInputValidator(
                validate = {
                    if (isInputValid(it)) {
                        DialogInputValidator.Result.Valid
                    } else {
                        DialogInputValidator.Result.Error(invalidInputMessage)
                    }
                },
                state = if (isInputValid(value.value)) {
                    DialogInputValidator.Result.Valid
                } else {
                    DialogInputValidator.Result.Error(invalidInputMessage)
                }
            )
            LaunchedEffect(value.value, invalidInputMessage) {
                validator.check(value.value)
                state.enableButton(
                    DialogButtonType.Positive,
                    validator.isValid()
                )
            }
            DialogInput(
                state = state,
                value = value,
                singleLine = true,
                clearable = false,
                prefix = "#",
                validator = validator,
                requestFocus = true,
                selectionState = DialogInput.SelectionState.SelectAll,
                buttons = DialogDefaults.buttons(
                    positive = DialogButton(confirmText),
                    negative = DialogButton(dismissText)
                ),
                onTextStateChanged = { valid, _ ->
                    state.enableButton(DialogButtonType.Positive, valid)
                },
                title = { androidx.compose.material3.Text(title) },
                onEvent = { event ->
                    when (event.type) {
                        DialogEventType.ButtonPositive -> {
                            if (isInputValid(value.value)) onConfirm()
                        }

                        DialogEventType.ButtonNegative,
                        DialogEventType.Dismissed -> onDismissRequest()
                    }
                }
            )
        }
    }
}
