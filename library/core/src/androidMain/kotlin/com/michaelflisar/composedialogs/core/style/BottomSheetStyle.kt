package com.michaelflisar.composedialogs.core.style

import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.Options
import com.michaelflisar.composedialogs.core.SpecialOptions
import com.michaelflisar.composedialogs.core.copied.AlertDialogFlowRow
import com.michaelflisar.composedialogs.core.copied.ButtonsCrossAxisSpacing
import com.michaelflisar.composedialogs.core.copied.ButtonsMainAxisSpacing
import com.michaelflisar.composedialogs.core.views.ComposeDialogButton
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

internal class BottomSheetStyle(
    val dragHandle: Boolean,
    val hideAnimated: Boolean,
    val resizeContent: Boolean,
    val peekHeight: Dp,
    // DialogProperties
    dismissOnBackPress: Boolean,
    dismissOnClickOutside: Boolean,
    securePolicy: SecureFlagPolicy,
    //usePlatformDefaultWidth: Boolean,
    decorFitsSystemWindows: Boolean,
) : ComposeDialogStyle {

    override val type = ComposeDialogStyle.Type.BottomSheet

    private val BottomSheetMaxWidth = 640.dp
    private val BottomSheetTopPadding = 72.dp
    private val BottomSheetVerticalPaddingPadding = 56.dp

    internal val dialogProperties = DialogProperties(
        dismissOnBackPress,
        dismissOnClickOutside,
        securePolicy,
        false,//usePlatformDefaultWidth,
        decorFitsSystemWindows
    )

    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Show(
        title: (@Composable () -> Unit)?,
        icon: @Composable (() -> Unit)?,
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
        val properties = dialogProperties
        val contentPadding = 16.dp
        val modifierContentPadding = Modifier.padding(
            start = contentPadding,
            end = contentPadding,
            top = contentPadding
        )

        var width by remember { mutableStateOf(0.dp) }
        val contentSize = remember { mutableStateOf(IntSize.Zero) }
        val footerSize = remember { mutableStateOf(IntSize.Zero) }
        val horizontalPadding by remember {
            derivedStateOf { if (width > BottomSheetMaxWidth) BottomSheetVerticalPaddingPadding else 0.dp }
        }

        val buttonPressed = remember { mutableStateOf(false) }

        val dragState = createDraggableState(this, contentSize)
        val dismiss = {
            if (hideAnimated) {
                if (state.interactionSource.dismissAllowed.value) {
                    coroutineScope.launch {
                        dragState.animateTo(BottomSheetState.Collapsed)
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

        BasicAlertDialog(
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
                val offsetState = remember { mutableIntStateOf(0) }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .sizeIn(maxWidth = BottomSheetMaxWidth)
                        .then(
                            anchoredDraggable(
                                dragState,
                                offsetState,
                                state,
                                contentSize,
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(modifierContentPadding),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Column(
                            modifier = Modifier
                                .onSizeChanged { contentSize.value = it }
                                .padding(bottom = with(density) { footerSize.value.height.toDp() })
                                .offset { IntOffset(0, dragState.offset.roundToInt()) }
                        ) {
                            SheetHeader(title, icon, dismiss)
                            SheetContent(
                                Modifier
                                    .fillMaxWidth()
                                    .weight(1f, false),
                                offsetState,
                                options,
                                content
                            )
                        }
                        SheetFooter(
                            Modifier
                                .onSizeChanged { footerSize.value = it }
                            //.offset { IntOffset(0, -offsetState.intValue) }
                            ,
                            buttons,
                            options,
                            state,
                            dismiss = {
                                buttonPressed.value = true
                                val dismissed = dismiss()
                                buttonPressed.value = false
                                dismissed
                            },
                            onEvent
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun ColumnScope.SheetHeader(
        title: (@Composable () -> Unit)? = null,
        icon: (@Composable () -> Unit)? = null,
        dismiss: () -> Boolean
    ) {
        // Drag Handle
        if (dragHandle) {
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
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                //.size(24.dp)
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colorScheme.onSurface
                ) {
                    icon()
                }

            }
        }
        if (title != null) {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.headlineSmall
            ) {
                Box(
                    modifier = if (icon != null) Modifier.align(Alignment.CenterHorizontally) else Modifier
                ) {
                    title()
                }
            }
        }
        // Space
        Spacer(modifier = Modifier.height(8.dp))
    }

    @Composable
    private fun SheetFooter(
        modifier: Modifier,
        buttons: DialogButtons,
        options: Options,
        state: DialogState,
        dismiss: () -> Boolean,
        onEvent: (event: DialogEvent) -> Unit
    ) {
        // Buttons
        val backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
            LocalAbsoluteTonalElevation.current
        )
        Row(
            modifier = modifier
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
        val resizeByPaddingModifier = if (resizeContent) {
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

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun anchoredDraggable(
        dragState: AnchoredDraggableState<BottomSheetState>,
        offsetState: MutableState<Int>,
        state: DialogState,
        contentSize: MutableState<IntSize>,
        onEvent: (event: DialogEvent) -> Unit
    ): Modifier {

        val maxSwipeDistance by remember {
            derivedStateOf { (contentSize.value.height.takeIf { it > 0 } ?: 0f).toFloat() }
        }

        val collapsed =
            dragState.offset == maxSwipeDistance && !dragState.isAnimationRunning && dragState.targetValue == BottomSheetState.Collapsed

        LaunchedEffect(dragState.offset.roundToInt()) {
            offsetState.value = dragState.offset.roundToInt()
        }

        println("maxSwipeDistance = $maxSwipeDistance | offset = ${dragState.offset} | targetValue = ${dragState.targetValue} | collapsed = ${collapsed} | animating: ${dragState.isAnimationRunning}")

        if (maxSwipeDistance == 0f)
            return Modifier

        LaunchedEffect(collapsed) {
            if (collapsed && !state.dismiss(onEvent)) {
                // expand again if dismissing is not allowed
                dragState.animateTo(BottomSheetState.Expanded)
            }
        }

        return Modifier
            //.clipToBounds()

            .anchoredDraggable(
                state = dragState,
                enabled = state.interactionSource.swipeAllowed.value,
                orientation = Orientation.Vertical
            )
    }

    enum class BottomSheetState { Collapsed, Peek, Expanded }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun createDraggableState(
        style: BottomSheetStyle,
        contentSize: MutableState<IntSize>
    ): AnchoredDraggableState<BottomSheetState> {
        val density = LocalDensity.current

        val maxSwipeDistance by remember(contentSize.value) {
            derivedStateOf { (contentSize.value.height.takeIf { it > 0 } ?: 0f).toFloat() }
        }
        val peekHeight =
            (maxSwipeDistance - with(density) { style.peekHeight.toPx() }).coerceAtLeast(0f)


        val anchors = DraggableAnchors {
            BottomSheetState.Collapsed at maxSwipeDistance
            BottomSheetState.Peek at peekHeight
            BottomSheetState.Expanded at 0f
        }

        val decayAnimSpec = rememberSplineBasedDecay<Float>()
        val dragState = remember(maxSwipeDistance) {
            AnchoredDraggableState(
                initialValue = BottomSheetState.Expanded,
                anchors = anchors,
                positionalThreshold = { distance: Float -> distance * 0.5f },
                velocityThreshold = { maxSwipeDistance },
                snapAnimationSpec = tween(),
                decayAnimationSpec = decayAnimSpec,
                confirmValueChange = { true }
            )
        }
        return dragState
    }
}