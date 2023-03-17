package com.michaelflisar.composedialogs.dialogs.color

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.*
import com.michaelflisar.composedialogs.dialogs.color.classes.ColorDefinitions
import java.text.DecimalFormat
import kotlin.math.ceil

@Composable
fun DialogColor(
    // Base Dialog - State
    state: DialogState,
    // Custom - Required
    color: MutableState<Color>,
    // Custom - Optional
    texts: DialogColorTexts = DialogColorTexts(),
    colorState: MutableState<DialogColorPage> = rememberDialogColorState(),
    alphaSupported: Boolean = true,
    shape: Shape = MaterialTheme.shapes.small,
    gridSize: Int = 4,
    labelStyle: DialogColorLabelStyle = DialogColorLabelStyle.Value,
    // Base Dialog - Optional
    title: DialogTitle = DialogDefaults.title(),
    icon: DialogIcon? = null,
    style: DialogStyle = DialogDefaults.styleDialog(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    val context = LocalContext.current

    val checkerBoardPixelSize = with(LocalDensity.current) { 4.dp.toPx().toInt() }

    var selectedMainColor by remember {
        mutableStateOf(
            DialogColorUtil.getNearestColorGroup(
                context,
                color.value
            )
        )
    }
    var selectedSubColor by remember { mutableStateOf<Color?>(null) }
    var selectedPresetsLevel by remember { mutableStateOf(0) }
    var selectedAlpha by remember { mutableStateOf(color.value.alpha) }

    val updateSelectedPresetColors = {
        selectedMainColor = DialogColorUtil.getNearestColorGroup(context, color.value)
        selectedSubColor = null
        selectedPresetsLevel = 0
    }

    Dialog(state, title, icon, style, buttons, options, onEvent) {
        // Title to switch pages
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextButton(
                modifier = Modifier
                    .weight(1f)
                    .alpha(if (colorState.value == DialogColorPage.Custom) .5f else 1f),
                onClick = {
                    colorState.value = DialogColorPage.Presets
                    selectedSubColor = null
                    selectedPresetsLevel = 0
                }
            ) {
                Text(text = texts.presets)
            }
            TextButton(
                modifier = Modifier
                    .weight(1f)
                    .alpha(if (colorState.value == DialogColorPage.Presets) .5f else 1f),
                onClick = { colorState.value = DialogColorPage.Custom }
            ) {
                Text(text = texts.custom)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        //state.dismissable(selectedPresetsLevel == 0 || colorState.value == DialogColorPage.Custom)
        //BackHandler(enabled = selectedPresetsLevel == 1) {
        //    selectedPresetsLevel = 0
        //}

        // Content
        when (colorState.value) {
            DialogColorPage.Custom -> {

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
                            DialogColorUtil.drawCheckerboard(this, checkerBoardPixelSize)
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
                                text = "#" + Integer.toHexString(color.value.toArgb()).substring(if (alphaSupported) 0 else 2),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                if (alphaSupported) {
                    ColorSlider("A", color.value.alpha, labelStyle) {
                        selectedAlpha = it
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
            DialogColorPage.Presets -> {

                val space = 8.dp
                val size = 48.dp
                Column(
                    verticalArrangement = Arrangement.spacedBy(space)
                ) {
                    if (selectedPresetsLevel == 0) {
                        ColorGrid(
                            true,
                            ColorDefinitions.COLORS.map { it.getColor(context) },
                            selectedAlpha,
                            null,
                            gridSize,
                            shape,
                            space,
                            size,
                            checkerBoardPixelSize
                        ) {
                            selectedMainColor = ColorDefinitions.COLORS[it]
                            selectedPresetsLevel = 1
                        }
                    } else {
                        ColorGrid(
                            false,
                            selectedMainColor.colors.map { it.getColor(context) },
                            selectedAlpha,
                            selectedSubColor,
                            gridSize,
                            shape,
                            space,
                            size,
                            checkerBoardPixelSize
                        ) {
                            selectedSubColor = selectedMainColor.colors[it].getColor(context)
                            color.value = selectedSubColor!!.copy(selectedAlpha)
                        }
                    }

                    if (alphaSupported) {
                        ColorSlider("A", selectedAlpha, labelStyle) {
                            selectedAlpha = it
                            color.value = color.value.copy(alpha = it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun rememberDialogColorState(
    page: DialogColorPage = DialogColorPage.Presets
): MutableState<DialogColorPage> {
    return remember { mutableStateOf(page) }
}

private val DF_PERCENTAGES = DecimalFormat("#.#")

enum class DialogColorPage {
    Custom, Presets
}

enum class DialogColorLabelStyle {
    Value, Percent
}

data class DialogColorTexts(
    val presets: String = "Presets",
    val custom: String = "Custom"
)

@Composable
private fun ColorSlider(
    label: String,
    value: Float,
    labelStyle: DialogColorLabelStyle,
    onValueChange: (value: Float) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = label)
        Slider(modifier = Modifier.weight(1f), value = value, onValueChange = onValueChange)
        when (labelStyle) {
            DialogColorLabelStyle.Value -> Text(text = (255f * value).toInt().toString())
            DialogColorLabelStyle.Percent -> { Text(modifier = Modifier.width(48.dp), text = DF_PERCENTAGES.format(value * 100f) + "%", textAlign = TextAlign.End) }
        }

    }
}

@Composable
private fun ColorGrid(
    supportBlackWhite: Boolean,
    colors: List<Color>,
    selectedAlpha: Float,
    selectedColor: Color?,
    gridSize: Int,
    shape: Shape,
    space: Dp,
    size: Dp,
    checkerBoardPixelSize: Int,
    onClick: (index: Int) -> Unit
) {
    val rows = ceil(colors.size.toFloat() / gridSize.toFloat()).toInt()
    for (r in 0 until rows) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(space)
        ) {
            for (c in 0 until gridSize) {
                val index = r * gridSize + c
                val color = colors.getOrNull(index)

                if (color == null) {
                    Spacer(
                        modifier = Modifier
                            .height(size)
                            .weight(1f)
                    )
                } else {
                    val colorWithAlpha = color.copy(alpha = selectedAlpha)
                    val onColor = DialogColorUtil.getBestTextColor(colorWithAlpha)
                    Box(
                        modifier = Modifier
                            .height(size)
                            .weight(1f)
                            .background(colorWithAlpha, shape)
                            .clip(shape)
                            .clickable {
                                onClick(index)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(shape)
                        ) {
                            DialogColorUtil.drawCheckerboard(this, checkerBoardPixelSize)
                        }
                        if (color == Color.Black && supportBlackWhite) {
                            Canvas(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(shape)
                            ) {
                                DialogColorUtil.drawBlackWhite(this, selectedAlpha)
                            }
                        } else {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(colorWithAlpha, shape)
                            )
                        }
                        if (selectedColor == color) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "",
                                tint = onColor
                            )
                        }
                    }
                }
            }
        }
    }
}