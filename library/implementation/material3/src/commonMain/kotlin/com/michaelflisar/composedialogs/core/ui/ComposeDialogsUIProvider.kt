package com.michaelflisar.composedialogs.core.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox as Material3Checkbox
import androidx.compose.material3.Card as Material3Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu as Material3DropdownMenu
import androidx.compose.material3.DropdownMenuItem as Material3DropdownMenuItem
import androidx.compose.material3.Icon as Material3Icon
import androidx.compose.material3.IconButton as Material3IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton as Material3OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextField as Material3OutlinedTextField
import androidx.compose.material3.RadioButton as Material3RadioButton
import androidx.compose.material3.Slider as Material3Slider
import androidx.compose.material3.Text as Material3Text
import androidx.compose.material3.TextButton as Material3TextButton
import androidx.compose.material3.TimeInput as Material3TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.clickable
import androidx.compose.ui.window.Dialog as ComposeDialog
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.HorizontalDivider as Material3HorizontalDivider
import androidx.compose.material3.VerticalDivider as Material3VerticalDivider

internal object Material3ComposeDialogsUIProvider {
    val provider: ComposeDialogsUI.Provider = object : ComposeDialogsUI.Provider {
        @Composable
        override fun Text(
            text: String,
            role: ComposeDialogsUI.TextRole,
            modifier: Modifier
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
        override fun textStyle(role: ComposeDialogsUI.TextRole): TextStyle = when (role) {
            ComposeDialogsUI.TextRole.Label -> MaterialTheme.typography.titleSmall
            ComposeDialogsUI.TextRole.Body -> MaterialTheme.typography.bodyMedium
            ComposeDialogsUI.TextRole.BodyLarge -> MaterialTheme.typography.bodyLarge
            ComposeDialogsUI.TextRole.BodySmall -> MaterialTheme.typography.bodySmall
            ComposeDialogsUI.TextRole.Button -> MaterialTheme.typography.labelLarge
            ComposeDialogsUI.TextRole.TitleSmall -> MaterialTheme.typography.titleSmall
            ComposeDialogsUI.TextRole.TitleMedium -> MaterialTheme.typography.titleMedium
            ComposeDialogsUI.TextRole.HeadlineSmall -> MaterialTheme.typography.headlineSmall
        }

        @Composable
        override fun color(role: ComposeDialogsUI.ColorRole): Color = when (role) {
            ComposeDialogsUI.ColorRole.Primary -> MaterialTheme.colorScheme.primary
            ComposeDialogsUI.ColorRole.OnPrimary -> MaterialTheme.colorScheme.onPrimary
            ComposeDialogsUI.ColorRole.OnSurface -> MaterialTheme.colorScheme.onSurface
            ComposeDialogsUI.ColorRole.OnSurfaceVariant -> MaterialTheme.colorScheme.onSurfaceVariant
            ComposeDialogsUI.ColorRole.Outline -> MaterialTheme.colorScheme.outline
            ComposeDialogsUI.ColorRole.Error -> MaterialTheme.colorScheme.error
        }

        @Composable
        override fun ProvideContentColor(color: Color, content: @Composable () -> Unit) {
            CompositionLocalProvider(LocalContentColor provides color, content = content)
        }

        @Composable
        override fun ProvideTextStyle(style: TextStyle, content: @Composable () -> Unit) {
            androidx.compose.material3.ProvideTextStyle(value = style, content = content)
        }

        @Composable
        override fun Text(
            text: String,
            role: ComposeDialogsUI.TextRole,
            modifier: Modifier,
            color: Color?,
            textAlign: TextAlign?,
            fontWeight: FontWeight?
        ) {
            val style = when (role) {
                ComposeDialogsUI.TextRole.Label -> MaterialTheme.typography.titleSmall
                ComposeDialogsUI.TextRole.Body -> MaterialTheme.typography.bodyMedium
                ComposeDialogsUI.TextRole.BodyLarge -> MaterialTheme.typography.bodyLarge
                ComposeDialogsUI.TextRole.BodySmall -> MaterialTheme.typography.bodySmall
                ComposeDialogsUI.TextRole.Button -> MaterialTheme.typography.labelLarge
                ComposeDialogsUI.TextRole.TitleSmall -> MaterialTheme.typography.titleSmall
                ComposeDialogsUI.TextRole.TitleMedium -> MaterialTheme.typography.titleMedium
                ComposeDialogsUI.TextRole.HeadlineSmall -> MaterialTheme.typography.headlineSmall
            }
            Material3Text(
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
            content: @Composable () -> Unit
        ) {
            Material3TextButton(onClick = onClick, modifier = modifier) {
                content()
            }
        }

        @Composable
        override fun OutlinedButton(
            onClick: () -> Unit,
            enabled: Boolean,
            modifier: Modifier,
            content: @Composable () -> Unit
        ) {
            Material3OutlinedButton(onClick = onClick, enabled = enabled, modifier = modifier) {
                content()
            }
        }

        @Composable
        override fun IconButton(
            onClick: () -> Unit,
            enabled: Boolean,
            modifier: Modifier,
            content: @Composable () -> Unit
        ) {
            Material3IconButton(onClick = onClick, enabled = enabled, modifier = modifier) {
                content()
            }
        }

        @Composable
        override fun Card(
            modifier: Modifier,
            content: @Composable () -> Unit
        ) {
            Material3Card(modifier = modifier) {
                content()
            }
        }

        @Composable
        override fun OutlinedTextField(
            value: String,
            onValueChange: (String) -> Unit,
            modifier: Modifier,
            isError: Boolean
        ) {
            Material3OutlinedTextField(
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
            supportingText: (@Composable () -> Unit)?
        ) {
            Material3OutlinedTextField(
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
            supportingText: (@Composable () -> Unit)?
        ) {
            Material3OutlinedTextField(
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

        @OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
        @Composable
        override fun OutlinedContainer(
            title: String,
            modifier: Modifier,
            enabled: Boolean,
            onClick: (() -> Unit)?,
            leadingIcon: (@Composable () -> Unit)?,
            placeholder: (@Composable () -> Unit)?,
            trailingIcon: (@Composable () -> Unit)?,
            content: @Composable () -> Unit
        ) {
            val interactionSource = remember { MutableInteractionSource() }
            Box(modifier = modifier) {
                OutlinedTextFieldDefaults.DecorationBox(
                    value = title,
                    visualTransformation = VisualTransformation.None,
                    innerTextField = content,
                    label = if (title.isNotEmpty()) ({ Material3Text(title) }) else null,
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
            modifier: Modifier
        ) {
            Material3Slider(
                value = value,
                onValueChange = onValueChange,
                modifier = modifier
            )
        }

        @Composable
        override fun HorizontalDivider(modifier: Modifier) {
            Material3HorizontalDivider(modifier = modifier)
        }

        @Composable
        override fun VerticalDivider(modifier: Modifier) {
            Material3VerticalDivider(modifier = modifier)
        }

        @Composable
        override fun Checkbox(checked: Boolean, onCheckedChange: ((Boolean) -> Unit)?) {
            Material3Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        }

        @Composable
        override fun RadioButton(selected: Boolean, onClick: (() -> Unit)?) {
            Material3RadioButton(selected = selected, onClick = onClick)
        }

        @Composable
        override fun DropdownMenu(
            expanded: Boolean,
            onDismissRequest: () -> Unit,
            content: @Composable ColumnScope.() -> Unit
        ) {
            Material3DropdownMenu(
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
            onClick: () -> Unit
        ) {
            Material3DropdownMenuItem(
                modifier = modifier,
                text = text,
                onClick = onClick
            )
        }

        @OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
        @Composable
        override fun TimeInput(
            hour: Int,
            minute: Int,
            is24Hour: Boolean,
            modifier: Modifier,
            onTimeChange: (hour: Int, minute: Int) -> Unit
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
            Material3TimeInput(state = state, modifier = modifier)
        }

        @Composable
        override fun Dialog(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
            ComposeDialog(onDismissRequest = onDismissRequest) {
                content()
            }
        }

        @Composable
        override fun Icon(
            icon: ComposeDialogsUI.IconType,
            contentDescription: String?,
            modifier: Modifier,
            tint: Color?
        ) {
            Material3Icon(
                imageVector = when (icon) {
                    ComposeDialogsUI.IconType.Clear -> Icons.Outlined.Clear
                    ComposeDialogsUI.IconType.Check -> Icons.Default.Check
                    ComposeDialogsUI.IconType.ArrowDropDown -> Icons.Default.ArrowDropDown
                    ComposeDialogsUI.IconType.ArrowBack -> Icons.AutoMirrored.Filled.ArrowBack
                    ComposeDialogsUI.IconType.ArrowForward -> Icons.AutoMirrored.Filled.ArrowForward
                    ComposeDialogsUI.IconType.ChevronLeft -> Icons.AutoMirrored.Filled.KeyboardArrowLeft
                    ComposeDialogsUI.IconType.ChevronRight -> Icons.AutoMirrored.Filled.KeyboardArrowRight
                    ComposeDialogsUI.IconType.DoubleChevronLeft -> Icons.Default.KeyboardDoubleArrowLeft
                    ComposeDialogsUI.IconType.DoubleChevronRight -> Icons.Default.KeyboardDoubleArrowRight
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
            type: ComposeDialogsUI.ProgressIndicatorType,
            progress: Float?,
            modifier: Modifier
        ) {
            when (type) {
                ComposeDialogsUI.ProgressIndicatorType.Circular -> {
                    if (progress == null) {
                        CircularProgressIndicator(modifier = modifier)
                    } else {
                        CircularProgressIndicator(
                            progress = { progress },
                            modifier = modifier
                        )
                    }
                }

                ComposeDialogsUI.ProgressIndicatorType.Linear -> {
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
}
