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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.color.ui.ColorPickerContent
import com.michaelflisar.composedialogs.dialogs.color.ui.TitleForPages

/**
 * Renders the color picker using the caller-supplied [ColorUIProvider].
 *
 * Preset data, selection state, and callbacks are explicit so this picker can be used independently
 * of the dialog module.
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
    landscape: Boolean,
    customPage: Boolean,
    selectedSubColor: MutableState<Color?>,
    selectedPresetsLevel: MutableState<Int>,
    selectedAlpha: MutableState<Float>,
    mainColors: List<Color>,
    mainLabels: List<String>?,
    subColors: List<Color>,
    subLabels: List<String>?,
    onMainColorSelected: (Int) -> Unit,
    onSubColorSelected: (Int) -> Unit,
    onPresetsClick: () -> Unit,
    onCustomClick: () -> Unit,
    updateSelectedPresetColors: () -> Unit
) {
    BackHandler(enabled = selectedPresetsLevel.value != 0) {
        selectedPresetsLevel.value = 0
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
                        uiProvider,
                        color,
                        texts,
                        customPage,
                        landscape,
                        selectedSubColor,
                        selectedPresetsLevel,
                        selectedAlpha,
                        alphaSupported,
                        directEditSupported,
                        shape,
                        gridSize,
                        labelStyle == ColorPickerLabelStyle.Percent,
                        mainColors,
                        mainLabels,
                        subColors,
                        subLabels,
                        onMainColorSelected,
                        onSubColorSelected,
                        updateSelectedPresetColors
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
                    onCustomClick
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            ColorPickerContent(
                uiProvider,
                color,
                texts,
                customPage,
                landscape,
                selectedSubColor,
                selectedPresetsLevel,
                selectedAlpha,
                alphaSupported,
                directEditSupported,
                shape,
                gridSize,
                labelStyle == ColorPickerLabelStyle.Percent,
                mainColors,
                mainLabels,
                subColors,
                subLabels,
                onMainColorSelected,
                onSubColorSelected,
                updateSelectedPresetColors
            )
        }
    }
}
