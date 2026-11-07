package com.michaelflisar.composedialogs.core.style

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionOnScreen
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.composeunstyled.DragIndication
import com.composeunstyled.ModalBottomSheetProperties
import com.composeunstyled.Scrim
import com.composeunstyled.Sheet
import com.composeunstyled.SheetDetent
import com.composeunstyled.UnstyledModalBottomSheet
import com.composeunstyled.rememberModalBottomSheetState
import com.michaelflisar.composedialogs.core.BaseDialogState
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogOptions
import com.michaelflisar.composedialogs.core.StyleOptions
import com.michaelflisar.composedialogs.core.composables.ComposeDialogButtons
import com.michaelflisar.composedialogs.core.composables.ComposeDialogContent
import com.michaelflisar.composedialogs.core.composables.ComposeDialogTitle
import com.michaelflisar.composedialogs.core.UpdateNavigationbarColor
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

object BottomSheetStyleDefaults {

    val peekHeight: (containerHeight: Dp, sheetHeight: Dp) -> Dp =
        { containerHeight, sheetHeight -> (containerHeight * .5f).coerceAtMost(sheetHeight) }

    val topCornerSize: Dp
        @Composable get() = 28.dp

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
    private val topCornerSize: Dp,
    private val containerColor: Color,
    private val iconColor: Color,
    private val titleColor: Color,
    private val contentColor: Color,
) : ComposeDialogStyle {

    override val type = ComposeDialogStyle.Type.BottomSheet

    @Composable
    override fun Show(
        title: (@Composable () -> Unit)?,
        icon: @Composable (() -> Unit)?,
        buttons: DialogButtons,
        options: DialogOptions,
        state: BaseDialogState,
        onEvent: (event: DialogEvent) -> Unit,
        content: @Composable () -> Unit,
    ) {
        val coroutineScope = rememberCoroutineScope()

        val spacing = spacing()

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
        val bottomSheetProperties = ModalBottomSheetProperties(
            dismissOnBackPress = shouldDismissOnBackPress,
            dismissOnClickOutside = shouldDismissOnClickOutside,
            offsetForIme = true
        )

        var buttonPressed = false
        //var dismissed = false
        val onDismiss = {
            if (state.interactionSource.dismissAllowed.value) {
                coroutineScope.launch {
                    //println("BOTTOMSHEET - dismissed - animateTo...")
                    //dismissed = true
                    bottomSheetState.animateTo(SheetDetent.Hidden)
                    if (buttonPressed)
                        state.dismiss()
                    else
                        state.dismiss(onEvent)
                    //println("BOTTOMSHEET - dismissed - state.visible = ${state.visible}")
                }
                true
            } else {
                bottomSheetState.targetDetent = SheetDetent.FullyExpanded
                false
            }
        }

        val density = LocalDensity.current

        // Hack, to detect hidden state after animation because animateTo does work reliably
        LaunchedEffect(bottomSheetState.currentDetent) {
            if (initialAnimationDone.value &&
                bottomSheetState.currentDetent == SheetDetent.Hidden &&
                bottomSheetState.targetDetent == SheetDetent.Hidden
            ) {
                //println("BOTTOMSHEET - Detent is hidden => calling onDismiss...")
                onDismiss()
            }
        }

        //LaunchedEffect(bottomSheetState.isIdle) {
        //    println("BOTTOMSHEET - TEST - isIdle = ${bottomSheetState.isIdle} | target = ${bottomSheetState.currentDetent.identifier} | interaction allowed = ${state.interactionSource.dismissAllowed.value}")
        //    if (bottomSheetState.isIdle &&
        //        bottomSheetState.currentDetent == SheetDetent.Hidden &&
        //        !state.interactionSource.dismissAllowed.value
        //    ) {
        //        println("BOTTOMSHEET - TEST - show again")
        //        bottomSheetState.currentDetent = SheetDetent.FullyExpanded
        //    }
        //}

        LaunchedEffect(initialAnimationDone.value) {
            if (!initialAnimationDone.value) {
                bottomSheetState.targetDetent = initialDetent
                initialAnimationDone.value = true
            }
        }

        val scrimSize = remember { mutableStateOf(DpSize.Zero) }

        //LaunchedEffect(bottomSheetState.offset) {
        //    println("BOTTOMSHEET - offset changed: ${bottomSheetState.offset}")
        //}

        UnstyledModalBottomSheet(
            state = bottomSheetState,
            properties = bottomSheetProperties,
            onDismiss = {
                onDismiss()
            },
            overlay = {
                if (scrim) {
                    Scrim(
                        enter = fadeIn(),
                        exit = fadeOut(),
                        scrimColor = MaterialTheme.colorScheme.scrim.copy(0.3f),
                        modifier = Modifier
                            .fillMaxSize()
                            .onSizeChanged {
                                scrimSize.value =
                                    with(density) {
                                        DpSize(
                                            it.width.toDp(),
                                            it.height.toDp()
                                        )
                                    }
                            }
                    )
                }
            }
        ) {
            val bottomOfDragHandle = remember { mutableStateOf(0.dp) }
            val topOfButtons = remember { mutableStateOf(0.dp) }
            val buttonsOffset = remember { mutableStateOf(0.dp) }

            LaunchedEffect(bottomOfDragHandle.value, topOfButtons.value) {
                val offsetForButtons = if (bottomOfDragHandle.value > topOfButtons.value) {
                    bottomOfDragHandle.value - topOfButtons.value
                } else {
                    0.dp
                }
                //println("BOTTOMSHEET - bottomOfDragHandle: ${bottomOfDragHandle.value} | topOfButtons: ${topOfButtons.value} | offsetForButtons: $offsetForButtons")
                buttonsOffset.value = offsetForButtons
            }

            val buttonsAreVisible = remember {
                derivedStateOf {
                    bottomSheetState.currentDetent != SheetDetent.Hidden &&
                    (bottomOfDragHandle.value == 0.dp || topOfButtons.value == 0.dp || bottomOfDragHandle.value > topOfButtons.value)
                }
            }

            if (buttonsAreVisible.value)
                UpdateNavigationbarColor(contentColor.luminance() > .5f)

            Box(
                modifier = Modifier
            ) {
                val footerSize = remember { mutableStateOf(DpSize.Zero) }

                val dragHandleHeight = 4.dp
                val dragHandleVerticalPadding = 22.dp

                val isCompact by remember {
                    derivedStateOf {
                        scrimSize.value.width < 640.dp
                    }
                }

                // 1) Box for Sheet (without Buttons!)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (isCompact) {
                                Modifier
                            } else Modifier.padding(horizontal = 56.dp)
                        )
                        //.statusBarsPadding()
                        .padding(
                            WindowInsets.navigationBars
                                .only(WindowInsetsSides.Horizontal)
                                .asPaddingValues()
                        ),
                    contentAlignment = Alignment.TopCenter,
                )
                {
                    val shape = RoundedCornerShape(
                        topStart = topCornerSize,
                        topEnd = topCornerSize,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                    Sheet(
                        modifier = Modifier
                            .statusBarsPadding()
                            .shadow(4.dp, shape)
                            .clip(shape)
                            .background(containerColor)
                            .widthIn(max = 640.dp)
                            .fillMaxWidth()
                    ) {

                        Column(
                            Modifier
                                .fillMaxWidth()

                        ) {

                            // 1) Drag Header On Top
                            if (dragHandle) {
                                DragIndication(
                                    modifier = Modifier
                                        .onGloballyPositioned {
                                            val position = it.positionOnScreen()
                                            bottomOfDragHandle.value =
                                                with(density) { (position.y + it.size.height).toDp() }
                                            //println("BOTTOMSHEET - drag handle position: $position")
                                        }
                                        .align(Alignment.CenterHorizontally)
                                        .padding(vertical = dragHandleVerticalPadding)
                                        .background(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            shape = RoundedCornerShape(percent = 50)
                                        )
                                        .width(32.dp)
                                        .height(dragHandleHeight)
                                )
                            } else {
                                Spacer(
                                    modifier = Modifier
                                        .height(topCornerSize)
                                        .then(
                                            if (!dragHandle) {
                                                Modifier.onGloballyPositioned {
                                                    val position = it.positionOnScreen()
                                                    bottomOfDragHandle.value =
                                                        with(density) { (position.y + it.size.height).toDp() }
                                                }
                                            } else Modifier
                                        )
                                )
                            }

                            // 2) Icon + Title
                            ComposeDialogTitle(
                                modifier = Modifier
                                    .padding(horizontal = 24.dp),
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
                                bottomPadding = spacing.contentPadding(buttons)
                            )

                            // 4) Footer
                            Spacer(
                                modifier = Modifier.height(footerSize.value.height)
                            )
                        }
                    }
                }

                // 2) Box for Buttons (overlays the Sheet, but is needed to have the buttons always visible when the sheet is dragged to the bottom)
                val boxSize = remember { mutableStateOf(DpSize.Zero) }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (isCompact) {
                                Modifier
                            } else Modifier.padding(horizontal = 56.dp)
                        )
                        .padding(
                            WindowInsets.navigationBars
                                .only(WindowInsetsSides.Horizontal)
                                .asPaddingValues()
                        )
                        .fillMaxHeight()
                        .onSizeChanged {
                            boxSize.value =
                                with(density) { DpSize(it.width.toDp(), it.height.toDp()) }
                        }
                        .offset {
                            IntOffset(
                                0,
                                (boxSize.value.height - with(density) { bottomSheetState.offset.toDp() }).toPx()
                                    .roundToInt() * -1
                                        + with(density) { buttonsOffset.value.toPx().roundToInt() }
                            )
                        },
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    Column(
                        modifier = Modifier
                            .onGloballyPositioned {
                                val position = it.positionOnScreen()
                                // top of buttons is only calculated once!
                                if (topOfButtons.value == 0.dp)
                                    topOfButtons.value = with(density) { position.y.toDp() }
                            }
                            .onSizeChanged {
                                footerSize.value =
                                    with(density) { DpSize(it.width.toDp(), it.height.toDp()) }
                            }
                    ) {
                        Footer(
                            buttons = buttons,
                            state = state,
                            options = options,
                            containerColor = containerColor,
                            onDismiss = onDismiss,
                            setButtonPressed = { buttonPressed = it },
                            onEvent = onEvent
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.Footer(
    buttons: DialogButtons,
    state: BaseDialogState,
    options: DialogOptions,
    containerColor: Color,
    onDismiss: () -> Boolean,
    setButtonPressed: (value: Boolean) -> Unit,
    onEvent: (event: DialogEvent) -> Unit,
) {
    ComposeDialogButtons(
        modifier = Modifier
            .fillMaxWidth()
            .background(containerColor)
            .padding(horizontal = 24.dp),
        buttons = buttons,
        state = state,
        options = options,
        dismissOnButtonPressed = {
            setButtonPressed(true)
            onDismiss()
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