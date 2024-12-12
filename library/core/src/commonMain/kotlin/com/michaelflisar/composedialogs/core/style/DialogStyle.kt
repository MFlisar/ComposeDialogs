package com.michaelflisar.composedialogs.core.style

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.composables.core.Dialog
import com.composables.core.DialogPanel
import com.composables.core.DialogProperties
import com.composables.core.Scrim
import com.composables.core.rememberDialogState
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.Options
import com.michaelflisar.composedialogs.core.internal.ComposeDialogButtons
import com.michaelflisar.composedialogs.core.internal.ComposeDialogContent
import com.michaelflisar.composedialogs.core.internal.ComposeDialogTitle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

object DialogStyleDefaults {

    val shape: Shape
        @Composable get() = RoundedCornerShape(size = 28.dp)

    val containerColor: Color
        @Composable get() = MaterialTheme.colorScheme.surfaceContainerHigh

    val iconColor
        @Composable get() = MaterialTheme.colorScheme.secondary

    val titleColor
        @Composable get() = MaterialTheme.colorScheme.onSurface

    val contentColor
        @Composable get() = MaterialTheme.colorScheme.onSurface
}

private enum class DragValue { Start, Center, End }

internal class DialogStyle(
    private val swipeDismissable: Boolean,
    // DialogProperties
    private val dismissOnBackPress: Boolean,
    private val dismissOnClickOutside: Boolean,
    // Style
    private val shape: Shape,
    private val containerColor: Color,
    private val iconColor: Color,
    private val titleColor: Color,
    private val contentColor: Color
) : ComposeDialogStyle {

    override val type = ComposeDialogStyle.Type.Dialog

    @Composable
    override fun Show(
        title: (@Composable () -> Unit)?,
        icon: @Composable (() -> Unit)?,
        buttons: DialogButtons,
        options: Options,
        state: DialogState,
        onEvent: (event: DialogEvent) -> Unit,
        content: @Composable () -> Unit
    ) {
        val coroutineScope = rememberCoroutineScope()
        val dialogState = rememberDialogState(initiallyVisible = true)

        val animDurationEnter = 250
        val animDurationExit = 150
        val animEnter =
            scaleIn(initialScale = 0.8f) + fadeIn(tween(durationMillis = animDurationEnter))
        val animExit =
            scaleOut(targetScale = 0.6f) + fadeOut(tween(durationMillis = animDurationExit))

        val dismiss = { dialogState.visible = false }
        var buttonPressed = false
        val waitForDismissAnimationAndUpdateState = {
            coroutineScope.launch {
                delay(animDurationExit.toLong())
                if (buttonPressed)
                    state.dismiss()
                else
                    state.dismiss(onEvent)
            }
        }

        val shoudDismissOnBackPress by remember {
            derivedStateOf { dismissOnBackPress && state.interactionSource.dismissAllowed.value }
        }
        val shoudDismissOnClickOutside by remember {
            derivedStateOf { dismissOnClickOutside && state.interactionSource.dismissAllowed.value }
        }

        Dialog(
            state = dialogState,
            properties = DialogProperties(
                dismissOnBackPress = shoudDismissOnBackPress,
                dismissOnClickOutside = shoudDismissOnClickOutside
            ),
            onDismiss = {
                waitForDismissAnimationAndUpdateState()
            }
        ) {

            val contentSize = remember { mutableStateOf(DpSize.Zero) }
            val scrimSize = remember { mutableStateOf(DpSize.Zero) }
            //val isCompact by remember {
            //    derivedStateOf {
            //        scrimSize.value.width < 600.dp
            //    }
            //}

            val density = LocalDensity.current
            val modifierSwipeDismiss = getSwipeDismissModifier(swipeDismissable, with(density) { scrimSize.value.height.toPx() } / 2f) {
                if (state.interactionSource.dismissAllowed.value) {
                    waitForDismissAnimationAndUpdateState()
                    true
                } else false
            }

            Scrim(
                enter = fadeIn(),
                exit = fadeOut(),
                scrimColor = MaterialTheme.colorScheme.scrim.copy(0.3f),
                modifier = Modifier
                    .fillMaxSize()
                    .onSizeChanged {
                        scrimSize.value =
                            with(density) { DpSize(it.width.toDp(), it.height.toDp()) }
                    }
            )
            DialogPanel(
                modifier = Modifier
                    .onSizeChanged {
                        contentSize.value =
                            with(density) { DpSize(it.width.toDp(), it.height.toDp()) }
                    }
                    .systemBarsPadding()
                    .imePadding()
                    .padding(16.dp)
                    .then(modifierSwipeDismiss)
                    .shadow(6.dp /* L3 */, shape)
                    .clip(shape)
                    .background(containerColor)
                    .padding(24.dp)
                   ,
                enter = animEnter,
                exit = animExit
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(min = 280.dp, max = 560.dp)
                        .width(IntrinsicSize.Min)
                        .then(if (options.wrapContentInScrollableContainer) Modifier.verticalScroll(rememberScrollState()) else Modifier)
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Icon + Title
                    ComposeDialogTitle(title, icon, iconColor, titleColor)

                    // Content
                    ComposeDialogContent(content, contentColor)

                    // Buttons
                    ComposeDialogButtons(
                        buttons = buttons,
                        options = options,
                        state = state,
                        dismissOnButtonPressed = {
                            buttonPressed = true
                            dismiss()
                            waitForDismissAnimationAndUpdateState()
                        },
                        onEvent = onEvent
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun getSwipeDismissModifier(
    swipeDismissable: Boolean,
    maxSwipeDistance: Float,
    dismiss: () -> Boolean
): Modifier {
    return if (swipeDismissable && maxSwipeDistance > 0f) {

        val dragState = createDraggableState(maxSwipeDistance)
        val alpha = 1f - (0.3f * abs(dragState.requireOffset() / maxSwipeDistance))
        LaunchedEffect(dragState.targetValue) {
            when (dragState.targetValue) {
                DragValue.Center -> {
                    // ...
                }

                DragValue.Start,
                DragValue.End -> {
                    if (!dismiss()) {
                        dragState.snapTo(DragValue.Center)
                    }
                }
            }
        }

        Modifier
            .offset {
                IntOffset(
                    0,
                    dragState
                        .requireOffset()
                        .roundToInt()
                )
            }
            .anchoredDraggable(
                state = dragState,
                orientation = Orientation.Vertical
            )
            .alpha(alpha)
    } else Modifier
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun createDraggableState(
    maxSwipeDistance: Float
): AnchoredDraggableState<DragValue> {

    val anchors = remember(maxSwipeDistance) {
        DraggableAnchors {
            DragValue.Start at -maxSwipeDistance
            DragValue.Center at 0f
            DragValue.End at maxSwipeDistance
        }
    }

    val density = LocalDensity.current

    val decayAnimSpec = rememberSplineBasedDecay<Float>()
    val dragState = remember {
        AnchoredDraggableState(
            initialValue = DragValue.Center,
            anchors = anchors,
            positionalThreshold = { distance: Float -> distance * .5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            snapAnimationSpec = tween(),
            decayAnimationSpec = decayAnimSpec,
            confirmValueChange = { true }
        )
    }

    return dragState
}