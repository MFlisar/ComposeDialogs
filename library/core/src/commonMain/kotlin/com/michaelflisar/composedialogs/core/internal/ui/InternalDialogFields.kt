package com.michaelflisar.composedialogs.core.internal.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

val InternalDialogControlMinHeight = 48.dp

val InternalDialogShape: Shape
    @Composable get() = MaterialTheme.shapes.small

@Composable
fun InternalDialogLabeledField(
    label: String?,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    labelTextAlign: TextAlign = TextAlign.Start,
    content: @Composable () -> Unit
) {
    val fieldContent: @Composable (Modifier) -> Unit = { contentModifier ->
        Column(
            modifier = contentModifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = label ?: " ",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelSmall,
                color = if (label == null) Color.Transparent else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                textAlign = labelTextAlign
            )
            content()
            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                    maxLines = 2
                )
            }
        }
    }
    if (label == null) {
        fieldContent(modifier)
    } else {
        Surface(
            modifier = modifier,
            shape = InternalDialogShape,
            color = MaterialTheme.colorScheme.surfaceContainerLow
        ) {
            fieldContent(Modifier)
        }
    }
}

@Composable
fun InternalDialogNumberInput(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    title: String,
    keyboardType: KeyboardType = KeyboardType.Number,
    readOnly: Boolean = false,
    onFocusChanged: (Boolean) -> Unit = {}
) {
    val focused = remember { mutableStateOf(false) }
    val textFieldValue = remember { mutableStateOf(TextFieldValue(value)) }
    SideEffect {
        if (textFieldValue.value.text != value) {
            textFieldValue.value = TextFieldValue(
                text = value,
                selection = TextRange(value.length)
            )
        }
    }
    val focusedColor = MaterialTheme.colorScheme.primary
    val unfocusedColor = MaterialTheme.colorScheme.outlineVariant
    BasicTextField(
        modifier = modifier
            .heightIn(min = InternalDialogControlMinHeight)
            .padding(horizontal = 4.dp)
            .onFocusChanged {
                focused.value = it.isFocused
                if (it.isFocused && !readOnly) {
                    textFieldValue.value = textFieldValue.value.copy(
                        selection = TextRange(0, textFieldValue.value.text.length)
                    )
                }
                onFocusChanged(it.isFocused)
            }
            .drawBehind {
                val strokeWidth = if (focused.value) 2.dp.toPx() else 1.dp.toPx()
                val y = size.height - strokeWidth / 2f
                drawLine(
                    color = if (focused.value) focusedColor else unfocusedColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            }
            .semantics { contentDescription = title },
        value = textFieldValue.value,
        onValueChange = { newValue ->
            textFieldValue.value = newValue
            onValueChange(newValue.text)
        },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        readOnly = readOnly,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        decorationBox = { innerTextField ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    innerTextField()
                }
            }
        }
    )
}

@Composable
fun InternalDialogInputButton(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier
            .heightIn(min = InternalDialogControlMinHeight)
            .semantics { contentDescription = "$title: $value" },
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 16.dp),
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Start,
            minLines = 1,
            maxLines = 1
        )
        androidx.compose.material3.Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun <T : Any> InternalDialogTextDropdown(
    modifier: Modifier = Modifier,
    title: String,
    items: List<T>,
    selected: T,
    itemText: (T) -> String,
    onItemSelected: (T) -> Unit,
    selectedText: String? = null,
    selectedTextAlign: TextAlign = TextAlign.Start,
    selectedTextStyle: TextStyle = LocalTextStyle.current,
    itemTextStyle: TextStyle = LocalTextStyle.current
) {
    val selectedIndex = items.indexOf(selected)
    val displayText = selectedText
        ?: items.getOrNull(selectedIndex)?.let(itemText).orEmpty()
    val expanded = remember { mutableStateOf(false) }
    val rotation = animateFloatAsState(if (expanded.value) -180f else 0f).value

    Box(modifier = modifier) {
        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = InternalDialogControlMinHeight)
                .semantics {
                    contentDescription = title
                    stateDescription = displayText
                },
            onClick = { expanded.value = !expanded.value },
            contentPadding = PaddingValues(horizontal = 16.dp),
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text(
                text = displayText,
                modifier = Modifier.weight(1f),
                style = selectedTextStyle,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = selectedTextAlign,
                minLines = 1,
                maxLines = 1
            )
            androidx.compose.material3.Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.rotate(rotation),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        androidx.compose.material3.DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            scrollState = rememberScrollState()
        ) {
            items.forEachIndexed { index, item ->
                androidx.compose.material3.DropdownMenuItem(
                    modifier = Modifier.fillMaxWidth().zIndex(1f),
                    text = {
                        Text(
                            text = itemText(item),
                            modifier = Modifier.fillMaxWidth(),
                            style = itemTextStyle,
                            color = if (index == selectedIndex) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                            minLines = 1,
                            maxLines = 1
                        )
                    },
                    onClick = {
                        onItemSelected(item)
                        expanded.value = false
                    }
                )
            }
        }
    }
}

@Composable
fun <T : Any> InternalDialogVerticalSegmentedButton(
    modifier: Modifier = Modifier,
    items: List<T>,
    selected: T,
    itemText: (T) -> String,
    onItemSelected: (T) -> Unit
) {
    Column(modifier = modifier.fillMaxHeight()) {
        items.forEach { item ->
            val isSelected = item == selected
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(InternalDialogShape)
                    .background(
                        if (isSelected) {
                            MaterialTheme.colorScheme.secondaryContainer
                        } else {
                            Color.Transparent
                        }
                    )
                    .selectable(
                        selected = isSelected,
                        onClick = { onItemSelected(item) },
                        role = Role.RadioButton
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = itemText(item),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    maxLines = 1
                )
            }
        }
    }
}
