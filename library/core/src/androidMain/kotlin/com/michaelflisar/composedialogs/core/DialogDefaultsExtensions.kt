package com.michaelflisar.composedialogs.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.SecureFlagPolicy
import com.michaelflisar.composedialogs.core.style.BottomSheetStyle
import com.michaelflisar.composedialogs.core.style.ComposeDialogStyle


private val SheetPeekHeight =
    56.dp * 2 // we want peek height for content + fixed buttons so with take 56*2

/**
 * the setup of a dialog that shows as a normal dialog popup
 *
 * @param dragHandle if true, a drag handle will be shown
 * @param hideAnimated if true, hiding the bottom sheet will be animated
 * @param resizeContent if true, the content of the bottom sheet will be resized
 * @param peekHeight the peek height of the bottom sheet
 * @param dismissOnBackPress if true, the dialog can be dismissed by a back press
 * @param dismissOnClickOutside if true, the dialog can be dismissed by clicking outside of its borders
 */
@Composable
fun DialogDefaults.styleBottomSheet(
    dragHandle: Boolean = true,
    hideAnimated: Boolean = false,
    resizeContent: Boolean = false,
    peekHeight: Dp = SheetPeekHeight,
    // DialogProperties
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    securePolicy: SecureFlagPolicy = SecureFlagPolicy.Inherit,
    //usePlatformDefaultWidth: Boolean = true,
    decorFitsSystemWindows: Boolean = true
): ComposeDialogStyle = BottomSheetStyle(
    dragHandle,
    hideAnimated,
    resizeContent,
    peekHeight,
    // DialogProperties
    dismissOnBackPress,
    dismissOnClickOutside,
    securePolicy,
    //usePlatformDefaultWidth,
    decorFitsSystemWindows
)