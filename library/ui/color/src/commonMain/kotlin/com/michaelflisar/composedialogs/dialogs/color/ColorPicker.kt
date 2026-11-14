package com.michaelflisar.composedialogs.dialogs.color

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.color.internal.ColorPickerPage
import com.michaelflisar.composedialogs.dialogs.color.ui.ColorPickerContent
import com.michaelflisar.composedialogs.dialogs.color.ui.TitleForPages

/**
 * Renders the color picker using the caller-supplied [ColorUIProvider].
 *
 * Palette navigation and editing state are managed by the picker.
 */
@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun ColorPicker(
    uiProvider: ColorUIProvider,
    color: MutableState<Color>,
    texts: ColorPickerTexts,
    alphaSupported: Boolean,
    directEditSupported: Boolean,
    shape: Shape,
    gridSize: Int,
    labelStyle: ColorPickerLabelStyle,
    landscape: Boolean
) {
    val selectedSubColor = rememberSaveable(saver = NullableColorStateSaver) {
        mutableStateOf<Color?>(null)
    }
    val selectedPresetsLevel = rememberSaveable { mutableIntStateOf(0) }
    val selectedMainColorIndex = rememberSaveable {
        mutableIntStateOf(getNearestColorGroupIndex(color.value))
    }
    val selectedAlpha = rememberSaveable { mutableFloatStateOf(color.value.alpha) }
    val pickerPage = rememberSaveable { mutableStateOf(ColorPickerPage.Presets) }
    val selectedMainColor = ColorDefinitions.COLORS[selectedMainColorIndex.intValue]
    val mainColors = remember { ColorDefinitions.COLORS.map { it.mainColor } }
    val subColors = selectedMainColor.colors.map { it.color }
    val subLabels = selectedMainColor.colors.map { it.label }

    val updateSelectedPresetColors = {
        selectedMainColorIndex.intValue = getNearestColorGroupIndex(color.value)
        selectedSubColor.value = null
        selectedPresetsLevel.intValue = 0
    }
    val onMainColorSelected: (Int) -> Unit = {
        selectedMainColorIndex.intValue = it
        selectedPresetsLevel.intValue = 1
    }
    val onSubColorSelected: (Int) -> Unit = {
        val selected = selectedMainColor.colors[it].color
        selectedSubColor.value = selected
        color.value = selected.copy(alpha = selectedAlpha.floatValue)
    }
    val onPresetsClick = {
        pickerPage.value = ColorPickerPage.Presets
        selectedSubColor.value = null
        selectedPresetsLevel.intValue = 0
    }
    val onCustomClick = {
        pickerPage.value = ColorPickerPage.Custom
    }
    val customPage = pickerPage.value == ColorPickerPage.Custom

    BackHandler(enabled = selectedPresetsLevel.intValue != 0) {
        selectedPresetsLevel.intValue = 0
    }
    Column {
        if (landscape) {
            Row {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    TitleForPages(
                        uiProvider = uiProvider,
                        modifier = Modifier.weight(1f),
                        presetsText = texts.presets,
                        customText = texts.custom,
                        customSelected = customPage,
                        onPresetsClick = onPresetsClick,
                        onCustomClick = onCustomClick
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    ColorPickerContent(
                        uiProvider = uiProvider,
                        color = color,
                        texts = texts,
                        customPage = customPage,
                        landscape = landscape,
                        selectedSubColor = selectedSubColor,
                        selectedPresetsLevel = selectedPresetsLevel,
                        selectedAlpha = selectedAlpha,
                        alphaSupported = alphaSupported,
                        directEditSupported = directEditSupported,
                        shape = shape,
                        gridSize = gridSize,
                        showPercent = labelStyle == ColorPickerLabelStyle.Percent,
                        mainColors = mainColors,
                        mainLabels = null,
                        subColors = subColors,
                        subLabels = subLabels,
                        onMainColorSelected = onMainColorSelected,
                        onSubColorSelected = onSubColorSelected,
                        updateSelectedPresetColors = updateSelectedPresetColors
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TitleForPages(
                    uiProvider = uiProvider,
                    modifier = Modifier.weight(1f),
                    presetsText = texts.presets,
                    customText = texts.custom,
                    customSelected = customPage,
                    onPresetsClick = onPresetsClick,
                    onCustomClick = onCustomClick
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            ColorPickerContent(
                uiProvider = uiProvider,
                color = color,
                texts = texts,
                customPage = customPage,
                landscape = landscape,
                selectedSubColor = selectedSubColor,
                selectedPresetsLevel = selectedPresetsLevel,
                selectedAlpha = selectedAlpha,
                alphaSupported = alphaSupported,
                directEditSupported = directEditSupported,
                shape = shape,
                gridSize = gridSize,
                showPercent = labelStyle == ColorPickerLabelStyle.Percent,
                mainColors = mainColors,
                mainLabels = null,
                subColors = subColors,
                subLabels = subLabels,
                onMainColorSelected = onMainColorSelected,
                onSubColorSelected = onSubColorSelected,
                updateSelectedPresetColors = updateSelectedPresetColors
            )
        }
    }
}
