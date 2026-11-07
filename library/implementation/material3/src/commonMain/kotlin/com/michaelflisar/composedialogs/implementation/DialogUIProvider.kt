package com.michaelflisar.composedialogs.implementation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.michaelflisar.composedialogs.core.ui.DialogUI

internal object DialogDefaultUIProvider {
    val impl: DialogUI.Provider = Material3DialogUIProvider
}

private object Material3DialogUIProvider : DialogUI.Provider {
    @Composable
    override fun Text(
        text: String,
        role: DialogUI.TextRole,
        modifier: Modifier,
    ) {
        Text(
            text = text,
            role = role,
            modifier = modifier,
            color = null,
            textAlign = null,
            fontWeight = null
        )
    }

    @Composable
    override fun textStyle(role: DialogUI.TextRole): TextStyle = when (role) {
        DialogUI.TextRole.Label -> MaterialTheme.typography.titleSmall
        DialogUI.TextRole.Body -> MaterialTheme.typography.bodyMedium
        DialogUI.TextRole.BodyLarge -> MaterialTheme.typography.bodyLarge
        DialogUI.TextRole.BodySmall -> MaterialTheme.typography.bodySmall
        DialogUI.TextRole.Button -> MaterialTheme.typography.labelLarge
        DialogUI.TextRole.TitleSmall -> MaterialTheme.typography.titleSmall
        DialogUI.TextRole.TitleMedium -> MaterialTheme.typography.titleMedium
        DialogUI.TextRole.HeadlineSmall -> MaterialTheme.typography.headlineSmall
    }

    @Composable
    override fun color(role: DialogUI.ColorRole): Color = when (role) {
        DialogUI.ColorRole.Primary -> MaterialTheme.colorScheme.primary
        DialogUI.ColorRole.OnPrimary -> MaterialTheme.colorScheme.onPrimary
        DialogUI.ColorRole.OnSurface -> MaterialTheme.colorScheme.onSurface
        DialogUI.ColorRole.OnSurfaceVariant -> MaterialTheme.colorScheme.onSurfaceVariant
        DialogUI.ColorRole.Outline -> MaterialTheme.colorScheme.outline
        DialogUI.ColorRole.Error -> MaterialTheme.colorScheme.error
    }

    @Composable
    override fun ProvideContentColor(color: Color, content: @Composable () -> Unit) {
        CompositionLocalProvider(LocalContentColor provides color, content = content)
    }

    @Composable
    override fun ProvideTextStyle(style: TextStyle, content: @Composable () -> Unit) {
        ProvideTextStyle(value = style, content = content)
    }

    @Composable
    override fun Text(
        text: String,
        role: DialogUI.TextRole,
        modifier: Modifier,
        color: Color?,
        textAlign: TextAlign?,
        fontWeight: FontWeight?,
    ) {
        val style = when (role) {
            DialogUI.TextRole.Label -> MaterialTheme.typography.titleSmall
            DialogUI.TextRole.Body -> MaterialTheme.typography.bodyMedium
            DialogUI.TextRole.BodyLarge -> MaterialTheme.typography.bodyLarge
            DialogUI.TextRole.BodySmall -> MaterialTheme.typography.bodySmall
            DialogUI.TextRole.Button -> MaterialTheme.typography.labelLarge
            DialogUI.TextRole.TitleSmall -> MaterialTheme.typography.titleSmall
            DialogUI.TextRole.TitleMedium -> MaterialTheme.typography.titleMedium
            DialogUI.TextRole.HeadlineSmall -> MaterialTheme.typography.headlineSmall
        }
        Text(
            text = text,
            modifier = modifier,
            color = color ?: Color.Unspecified,
            style = if (fontWeight == null) style else style.copy(fontWeight = fontWeight),
            textAlign = textAlign
        )
    }

    @Composable
    override fun TextButton(
        onClick: () -> Unit,
        modifier: Modifier,
        content: @Composable () -> Unit,
    ) {
        androidx.compose.material3.TextButton(onClick = onClick, modifier = modifier) {
            content()
        }
    }

    @Composable
    override fun OutlinedButton(
        onClick: () -> Unit,
        enabled: Boolean,
        modifier: Modifier,
        content: @Composable () -> Unit,
    ) {
        androidx.compose.material3.OutlinedButton(onClick = onClick, enabled = enabled, modifier = modifier) {
            content()
        }
    }

    @Composable
    override fun IconButton(
        onClick: () -> Unit,
        enabled: Boolean,
        modifier: Modifier,
        content: @Composable () -> Unit,
    ) {
        androidx.compose.material3.IconButton(onClick = onClick, enabled = enabled, modifier = modifier) {
            content()
        }
    }

    @Composable
    override fun Card(
        modifier: Modifier,
        content: @Composable () -> Unit,
    ) {
        androidx.compose.material3.Card(modifier = modifier) {
            content()
        }
    }

    @Composable
    override fun OutlinedTextField(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier,
        isError: Boolean,
    ) {
        androidx.compose.material3.OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            isError = isError
        )
    }

    @Composable
    override fun OutlinedTextField(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        readOnly: Boolean,
        isError: Boolean,
        singleLine: Boolean,
        keyboardOptions: KeyboardOptions,
        label: (@Composable () -> Unit)?,
        placeholder: (@Composable () -> Unit)?,
        leadingIcon: (@Composable () -> Unit)?,
        trailingIcon: (@Composable () -> Unit)?,
        supportingText: (@Composable () -> Unit)?,
    ) {
        androidx.compose.material3.OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            enabled = enabled,
            readOnly = readOnly,
            isError = isError,
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            supportingText = supportingText
        )
    }

    @Composable
    override fun OutlinedTextField(
        value: TextFieldValue,
        onValueChange: (TextFieldValue) -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        isError: Boolean,
        singleLine: Boolean,
        minLines: Int,
        maxLines: Int,
        keyboardOptions: KeyboardOptions,
        textStyle: TextStyle,
        label: (@Composable () -> Unit)?,
        placeholder: (@Composable () -> Unit)?,
        prefix: (@Composable () -> Unit)?,
        suffix: (@Composable () -> Unit)?,
        trailingIcon: (@Composable () -> Unit)?,
        supportingText: (@Composable () -> Unit)?,
    ) {
        androidx.compose.material3.OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            enabled = enabled,
            isError = isError,
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            textStyle = textStyle,
            label = label,
            placeholder = placeholder,
            prefix = prefix,
            suffix = suffix,
            trailingIcon = trailingIcon,
            supportingText = supportingText
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun OutlinedContainer(
        title: String,
        modifier: Modifier,
        enabled: Boolean,
        onClick: (() -> Unit)?,
        leadingIcon: (@Composable () -> Unit)?,
        placeholder: (@Composable () -> Unit)?,
        trailingIcon: (@Composable () -> Unit)?,
        content: @Composable () -> Unit,
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        Box(modifier = modifier) {
            OutlinedTextFieldDefaults.DecorationBox(
                value = title,
                visualTransformation = VisualTransformation.None,
                innerTextField = content,
                label = if (title.isNotEmpty()) ({ Text(title) }) else null,
                placeholder = placeholder,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                singleLine = true,
                enabled = enabled,
                isError = false,
                interactionSource = interactionSource,
                colors = OutlinedTextFieldDefaults.colors(),
                container = {
                    OutlinedTextFieldDefaults.Container(
                        enabled = enabled,
                        isError = false,
                        interactionSource = interactionSource,
                        colors = OutlinedTextFieldDefaults.colors(),
                        modifier = Modifier.then(
                            if (enabled && onClick != null) {
                                Modifier.clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                    enabled = enabled,
                                    onClick = onClick
                                )
                            } else Modifier
                        )
                    )
                }
            )
        }
    }

    @Composable
    override fun Slider(
        value: Float,
        onValueChange: (Float) -> Unit,
        modifier: Modifier,
    ) {
        androidx.compose.material3.Slider(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
        )
    }

    @Composable
    override fun HorizontalDivider(modifier: Modifier) {
        androidx.compose.material3.HorizontalDivider(modifier = modifier)
    }

    @Composable
    override fun VerticalDivider(modifier: Modifier) {
        androidx.compose.material3.VerticalDivider(modifier = modifier)
    }

    @Composable
    override fun Checkbox(checked: Boolean, onCheckedChange: ((Boolean) -> Unit)?) {
        androidx.compose.material3.Checkbox(checked = checked, onCheckedChange = onCheckedChange)
    }

    @Composable
    override fun RadioButton(selected: Boolean, onClick: (() -> Unit)?) {
        androidx.compose.material3.RadioButton(selected = selected, onClick = onClick)
    }

    @Composable
    override fun DropdownMenu(
        expanded: Boolean,
        onDismissRequest: () -> Unit,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        androidx.compose.material3.DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            content = content
        )
    }

    @Composable
    override fun DropdownMenuItem(
        modifier: Modifier,
        selected: Boolean,
        text: @Composable () -> Unit,
        onClick: () -> Unit,
    ) {
        androidx.compose.material3.DropdownMenuItem(
            modifier = modifier,
            text = text,
            onClick = onClick
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun TimeInput(
        hour: Int,
        minute: Int,
        is24Hour: Boolean,
        modifier: Modifier,
        onTimeChange: (hour: Int, minute: Int) -> Unit,
    ) {
        val state = rememberTimePickerState(
            initialHour = hour,
            initialMinute = minute,
            is24Hour = is24Hour
        )
        LaunchedEffect(hour, minute) {
            state.hour = hour
            state.minute = minute
        }
        LaunchedEffect(state.hour, state.minute) {
            onTimeChange(state.hour, state.minute)
        }
        TimeInput(state = state, modifier = modifier)
    }

    @Composable
    override fun Dialog(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
        androidx.compose.ui.window.Dialog(onDismissRequest = onDismissRequest) {
            content()
        }
    }

    @Composable
    override fun Icon(
        icon: DialogUI.IconType,
        contentDescription: String?,
        modifier: Modifier,
        tint: Color?,
    ) {
        Icon(
            imageVector = when (icon) {
                DialogUI.IconType.Clear -> Icons.Outlined.Clear
                DialogUI.IconType.Check -> Icons.Default.Check
                DialogUI.IconType.ArrowDropDown -> Icons.Default.ArrowDropDown
                DialogUI.IconType.ArrowBack -> Icons.AutoMirrored.Filled.ArrowBack
                DialogUI.IconType.ArrowForward -> Icons.AutoMirrored.Filled.ArrowForward
                DialogUI.IconType.ChevronLeft -> Icons.AutoMirrored.Filled.KeyboardArrowLeft
                DialogUI.IconType.ChevronRight -> Icons.AutoMirrored.Filled.KeyboardArrowRight
                DialogUI.IconType.DoubleChevronLeft -> Icons.Default.KeyboardDoubleArrowLeft
                DialogUI.IconType.DoubleChevronRight -> Icons.Default.KeyboardDoubleArrowRight
            },
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint ?: LocalContentColor.current
        )
    }

    @Composable
    override fun smallShape(): Shape = MaterialTheme.shapes.small

    @Composable
    override fun ProgressIndicator(
        type: DialogUI.ProgressIndicatorType,
        progress: Float?,
        modifier: Modifier,
    ) {
        when (type) {
            DialogUI.ProgressIndicatorType.Circular -> {
                if (progress == null) {
                    CircularProgressIndicator(modifier = modifier)
                } else {
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = modifier
                    )
                }
            }

            DialogUI.ProgressIndicatorType.Linear -> {
                if (progress == null) {
                    LinearProgressIndicator(modifier = modifier)
                } else {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = modifier
                    )
                }
            }
        }
    }
}
