package com.michaelflisar.composedialogs.core.style

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.composables.core.DragIndication
import com.composables.core.ModalBottomSheet
import com.composables.core.ModalSheetProperties
import com.composables.core.Scrim
import com.composables.core.Sheet
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.Options
import com.michaelflisar.composedialogs.core.StyleOptions
import com.michaelflisar.composedialogs.core.internal.ComposeDialogButtons
import com.michaelflisar.composedialogs.core.internal.ComposeDialogContent
import com.michaelflisar.composedialogs.core.internal.ComposeDialogTitle
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

object BottomSheetStyleDefaults {

    val peekHeight: (containerHeight: Dp, sheetHeight: Dp) -> Dp =
        { containerHeight, sheetHeight -> containerHeight * .5f }

    val shape: Shape
        @Composable get() = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)

    val containerColor: Color
        @Composable get() = MaterialTheme.colorScheme.surface

    val iconColor
        @Composable get() = MaterialTheme.colorScheme.secondary

    val titleColor
        @Composable get() = MaterialTheme.colorScheme.onSurface

    val contentColor
        @Composable get() = MaterialTheme.colorScheme.onSurface
}

internal class BottomSheetStyle(
    private val dragHandle: Boolean,
    private val peekHeight: ((containerHeight: Dp, sheetHeight: Dp) -> Dp)?,
    private val expandInitially: Boolean,
    private val velocityThreshold: () -> Dp,
    private val positionalThreshold: (totalDistance: Dp) -> Dp,
    private val animateShow: Boolean = false,
    // DialogProperties
    private val dismissOnBackPress: Boolean,
    private val dismissOnClickOutside: Boolean,
    // Style
    private val options: StyleOptions = StyleOptions(),
    private val shape: Shape,
    private val containerColor: Color,
    private val iconColor: Color,
    private val titleColor: Color,
    private val contentColor: Color
) : ComposeDialogStyle {

    override val type = ComposeDialogStyle.Type.BottomSheet

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

        val peek: SheetDetent? = peekHeight?.let {
            SheetDetent("peek", it)
        }

        val shouldDismissOnBackPress by remember {
            derivedStateOf { dismissOnBackPress && state.interactionSource.dismissAllowed.value }
        }
        val shouldDismissOnClickOutside by remember {
            derivedStateOf { dismissOnClickOutside && state.interactionSource.dismissAllowed.value }
        }

        val initialAnimationDone = remember { mutableStateOf(false) }
        val initialDetent = if (expandInitially || peek == null) SheetDetent.FullyExpanded else peek

        val bottomSheetState = rememberModalBottomSheetState(
            initialDetent = if (animateShow && !initialAnimationDone.value) SheetDetent.Hidden else initialDetent,
            detents = listOfNotNull(SheetDetent.Hidden, peek, SheetDetent.FullyExpanded),
            confirmDetentChange = {
                val confirm = if (it == SheetDetent.Hidden) {
                    state.interactionSource.dismissAllowed.value
                } else true
                println("TEST - confirmDetentChange = $confirm | ${it.identifier}")
                confirm
            },
            velocityThreshold = velocityThreshold,
            positionalThreshold = positionalThreshold
        )
        val bottomSheetProperties = ModalSheetProperties(
            dismissOnBackPress = shouldDismissOnBackPress,
            dismissOnClickOutside = shouldDismissOnClickOutside
        )

        val onDismiss = { buttonPressed: Boolean ->
            println("TEST - onDismiss - 1")
            if (state.interactionSource.dismissAllowed.value) {
                coroutineScope.launch {
                    bottomSheetState.animateTo(SheetDetent.Hidden)
                    println("TEST - onDismiss - 2")
                    if (buttonPressed)
                        state.dismiss()
                    else
                        state.dismiss(onEvent)
                    println("TEST - onDismiss - 3")
                }
                true
            } else {
                println("TEST - show again from onDismiss")
                bottomSheetState.currentDetent = SheetDetent.FullyExpanded
                false
            }
        }

        val density = LocalDensity.current

        //LaunchedEffect(bottomSheetState.isIdle) {
        //    println("TEST - isIdle = ${bottomSheetState.isIdle} | target = ${bottomSheetState.currentDetent.identifier} | interaction allowed = ${state.interactionSource.dismissAllowed.value}")
        //    if (bottomSheetState.isIdle &&
        //        bottomSheetState.currentDetent == SheetDetent.Hidden &&
        //        !state.interactionSource.dismissAllowed.value
        //    ) {
        //        println("TEST - show again")
        //        bottomSheetState.currentDetent = SheetDetent.FullyExpanded
        //    }
        //}

        LaunchedEffect(initialAnimationDone.value) {
            if (!initialAnimationDone.value) {
                bottomSheetState.currentDetent = initialDetent
                initialAnimationDone.value = true
            }
        }

        // TODO: workaround for bug https://github.com/composablehorizons/compose-unstyled/issues/48
        var buttonPressed2 = false
        LaunchedEffect(bottomSheetState.progress == 1f) {
            if (!state.interactionSource.dismissAllowed.value) {
                if (buttonPressed2)
                    state.dismiss()
                else
                    state.dismiss(onEvent)
            }
        }

        ModalBottomSheet(
            state = bottomSheetState,
            properties = bottomSheetProperties,
            onDismiss = {
                println("TEST - onDismiss callback...")
                onDismiss(false)
            }
        ) {

            val contentSize = remember { mutableStateOf(DpSize.Zero) }
            val scrimSize = remember { mutableStateOf(DpSize.Zero) }
            val isCompact by remember {
                derivedStateOf {
                    scrimSize.value.width < 640.dp
                }
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
            Sheet(
                modifier = Modifier
                    .onSizeChanged {
                        contentSize.value =
                            with(density) { DpSize(it.width.toDp(), it.height.toDp()) }
                    }
                    .padding(top = 12.dp)
                    .let { if (isCompact) it else it.padding(horizontal = 56.dp) }
                    .statusBarsPadding()
                    .padding(
                        WindowInsets.navigationBars
                            .only(WindowInsetsSides.Horizontal)
                            .asPaddingValues()
                    )
                    .shadow(4.dp, shape)
                    .clip(shape)
                    .background(containerColor)
                    .widthIn(max = 640.dp)
                    .fillMaxWidth()
                    .imePadding()
            ) {

                Column(
                    Modifier
                        .fillMaxWidth()

                ) {

                    // 1) Drag Header On Top
                    if (dragHandle) {
                        DragIndication(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(vertical = 22.dp)
                                .background(
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                    RoundedCornerShape(percent = 50)
                                )
                                .width(32.dp)
                                .height(4.dp)
                        )
                    }

                    // 2) Icon + Title
                    ComposeDialogTitle(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        title = title,
                        icon = icon,
                        iconColor = iconColor,
                        titleColor = titleColor,
                        options = this@BottomSheetStyle.options,
                    )

                    // 3) Content
                    ComposeDialogContent(
                        content = content,
                        contentColor = contentColor,
                        modifier = Modifier.weight(weight = 1f, fill = false).padding(horizontal = 24.dp)
                    )

                    // 4) Footer
                    Column(
                        modifier = Modifier
                            .then(
                                if (buttons.enabled) {
                                    Modifier.offset {
                                        IntOffset(
                                            0,
                                            (contentSize.value.height.roundToPx() - bottomSheetState.offset).roundToInt() * -1
                                        )
                                    }
                                } else Modifier
                            )
                    ) {
                        ComposeDialogButtons(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(containerColor)
                                .padding(horizontal = 24.dp),
                            buttons = buttons,
                            options = options,
                            state = state,
                            dismissOnButtonPressed = {
                                buttonPressed2 = true
                                onDismiss(true)
                            },
                            onEvent = onEvent
                        )

                        // 5) Spacer behind nav bar
                        Spacer(
                            Modifier
                                .fillMaxWidth()
                                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                                .background(containerColor)
                        )
                    }
                }
            }
        }
    }
}