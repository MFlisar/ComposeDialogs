package com.michaelflisar.composedialogs.core.style

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogOptions
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
    private val scrim: Boolean,
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
        dialogOptions: DialogOptions,
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
                confirm
            },
            velocityThreshold = velocityThreshold,
            positionalThreshold = positionalThreshold
        )
        val bottomSheetProperties = ModalSheetProperties(
            dismissOnBackPress = shouldDismissOnBackPress,
            dismissOnClickOutside = shouldDismissOnClickOutside
        )

        // TODO: workaround for bug https://github.com/composablehorizons/compose-unstyled/issues/48
        // workaround for close bug - not needed on windows!
        val wasShown = remember { mutableStateOf(false) }
        var buttonPressed2 = false
        val useBottomsheetWorkaround = false
        if (useBottomsheetWorkaround) {
            LaunchedEffect(
                bottomSheetState.progress == 1f,
                //bottomSheetState.isIdle,
                bottomSheetState.targetDetent,
                initialAnimationDone.value,
                wasShown.value
            ) {
                if (bottomSheetState.progress == 1f &&
                    initialAnimationDone.value &&
                    wasShown.value &&
                    //bottomSheetState.isIdle &&
                    bottomSheetState.targetDetent == SheetDetent.Hidden &&
                    state.interactionSource.dismissAllowed.value
                ) {
                    if (buttonPressed2)
                        state.dismiss()
                    else
                        state.dismiss(onEvent)
                }
            }
            LaunchedEffect(
                bottomSheetState.targetDetent
            ) {
                if (bottomSheetState.targetDetent != SheetDetent.Hidden) {
                    wasShown.value = true
                }
            }
        }

/*
        LaunchedEffect(
            bottomSheetState.progress == 0f,
            bottomSheetState.progress == 1f,
            bottomSheetState.currentDetent,
            bottomSheetState.targetDetent,
            bottomSheetState.isIdle,
            initialAnimationDone.value,
            wasShown.value
        ) {
            println("BOTTOMSHEET")
            println(" - progress = ${bottomSheetState.progress}")
            println(" - currentDetent = ${bottomSheetState.currentDetent.identifier}")
            println(" - targetDetent = ${bottomSheetState.targetDetent.identifier}")
            println(" - isIdle = ${bottomSheetState.isIdle}")
            println(" - initialAnimationDone = ${initialAnimationDone.value}")
            println(" - wasShown = ${wasShown.value}")

            if (bottomSheetState.targetDetent != SheetDetent.Hidden) {
                wasShown.value = true
            }
        }
*/

        val onDismiss = { buttonPressed: Boolean ->
            if (state.interactionSource.dismissAllowed.value) {
                coroutineScope.launch {
                    bottomSheetState.animateTo(SheetDetent.Hidden)
                    if (buttonPressed)
                        state.dismiss()
                    else
                        state.dismiss(onEvent)
                }
                true
            } else {
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

        ModalBottomSheet(
            state = bottomSheetState,
            properties = bottomSheetProperties,
            onDismiss = {
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

            if (scrim) {
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
            }
            Box(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .then(
                        if (isCompact) {
                            Modifier
                        } else Modifier.padding(horizontal = 56.dp)
                    )
                    .statusBarsPadding()
                    .padding(
                        WindowInsets.navigationBars
                            .only(WindowInsetsSides.Horizontal)
                            .asPaddingValues()
                    )
            ) {
                Sheet(
                    modifier = Modifier
                        .onSizeChanged {
                            contentSize.value =
                                with(density) { DpSize(it.width.toDp(), it.height.toDp()) }
                        }
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
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        shape = RoundedCornerShape(percent = 50)
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
                            modifier = Modifier.weight(weight = 1f, fill = false)
                                .padding(horizontal = 24.dp),
                            bottomPadding = dialogOptions.contentPadding(buttons)
                        )

                        // 4) Footer
                        Column(
                            modifier = Modifier
                                .then(
                                    if (buttons.enabled) {
                                        Modifier.offset {
                                            IntOffset(
                                                0,
                                                (contentSize.value.height.toPx() - bottomSheetState.offset).roundToInt() * -1 + 1 // +1 for rounding errors? otherwise it sometimes does show a pixel row below the bottom that is not overlayed by the buttons...
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
                                state = state,
                                dialogOptions = dialogOptions,
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
}