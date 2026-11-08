package com.michaelflisar.composedialogs.dialogs.color.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.color.ColorPickerIcon
import com.michaelflisar.composedialogs.dialogs.color.ColorUIProvider
import com.michaelflisar.composedialogs.dialogs.color.ColorTextStyle
import com.michaelflisar.composedialogs.dialogs.color.ColorPickerTexts
import com.michaelflisar.composedialogs.dialogs.color.utils.drawCheckerboard
import com.michaelflisar.composedialogs.dialogs.color.utils.parseColor

@Composable
internal fun ColorPickerContent(
    uiProvider: ColorUIProvider,
    color: MutableState<Color>,
    texts: ColorPickerTexts,
    customPage: Boolean,
    landscape: Boolean,
    selectedSubColor: MutableState<Color?>,
    selectedPresetsLevel: MutableState<Int>,
    selectedAlpha: MutableState<Float>,
    alphaSupported: Boolean,
    directEditSupported: Boolean,
    shape: Shape,
    gridSize: Int,
    showPercent: Boolean,
    mainColors: List<Color>,
    mainLabels: List<String>?,
    subColors: List<Color>,
    subLabels: List<String>?,
    onMainColorSelected: (Int) -> Unit,
    onSubColorSelected: (Int) -> Unit,
    updateSelectedPresetColors: () -> Unit
) {
    val ui = uiProvider
    val density = LocalDensity.current
    val showDirectEditDialog = remember { mutableStateOf(false) }
    val directEditText = remember { mutableStateOf("") }
    val parsedDirectEditColor = parseColor(directEditText.value, alphaSupported)
    val colorCellSize = 48.dp
    val onEditColor = {
        directEditText.value = color.value.toArgb().toHexString()
            .substring(if (alphaSupported) 0 else 2)
        showDirectEditDialog.value = true
    }
    Column(modifier = Modifier.animateContentSize()) {
        Crossfade(
            modifier = Modifier,
            targetState = customPage,
            label = "color"
        ) { isCustomPage ->
            if (isCustomPage) {
                if (landscape) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ColorPickerPreview(
                                uiProvider = uiProvider,
                                color = color.value,
                                texts = texts,
                                alphaSupported = alphaSupported,
                                directEditSupported = directEditSupported,
                                shape = shape,
                                colorCellSize = colorCellSize,
                                landscape = true,
                                onEdit = onEditColor
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(2f)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (alphaSupported) {
                                ColorSlider(uiProvider, "A", color.value.alpha, showPercent) {
                                    selectedAlpha.value = it
                                    color.value = color.value.copy(alpha = it)
                                    updateSelectedPresetColors()
                                }
                            }
                            ColorSlider(uiProvider, "R", color.value.red, showPercent) {
                                color.value = color.value.copy(red = it)
                                updateSelectedPresetColors()
                            }
                            ColorSlider(uiProvider, "G", color.value.green, showPercent) {
                                color.value = color.value.copy(green = it)
                                updateSelectedPresetColors()
                            }
                            ColorSlider(uiProvider, "B", color.value.blue, showPercent) {
                                color.value = color.value.copy(blue = it)
                                updateSelectedPresetColors()
                            }
                        }
                    }
                } else {
                    Column {
                        ColorPickerPreview(
                            uiProvider = uiProvider,
                            color = color.value,
                            texts = texts,
                            alphaSupported = alphaSupported,
                            directEditSupported = directEditSupported,
                            shape = shape,
                            colorCellSize = colorCellSize,
                            landscape = false,
                            onEdit = onEditColor
                        )
                        if (alphaSupported) {
                            ColorSlider(uiProvider, "A", color.value.alpha, showPercent) {
                                selectedAlpha.value = it
                                color.value = color.value.copy(alpha = it)
                                updateSelectedPresetColors()
                            }
                        }
                        ColorSlider(uiProvider, "R", color.value.red, showPercent) {
                            color.value = color.value.copy(red = it)
                            updateSelectedPresetColors()
                        }
                        ColorSlider(uiProvider, "G", color.value.green, showPercent) {
                            color.value = color.value.copy(green = it)
                            updateSelectedPresetColors()
                        }
                        ColorSlider(uiProvider, "B", color.value.blue, showPercent) {
                            color.value = color.value.copy(blue = it)
                            updateSelectedPresetColors()
                        }
                    }
                }
            } else {
                val space = 8.dp
                Column(
                    modifier = if (landscape) {
                        Modifier.verticalScroll(rememberScrollState())
                    } else {
                        Modifier.wrapContentHeight()
                    },
                    verticalArrangement = Arrangement.spacedBy(space)
                ) {
                    val level = selectedPresetsLevel.value
                    if (level == 0) {
                        ColorGrid(
                            uiProvider,
                            level,
                            mainColors,
                            mainLabels,
                            selectedAlpha.value,
                            null,
                            gridSize,
                            shape,
                            space,
                            colorCellSize,
                            density,
                            onMainColorSelected
                        )
                    } else {
                        ColorGrid(
                            uiProvider,
                            level,
                            subColors,
                            subLabels,
                            selectedAlpha.value,
                            selectedSubColor.value,
                            gridSize,
                            shape,
                            space,
                            colorCellSize,
                            density
                        ) { index ->
                            onSubColorSelected(index)
                        }
                    }

                    if (alphaSupported) {
                        ColorSlider(uiProvider, "A", selectedAlpha.value, showPercent) {
                            selectedAlpha.value = it
                            color.value = color.value.copy(alpha = it)
                        }
                    }
                }

            }
        }
        if (directEditSupported) {
            ui.InputDialog(
                visible = showDirectEditDialog.value,
                title = texts.editColor,
                value = directEditText,
                isInputValid = { parseColor(it, alphaSupported) != null },
                invalidInputMessage = texts.invalidColor,
                confirmText = texts.apply,
                dismissText = texts.cancel,
                onConfirm = {
                    parsedDirectEditColor?.let {
                        color.value = it
                        selectedAlpha.value = it.alpha
                        updateSelectedPresetColors()
                        showDirectEditDialog.value = false
                    }
                },
                onDismissRequest = { showDirectEditDialog.value = false }
            )
        }
    }
}

@Composable
private fun ColorPickerPreview(
    uiProvider: ColorUIProvider,
    color: Color,
    texts: ColorPickerTexts,
    alphaSupported: Boolean,
    directEditSupported: Boolean,
    shape: Shape,
    colorCellSize: Dp,
    landscape: Boolean,
    onEdit: () -> Unit
) {
    if (landscape) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ColorPickerSwatch(
                color,
                shape,
                Modifier
                    .fillMaxWidth()
                    .height(colorCellSize)
            )
            ColorPickerValueCard(
                uiProvider = uiProvider,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(colorCellSize),
                color = color,
                texts = texts,
                alphaSupported = alphaSupported,
                directEditSupported = directEditSupported,
                onEdit = onEdit
            )
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ColorPickerSwatch(color, shape, Modifier.size(colorCellSize))
            ColorPickerValueCard(
                uiProvider = uiProvider,
                modifier = Modifier
                    .weight(1f)
                    .height(colorCellSize),
                color = color,
                texts = texts,
                alphaSupported = alphaSupported,
                directEditSupported = directEditSupported,
                onEdit = onEdit
            )
        }
    }
}

@Composable
private fun ColorPickerSwatch(
    color: Color,
    shape: Shape,
    modifier: Modifier
) {
    val density = LocalDensity.current
    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape)
        ) {
            drawCheckerboard(this, density)
        }
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .background(color, shape)
        )
    }
}

@Composable
private fun ColorPickerValueCard(
    uiProvider: ColorUIProvider,
    modifier: Modifier,
    color: Color,
    texts: ColorPickerTexts,
    alphaSupported: Boolean,
    directEditSupported: Boolean,
    onEdit: () -> Unit
) {
    val ui = uiProvider
    ui.Card(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                ui.Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (alphaSupported) "ARGB" else "RGB",
                    style = ColorTextStyle.TitleSmall,
                    color = null,
                    fontWeight = FontWeight.Bold
                )
                ui.Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "#" + color.toArgb().toHexString()
                        .substring(if (alphaSupported) 0 else 2),
                    style = ColorTextStyle.BodyMedium,
                    color = null,
                    fontWeight = null
                )
            }
            if (directEditSupported) {
                ui.TabButton(
                    modifier = Modifier,
                    onClick = onEdit
                ) {
                    ui.Icon(
                        icon = ColorPickerIcon.Edit,
                        contentDescription = texts.editColor,
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}
