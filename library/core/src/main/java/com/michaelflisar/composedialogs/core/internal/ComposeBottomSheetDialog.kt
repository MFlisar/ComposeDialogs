package com.michaelflisar.composedialogs.core.internal

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeableState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.*
import com.michaelflisar.composedialogs.core.copied.AlertDialogFlowRow
import com.michaelflisar.composedialogs.core.copied.ButtonsCrossAxisSpacing
import com.michaelflisar.composedialogs.core.copied.ButtonsMainAxisSpacing
import com.michaelflisar.composedialogs.core.views.ComposeDialogButton
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

internal val BottomSheetMaxWidth = 640.dp
internal val BottomSheetTopPadding = 72.dp
internal val BottomSheetVerticalPaddingPadding = 56.dp

enum class BottomSheetState {
    Collapsed,
    Peek,
    Expanded
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ComposeBottomSheetDialog(
    title: String,
    titleStyle: DialogTitleStyle,
    icon: DialogIcon?,
    style: DialogStyle.BottomSheet,
    buttons: DialogButtons,
    options: Options,
    specialOptions: SpecialOptions,
    state: DialogState,
    onEvent: (event: DialogEvent) -> Unit,
    content: @Composable (ColumnScope.() -> Unit)
) {
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    // Settings/States
    val properties = style.dialogProperties
    val contentPadding = 16.dp
    val modifierContentPadding = Modifier.padding(
        start = contentPadding,
        end = contentPadding,
        top = contentPadding
    )

    var width by remember { mutableStateOf(0.dp) }
    val contentSize = remember { mutableStateOf(IntSize.Zero) }
    val horizontalPadding by remember {
        derivedStateOf { if (width > BottomSheetMaxWidth) BottomSheetVerticalPaddingPadding else 0.dp }
    }

    val buttonPressed = remember { mutableStateOf(false) }

    val swipeableState = rememberSwipeableState(initialValue = BottomSheetState.Expanded)
    val dismiss = {
        if (style.hideAnimated) {
            if (state.interactionSource.dismissAllowed.value) {
                coroutineScope.launch {
                    swipeableState.animateTo(BottomSheetState.Collapsed)
                }
                true
            } else false
        } else {
            if (buttonPressed.value)
                state.dismiss()
            else
                state.dismiss(onEvent)
        }
    }

    AlertDialog(
        onDismissRequest = {
            dismiss()
        },
        modifier = Modifier.fillMaxSize(),
        properties = properties
    ) {
        // Wrap content in Box to align surface at the bottom
        // + catch clicks above (behind) content
        // + show buttons as fixed footer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
        ) {
            val interactionSource = remember { MutableInteractionSource() }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = BottomSheetTopPadding)
                .fillMaxHeight(1f)
                .clickable(interactionSource = interactionSource, indication = null) {
                    if (properties.dismissOnClickOutside)
                        dismiss()
                }
            )
            val offsetState = remember { mutableStateOf(0) }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .sizeIn(maxWidth = BottomSheetMaxWidth)
                    .then(
                        getSwipeModifier(
                            swipeableState,
                            offsetState,
                            state,
                            contentSize,
                            style,
                            onEvent
                        )
                    )
                    .nestedScroll(rememberNestedScrollInteropConnection())
                    .onSizeChanged {
                        width = with(density) { it.width.toDp() }
                    }
                    .padding(horizontal = horizontalPadding),
                shape = MaterialTheme.shapes.extraLarge.copy(
                    bottomStart = CornerSize(0),
                    bottomEnd = CornerSize(0)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onSizeChanged { contentSize.value = it }
                        .then(modifierContentPadding),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    SheetHeader(title, titleStyle, icon, style, dismiss)
                    SheetContent(Modifier
                        .fillMaxWidth()
                        .weight(1f, false)
                        , offsetState, style, options, content
                    )
                    SheetFooter(offsetState, buttons, options, state, dismiss = {
                        buttonPressed.value = true
                        val dismissed = dismiss()
                        buttonPressed.value = false
                        dismissed
                    }, onEvent)
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.SheetHeader(
    title: String,
    titleStyle: DialogTitleStyle,
    icon: DialogIcon?,
    style: DialogStyle.BottomSheet,
    dismiss: () -> Boolean
) {
    // Drag Handle
    if (style.dragHandle) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .alpha(0.4f)
        ) {
            Spacer(
                modifier = Modifier
                    .padding(bottom = 22.dp)
                    .width(32.dp)
                    .height(4.dp)
                    .background(
                        MaterialTheme.colorScheme.onSurfaceVariant,
                        RoundedCornerShape(percent = 50)
                    )
                    .clickable {
                        dismiss()
                    }
            )
        }
    }
    // Icon + Title
    if (icon != null) {
        val modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .size(24.dp)
        when (icon) {
            is DialogIcon.Painter -> Image(modifier = modifier ,painter = icon.painter(), contentDescription = "")
            is DialogIcon.Vector -> Icon(modifier = modifier , imageVector = icon.icon, contentDescription = "", tint = icon.tint ?: MaterialTheme.colorScheme.onSurface)
        }
    }
    if (title.isNotEmpty()) {
        Text(
            modifier = if (icon != null) Modifier.align(Alignment.CenterHorizontally) else Modifier,
            text = title,
            style = titleStyle.style ?: MaterialTheme.typography.headlineSmall,
            fontWeight = titleStyle.fontWeight
        )
    }
    // Space
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun ColumnScope.SheetFooter(
    offsetState: MutableState<Int>,
    buttons: DialogButtons,
    options: Options,
    state: DialogState,
    dismiss: () -> Boolean,
    onEvent: (event: DialogEvent) -> Unit
) {
    // Buttons
    val backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(LocalAbsoluteTonalElevation.current)
    Row(modifier = Modifier
        .offset { IntOffset(0, -offsetState.value) }
        .background(backgroundColor)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        //CompositionLocalProvider(LocalContentColor provides buttonContentColor) {
        //val textStyle =  MaterialTheme.typography.fromToken(DialogTokens.ActionLabelTextFont)
        //ProvideTextStyle(value = textStyle) {
        AlertDialogFlowRow(
            mainAxisSpacing = ButtonsMainAxisSpacing,
            crossAxisSpacing = ButtonsCrossAxisSpacing
        ) {
            ComposeDialogButton(
                buttons.negative,
                DialogButtonType.Negative,
                options,
                state,
                requestDismiss = {
                    dismiss()
                },
                onEvent
            )
            ComposeDialogButton(
                buttons.positive,
                DialogButtonType.Positive,
                options,
                state,
                requestDismiss = {
                    dismiss()
                },
                onEvent
            )
        }
        //}
        //}
    }
}

@Composable
private fun SheetContent(
    modifier: Modifier,
    offsetState: MutableState<Int>,
    style: DialogStyle.BottomSheet,
    options: Options,
    content: @Composable (ColumnScope.() -> Unit)
) {
    val scrollModifier = if (options.wrapContentInScrollableContainer) {
        Modifier
            .wrapContentHeight()
            .verticalScroll(rememberScrollState())
    } else Modifier

    val density = LocalDensity.current

    //var contentHeight by remember {
    //    mutableStateOf(0.dp)
    //}
    val resizeByPaddingModifier = if (style.resizeContent) {
        Modifier.padding(bottom = with(density) { offsetState.value.toDp() })
        //Modifier.heightIn(max = contentHeight - with(LocalDensity.current) { offsetState.value.toDp() })
    } else Modifier

    // Content
    Column(
        modifier = modifier
            .then(resizeByPaddingModifier)
            .fillMaxWidth()
            .wrapContentHeight()
            //.onSizeChanged {
            //    contentHeight = with(density) { it.height.toDp() }
            //}
            .then(scrollModifier)
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun getSwipeModifier(
    swipeableState: SwipeableState<BottomSheetState>,
    offsetState: MutableState<Int>,
    state: DialogState,
    contentSize: MutableState<IntSize>,
    style: DialogStyle.BottomSheet,
    onEvent: (event: DialogEvent) -> Unit
): Modifier {

    val density = LocalDensity.current

    val coroutineScope = rememberCoroutineScope()
    val maxSwipeDistance by remember {
        derivedStateOf { (contentSize.value.height.takeIf { it > 0 } ?: 0f).toFloat() }
    }

    val peekHeight =
        (maxSwipeDistance - with(density) { style.peekHeight.toPx() }).coerceAtLeast(0f)
    val anchors = mapOf(
        0f to BottomSheetState.Expanded,
        peekHeight to BottomSheetState.Peek,
        maxSwipeDistance to BottomSheetState.Collapsed
    )

    val targetValue by remember {
        derivedStateOf { swipeableState.targetValue }
    }
    val collapsed by remember {
        derivedStateOf {
            swipeableState.offset.value == maxSwipeDistance && !swipeableState.isAnimationRunning && swipeableState.targetValue == BottomSheetState.Collapsed
        }
    }
    val offset by remember {
        derivedStateOf {
            swipeableState.offset.value.roundToInt()
                .coerceAtLeast(0) // no "overscroll" to upside
        }
    }
    offsetState.value = offset

    Log.d(
        "SWIPE",
        "maxSwipeDistance = $maxSwipeDistance | offset = ${swipeableState.offset.value} - $offset | targetValue = ${targetValue} | collapsed = ${collapsed} | animating: ${swipeableState.isAnimationRunning}"
    )

    if (collapsed && maxSwipeDistance != 0f) {
        if (!state.dismiss(onEvent)) {
            // expand again if dismissing is not allowed
            SideEffect {
                coroutineScope.launch {
                    swipeableState.animateTo(BottomSheetState.Expanded)
                }
            }
        }
    }
    if (maxSwipeDistance == 0f)
        return Modifier

    // we allow to switch between half and full expanded currently...
    //val enabled = state.interactionSource.dismissAllowed

    return Modifier
        //.clipToBounds()
        .offset { IntOffset(0, offset) }
        .swipeable(
            state = swipeableState,
            enabled = state.interactionSource.swipeAllowed.value,
            anchors = anchors,
            thresholds = { _, _ -> FractionalThreshold(0.5f) },
            orientation = Orientation.Vertical
        )
}