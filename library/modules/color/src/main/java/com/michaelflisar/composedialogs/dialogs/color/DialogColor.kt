package com.michaelflisar.composedialogs.dialogs.color

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.*
import com.michaelflisar.composedialogs.dialogs.color.classes.ColorDefinitions
import com.michaelflisar.composedialogs.dialogs.color.classes.GroupedColor
import java.text.DecimalFormat
import kotlin.math.ceil

object DialogColor {
    object ColorStateSaver: Saver<MutableState<Color>, Int> {
        override fun restore(value: Int): MutableState<Color> {
            return mutableStateOf(Color(value))
        }

        override fun SaverScope.save(value: MutableState<Color>): Int {
            return value.value.toArgb()
        }

    }

    object ColorStateSaverNullable: Saver<MutableState<Color?>, String> {
        override fun restore(value: String): MutableState<Color?> {
            return mutableStateOf(value.takeIf { it.isNotEmpty() }?.toInt()?.let { Color(it) })
        }

        override fun SaverScope.save(value: MutableState<Color?>): String{
            return value.value?.toArgb()?.toString() ?: ""
        }

    }
}

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
    gridSize: Int = if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) 6 else 4,
    labelStyle: DialogColorLabelStyle = DialogColorLabelStyle.Value,
    // Base Dialog - Optional
    title: String = "",
    titleStyle: DialogTitleStyle = DialogDefaults.titleStyle(),
    icon: DialogIcon? = null,
    style: DialogStyle = DialogDefaults.styleDialog(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    // saved dialog state
    val selectedSubColor = rememberSaveable(saver = DialogColor.ColorStateSaverNullable) { mutableStateOf(null) }
    val selectedPresetsLevel = rememberSaveable { mutableIntStateOf(0) }
    val context = LocalContext.current
    val selectedMainColor = rememberSaveable {
        mutableStateOf(
            DialogColorUtil.getNearestColorGroup(
                context,
                color.value
            )
        )
    }
    val selectedAlpha = rememberSaveable { mutableFloatStateOf(color.value.alpha) }

    Dialog(state, title, titleStyle, icon, style, buttons, options, onEvent = onEvent) {

        val landscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

        BackHandler(enabled = selectedPresetsLevel.intValue != 0, onBack = {
            selectedPresetsLevel.intValue = 0
        })

        if (landscape) {
            Row {
               Column(
                   verticalArrangement = Arrangement.spacedBy(8.dp)
               ) {
                   TitleForPages(Modifier.weight(1f), texts, colorState, selectedSubColor, selectedPresetsLevel)
               }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                ) {
                    Content(color, colorState, selectedMainColor, selectedSubColor, selectedPresetsLevel, selectedAlpha, alphaSupported, shape, gridSize, labelStyle)
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TitleForPages(Modifier.weight(1f), texts, colorState, selectedSubColor, selectedPresetsLevel)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Content(color, colorState, selectedMainColor, selectedSubColor, selectedPresetsLevel, selectedAlpha, alphaSupported, shape, gridSize, labelStyle)
        }
    }
}

@Composable
private fun TitleForPages(
    modifier: Modifier,
    texts: DialogColorTexts,
    colorState: MutableState<DialogColorPage>,
    selectedSubColor: MutableState<Color?>,
    selectedPresetsLevel: MutableState<Int>
) {
    TextButton(
        modifier = modifier
            .alpha(if (colorState.value == DialogColorPage.Custom) .5f else 1f),
        onClick = {
            colorState.value = DialogColorPage.Presets
            selectedSubColor.value = null
            selectedPresetsLevel.value = 0
        }
    ) {
        Text(text = texts.presets)
    }
    TextButton(
        modifier = modifier
            .alpha(if (colorState.value == DialogColorPage.Presets) .5f else 1f),
        onClick = { colorState.value = DialogColorPage.Custom }
    ) {
        Text(text = texts.custom)
    }
}

@Composable
private fun Content(
    color: MutableState<Color>,
    colorState: MutableState<DialogColorPage>,
    selectedMainColor: MutableState<GroupedColor>,
    selectedSubColor: MutableState<Color?>,
    selectedPresetsLevel: MutableState<Int>,
    selectedAlpha: MutableState<Float>,
    alphaSupported: Boolean,
    shape: Shape,
    gridSize: Int,
    labelStyle: DialogColorLabelStyle
) {
    val context = LocalContext.current
    val checkerBoardPixelSize = with(LocalDensity.current) { 4.dp.toPx().toInt() }
    val updateSelectedPresetColors = {
        selectedMainColor.value = DialogColorUtil.getNearestColorGroup(context, color.value)
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
                DialogColorPage.Custom -> {
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
                                    DialogColorUtil.drawCheckerboard(
                                        this,
                                        checkerBoardPixelSize
                                    )
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
                                        text = "#" + Integer.toHexString(color.value.toArgb())
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
                DialogColorPage.Presets -> {
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
                                ColorDefinitions.COLORS.map { it.getColor(context) },
                                null,
                                selectedAlpha.value,
                                null,
                                gridSize,
                                shape,
                                space,
                                size,
                                checkerBoardPixelSize
                            ) {
                                selectedMainColor.value = ColorDefinitions.COLORS[it]
                                selectedPresetsLevel.value = 1
                            }
                        } else {
                            ColorGrid(
                                level,
                                selectedMainColor.value.colors.map { it.getColor(context) },
                                selectedMainColor.value.colors.map { it.label },
                                selectedAlpha.value,
                                selectedSubColor.value,
                                gridSize,
                                shape,
                                space,
                                size,
                                checkerBoardPixelSize
                            ) {
                                selectedSubColor.value =
                                    selectedMainColor.value.colors[it].getColor(context)
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

@Composable
fun rememberDialogColor(
    color: Color
): MutableState<Color> {
    return rememberSaveable(saver = DialogColor.ColorStateSaver) { mutableStateOf(color) }
}

@Composable
fun rememberDialogColorState(
    page: DialogColorPage = DialogColorPage.Presets
): MutableState<DialogColorPage> {
    return rememberSaveable { mutableStateOf(page) }
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
            DialogColorLabelStyle.Percent -> {
                Text(
                    modifier = Modifier.width(48.dp),
                    text = DF_PERCENTAGES.format(value * 100f) + "%",
                    textAlign = TextAlign.End
                )
            }
        }

    }
}

@Composable
private fun ColorGrid(
    level: Int,
    colors: List<Color>,
    labels: List<String>?,
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
                    val selected = selectedColor == color
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
                        if (color == Color.Black && level == 0) {
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
                            val label = labels?.getOrNull(index)
                            if (label != null && !selected) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = onColor
                                )
                            }
                        }
                        if (selected) {
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