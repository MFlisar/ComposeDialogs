package com.michaelflisar.composedialogs.core.internal

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.michaelflisar.composedialogs.core.*
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

@Composable
fun ComposeAlertDialog(
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: DialogStyle.Dialog,
    buttons: DialogButtons,
    options: Options,
    specialOptions: SpecialOptions,
    state: DialogState,
    onEvent: (event: DialogEvent) -> Unit,
    content: @Composable (ColumnScope.() -> Unit)
) {
    val modifierSwipeDismiss = getSwipeDismissModifier(style, state, onEvent)
    ComposeAlertDialog(
        style,
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
        Modifier
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
            val scrollModifier =  if (options.wrapContentInScrollableContainer) {
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
                            Modifier .width(IntrinsicSize.Min)
                        } else Modifier
                    ),
                //horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun getSwipeDismissModifier(
    style: DialogStyle.Dialog,
    state: DialogState,
    onEvent: (event: DialogEvent) -> Unit
): Modifier {
    return if (style.swipeDismissable) {
        val swipeableState = rememberSwipeableState(initialValue = 0)
        val maxSwipeDistance = with(LocalDensity.current) { 96.dp.toPx().toFloat() }
        val anchors = mapOf(
            -maxSwipeDistance to -1,
            0f to 0,
            maxSwipeDistance to 1
        )

        val coroutineScope = rememberCoroutineScope()

        if (swipeableState.isAnimationRunning && swipeableState.targetValue != 0) {
            if (!state.dismiss(onEvent)) {
                // expand again if dismissing is not allowed
                SideEffect {
                    coroutineScope.launch {
                        swipeableState.animateTo(0)
                    }
                }
            }
        }

        val alpha = 1f - (0.3f * abs(swipeableState.offset.value / maxSwipeDistance))

        Modifier
            .offset { IntOffset(0, swipeableState.offset.value.roundToInt() / 4) }
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Vertical
            )
            .alpha(alpha)
    } else Modifier
}

@Composable
internal fun ComposeAlertDialog(
    style: DialogStyle.Dialog,
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable (() -> Unit)? = null,
    text: @Composable (() -> Unit)? = null
) {
    val properties = style.dialogProperties
    val (fixedProperties, fixedModifier, fixedModifierInner) = if (properties.usePlatformDefaultWidth) {
        Triple(
            DialogProperties(
                properties.dismissOnBackPress,
                properties.dismissOnClickOutside,
                properties.securePolicy,
                false,
                properties.decorFitsSystemWindows
            ),
            // outer modifier gets paddings only so that dismiss on click outside works as desired
            Modifier
                .sizeIn(minWidth = DialogMinWidth, maxWidth = DialogMaxWidth)
                //.background(Color.Red)
                .padding(horizontal = DialogHorizontalMinMargin, vertical = DialogVerticalMinMargin)
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

    com.michaelflisar.composedialogs.core.copied.AlertDialog(
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
        shape = style.shape,
        containerColor = style.containerColor,
        iconContentColor = style.iconContentColor,
        titleContentColor = style.titleContentColor,
        textContentColor = style.textContentColor,
        tonalElevation = style.tonalElevation,
        properties = fixedProperties
    )
}