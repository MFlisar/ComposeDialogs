package com.michaelflisar.composedialogs.core

import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.SecureFlagPolicy
import com.michaelflisar.composedialogs.core.style.BottomSheetStyle
import com.michaelflisar.composedialogs.core.style.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.style.DialogStyle

/**
 * the setup of a dialog that shows as a normal dialog popup
 *
 * @param swipeDismissable if true, the dialog can be swiped away by an up/down swipe
 * @param dismissOnBackPress if true, the dialog can be dismissed by a back press
 * @param dismissOnClickOutside if true, the dialog can be dismissed by clicking outside of its borders
 * @param usePlatformDefaultWidth if true, platform default width is used
 * @param shape the [Shape] of the dialog
 * @param containerColor the [Color] of the container
 * @param iconContentColor the content [Color] of the icon
 * @param titleContentColor the content [Color] of the title
 * @param textContentColor the content [Color] of the text
 * @param tonalElevation the elevation for the tonal [Color]
 */
@Composable
fun DialogDefaults.styleDialog(
    swipeDismissable: Boolean = false,
    // DialogProperties
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    securePolicy: SecureFlagPolicy = SecureFlagPolicy.Inherit,
    usePlatformDefaultWidth: Boolean = true,
    decorFitsSystemWindows: Boolean = true,
    // AlertDialog Settings
    shape: Shape = AlertDialogDefaults.shape,
    containerColor: Color = AlertDialogDefaults.containerColor,
    iconContentColor: Color = AlertDialogDefaults.iconContentColor,
    titleContentColor: Color = AlertDialogDefaults.titleContentColor,
    textContentColor: Color = AlertDialogDefaults.textContentColor,
    tonalElevation: Dp = AlertDialogDefaults.TonalElevation
): ComposeDialogStyle = DialogStyle(
    swipeDismissable,
    // DialogProperties
    dismissOnBackPress,
    dismissOnClickOutside,
    securePolicy,
    usePlatformDefaultWidth,
    decorFitsSystemWindows,
    // AlertDialog Settings
    shape,
    containerColor,
    iconContentColor,
    titleContentColor,
    textContentColor,
    tonalElevation
)

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