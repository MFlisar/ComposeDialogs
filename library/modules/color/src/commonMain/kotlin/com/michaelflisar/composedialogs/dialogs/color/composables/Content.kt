package com.michaelflisar.composedialogs.dialogs.color.composables

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.color.DialogColor
import com.michaelflisar.composedialogs.dialogs.color.DialogColorUtil
import com.michaelflisar.composedialogs.dialogs.color.classes.ColorDefinitions
import com.michaelflisar.composedialogs.dialogs.color.classes.GroupedColor

@OptIn(ExperimentalStdlibApi::class)
@Composable
internal fun Content(
    color: MutableState<Color>,
    colorState: MutableState<DialogColor.Page>,
    selectedMainColor: MutableState<GroupedColor>,
    selectedSubColor: MutableState<Color?>,
    selectedPresetsLevel: MutableState<Int>,
    selectedAlpha: MutableState<Float>,
    alphaSupported: Boolean,
    shape: Shape,
    gridSize: Int,
    labelStyle: DialogColor.LabelStyle
) {
    val density = LocalDensity.current
    val updateSelectedPresetColors = {
        selectedMainColor.value = DialogColorUtil.getNearestColorGroup(color.value)
        selectedSubColor.value = null
        selectedPresetsLevel.value = 0
    }
    Column(modifier = Modifier.animateContentSize()) {
        Crossfade(
            modifier = Modifier,
            targetState = colorState.value,
            label = "color"
        ) {
            when (it) {
                DialogColor.Page.Custom -> {
                    Column {
                        Row(
                            modifier = Modifier.height(IntrinsicSize.Min),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box {
                                Canvas(
                                    modifier = Modifier
                                        .height(IntrinsicSize.Min)
                                        .aspectRatio(1f)
                                        //.size(previewSize)
                                        .clip(shape)
                                ) {
                                    DialogColorUtil.drawCheckerboard(this, density)
                                }
                                Spacer(
                                    modifier = Modifier
                                        .height(IntrinsicSize.Min)
                                        .aspectRatio(1f)
                                        //.size(previewSize)
                                        .background(color.value, shape)
                                )
                            }

                            Card {
                                Column(
                                    modifier = Modifier
                                        .padding(all = 8.dp)
                                        .weight(1f)
                                ) {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = if (alphaSupported) "ARGB" else "RGB",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = "#" + color.value.toArgb().toHexString()
                                            .substring(if (alphaSupported) 0 else 2),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                        if (alphaSupported) {
                            ColorSlider("A", color.value.alpha, labelStyle) {
                                selectedAlpha.value = it
                                color.value = color.value.copy(alpha = it)
                                updateSelectedPresetColors()
                            }
                        }
                        ColorSlider("R", color.value.red, labelStyle) {
                            color.value = color.value.copy(red = it)
                            updateSelectedPresetColors()
                        }
                        ColorSlider("G", color.value.green, labelStyle) {
                            color.value = color.value.copy(green = it)
                            updateSelectedPresetColors()
                        }
                        ColorSlider("B", color.value.blue, labelStyle) {
                            color.value = color.value.copy(blue = it)
                            updateSelectedPresetColors()
                        }
                    }
                }

                DialogColor.Page.Presets -> {
                    val space = 8.dp
                    val size = 48.dp
                    Column(
                        modifier = Modifier.wrapContentHeight(),
                        verticalArrangement = Arrangement.spacedBy(space)
                    ) {
                        val level = selectedPresetsLevel.value
                        if (level == 0) {
                            ColorGrid(
                                level,
                                ColorDefinitions.COLORS.map { it.getColor() },
                                null,
                                selectedAlpha.value,
                                null,
                                gridSize,
                                shape,
                                space,
                                size,
                                density
                            ) {
                                selectedMainColor.value = ColorDefinitions.COLORS[it]
                                selectedPresetsLevel.value = 1
                            }
                        } else {
                            ColorGrid(
                                level,
                                selectedMainColor.value.colors.map { it.getColor() },
                                selectedMainColor.value.colors.map { it.label },
                                selectedAlpha.value,
                                selectedSubColor.value,
                                gridSize,
                                shape,
                                space,
                                size,
                                density
                            ) {
                                selectedSubColor.value =
                                    selectedMainColor.value.colors[it].getColor()
                                color.value = selectedSubColor.value!!.copy(selectedAlpha.value)
                            }
                        }
                        if (alphaSupported) {
                            ColorSlider("A", selectedAlpha.value, labelStyle) {
                                selectedAlpha.value = it
                                color.value = color.value.copy(alpha = it)
                            }
                        }
                    }
                }
            }
        }
    }
}