package com.michaelflisar.composedialogs.core.internal

import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.Options
import com.michaelflisar.composedialogs.core.SpecialOptions
import com.michaelflisar.composedialogs.core.copied.AlertDialog
import com.michaelflisar.composedialogs.core.copied.DialogMaxWidth
import com.michaelflisar.composedialogs.core.copied.DialogMinWidth
import com.michaelflisar.composedialogs.core.views.ComposeDialogButton
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

// Wrapper around Dialog because of resizing issues after initial compose:
// Github: https://stackoverflow.com/questions/71285843/how-to-make-dialog-re-measure-when-a-child-size-changes-dynamically/71287474
// Issue: https://issuetracker.google.com/issues/221643630

internal val DialogHorizontalMinMargin = 16.dp
internal val DialogVerticalMinMargin = DialogHorizontalMinMargin

internal object ComposeAlertDialog {

    class Data(
        // Settings
        val swipeDismissable: Boolean,
        // DialogProperties
        val dialogProperties: DialogProperties,
        // Style
        val shape: Shape,
        val containerColor: Color,
        val iconContentColor: Color,
        val titleContentColor: Color,
        val textContentColor: Color,
        val tonalElevation: Dp
    )

    enum class DragValue { Start, Center, End }

}

@Composable
internal fun ComposeAlertDialog(
    data: ComposeAlertDialog.Data,
    title: (@Composable () -> Unit)?,
    icon: @Composable (() -> Unit)?,
    buttons: DialogButtons,
    options: Options,
    specialOptions: SpecialOptions,
    state: DialogState,
    onEvent: (event: DialogEvent) -> Unit,
    content: @Composable (ColumnScope.() -> Unit)
) {
    val modifierSwipeDismiss = getSwipeDismissModifier(state, onEvent, data.swipeDismissable)
    ComposeAlertDialog(
        data = data,
        onDismissRequest = {
            state.dismiss(onEvent)
        },
        confirmButton = {
            ComposeDialogButton(
                buttons.positive,
                DialogButtonType.Positive,
                options,
                state,
                requestDismiss = {
                    state.dismiss()
                },
                onEvent
            )
        },
        modifier = Modifier
            .wrapContentHeight()
            .then(modifierSwipeDismiss),
        dismissButton = if (buttons.negative.text.isNotEmpty()) {
            {
                ComposeDialogButton(
                    buttons.negative,
                    DialogButtonType.Negative,
                    options,
                    state,
                    requestDismiss = {
                        state.dismiss()
                    },
                    onEvent
                )
            }
        } else null,
        icon = icon,
        title = title,
        text = {
            val scrollModifier = if (options.wrapContentInScrollableContainer) {
                Modifier
                    .wrapContentHeight()
                    .verticalScroll(rememberScrollState())
            } else Modifier
            Column(
                modifier = Modifier
                    .then(scrollModifier)
                    .sizeIn(minWidth = DialogMinWidth, maxWidth = DialogMaxWidth)
                    .then(
                        if (specialOptions.dialogIntrinsicWidthMin) {
                            Modifier.width(IntrinsicSize.Min)
                        } else Modifier
                    ),
                //horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun getSwipeDismissModifier(
    state: DialogState,
    onEvent: (event: DialogEvent) -> Unit,
    swipeDismissable: Boolean
): Modifier {
    return if (swipeDismissable) {
        val density = LocalDensity.current

        val maxSwipeDistance = with(density) { 96.dp.toPx() }
        val dragState = createDraggableState(maxSwipeDistance)

        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(dragState.targetValue) {
            when (dragState.targetValue) {
                ComposeAlertDialog.DragValue.Center -> {
                    // ...
                }

                ComposeAlertDialog.DragValue.Start,
                ComposeAlertDialog.DragValue.End -> {
                    if (!state.dismiss(onEvent)) {
                        // expand again if dismissing is not allowed
                        coroutineScope.launch {
                            dragState.animateTo(ComposeAlertDialog.DragValue.Center)
                        }
                    }
                }
            }
        }

        val alpha = 1f - (0.3f * abs(dragState.requireOffset() / maxSwipeDistance))

        Modifier
            .offset {
                IntOffset(
                    0,
                    dragState
                        .requireOffset()
                        .roundToInt() / 4
                )
            }
            .anchoredDraggable(
                state = dragState,
                //anchors = anchors,
                //thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Vertical
            )
            .alpha(alpha)
    } else Modifier
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun createDraggableState(
    maxSwipeDistance: Float
): AnchoredDraggableState<ComposeAlertDialog.DragValue> {
    val anchors = DraggableAnchors {
        ComposeAlertDialog.DragValue.Start at -maxSwipeDistance
        ComposeAlertDialog.DragValue.Center at 0f
        ComposeAlertDialog.DragValue.End at maxSwipeDistance
    }

    val decayAnimSpec = rememberSplineBasedDecay<Float>()
    val dragState = remember {
        AnchoredDraggableState(
            initialValue = ComposeAlertDialog.DragValue.Center,
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

@Composable
private fun ComposeAlertDialog(
    data: ComposeAlertDialog.Data,
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable (() -> Unit)? = null,
    text: @Composable (() -> Unit)? = null
) {
    val properties = data.dialogProperties
    val (fixedProperties, fixedModifier, fixedModifierInner) = if (properties.usePlatformDefaultWidth) {
        Triple(
            DialogProperties(
                properties.dismissOnBackPress,
                properties.dismissOnClickOutside,
                false
            ),
            // outer modifier gets paddings only so that dismiss on click outside works as desired
            Modifier
                .sizeIn(minWidth = DialogMinWidth, maxWidth = DialogMaxWidth)
                //.background(Color.Red)
                .padding(
                    horizontal = DialogHorizontalMinMargin,
                    vertical = DialogVerticalMinMargin
                )
            //.background(Color.Green)
            ,
            Modifier
                .sizeIn(minWidth = DialogMinWidth, maxWidth = DialogMaxWidth)
            //.background(Color.Blue)
            //.padding(horizontal = DialogHorizontalMinMargin, vertical = DialogVerticalMinMargin)
            //.background(Color.Yellow)
        )
    } else {
        Triple(
            properties,
            Modifier,
            Modifier
        )
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        modifier = modifier
            .wrapContentHeight()
            .then(fixedModifier),
        modifierInner = modifier
            .wrapContentHeight()
            .then(fixedModifierInner),
        dismissButton = dismissButton,
        icon = icon,
        title = title,
        text = text,
        shape = data.shape,
        containerColor = data.containerColor,
        iconContentColor = data.iconContentColor,
        titleContentColor = data.titleContentColor,
        textContentColor = data.textContentColor,
        tonalElevation = data.tonalElevation,
        properties = fixedProperties
    )
}