package com.michaelflisar.composedialogs.demo.composables

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun <S> Transition<S>.animateFloatTween(
    label: String,
    targetValueByState: @Composable (state: S) -> Float
): State<Float> {
    return animateFloat(
        transitionSpec = {
            tween()
        },
        label = label,
        targetValueByState = targetValueByState
    )
}

@Composable
fun <S> Transition<S>.animateIntTween(
    label: String,
    targetValueByState: @Composable (state: S) -> Int
): State<Int> {
    return animateInt(
        transitionSpec = {
            tween()
        },
        label = label,
        targetValueByState = targetValueByState
    )
}

@Composable
fun <S> Transition<S>.animateColorTween(
    label: String,
    targetValueByState: @Composable (state: S) -> Color
): State<Color> {
    return animateColor(
        transitionSpec = {
            tween()
        },
        label = label,
        targetValueByState = targetValueByState
    )
}

@Composable
fun <S> Transition<S>.animateDpTween(
    label: String,
    targetValueByState: @Composable (state: S) -> Dp
): State<Dp> {
    return animateDp(
        transitionSpec = {
            tween()
        },
        label = label,
        targetValueByState = targetValueByState
    )
}