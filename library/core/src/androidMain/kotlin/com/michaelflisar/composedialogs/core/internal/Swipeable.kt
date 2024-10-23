package com.michaelflisar.composedialogs.core.internal

import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.style.BottomSheetStyle

internal object Swipeable {

    enum class DragValue { Start, Center, End }
    enum class BottomSheetState { Collapsed, Peek, Expanded }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun createState(
        maxSwipeDistance: Float
    ): AnchoredDraggableState<DragValue> {
        val anchors = DraggableAnchors {
            DragValue.Start at -maxSwipeDistance
            DragValue.Center at 0f
            DragValue.End at maxSwipeDistance
        }

        val decayAnimSpec = rememberSplineBasedDecay<Float>()
        val dragState = remember {
            AnchoredDraggableState(
                initialValue = DragValue.Center,
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

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun createState(
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