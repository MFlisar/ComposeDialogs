package com.michaelflisar.composedialogs.core

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.style.BottomSheetStyle
import com.michaelflisar.composedialogs.core.style.BottomSheetStyleDefaults
import com.michaelflisar.composedialogs.core.style.DialogStyle
import com.michaelflisar.composedialogs.core.style.DialogStyleDefaults
import com.michaelflisar.composedialogs.core.style.FullscreenDialogStyle
import com.michaelflisar.composedialogs.core.style.FullscreenDialogStyleDefaults

// ------------------
// defaults functions
// ------------------

/**
 * Shows a dialog containing some arbitrary [content]
 *
 * @param state the [DialogState] of the dialog
 * @param title the optional title of the dialog
 * @param icon the optional icon of the dialog
 * @param style the [ComposeDialogStyle] of the dialog - use [DialogDefaults.styleDialog] or [DialogDefaults.styleBottomSheet]
 * @param buttons the [DialogButtons] of the dialog - use [DialogDefaults.buttons] here [DialogDefaults.buttonsDisabled]
 * @param options the [DialogOptions] of the dialog - use [DialogDefaults.options] here
 * @param onEvent the callback for all [DialogEvent] - this can be a button click [DialogEvent.Button] or the dismiss information [DialogEvent.Dismissed]
 * @param content the content of this dialog
 */
@Composable
fun Dialog(
    state: DialogState,
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: DialogOptions = DialogDefaults.options(),
    onEvent: (event: DialogEvent) -> Unit = {},
    content: @Composable () -> Unit
) {
    style.Show(
        title,
        icon,
        buttons,
        options,
        state,
        onEvent,
        content
    )
}

// ------------------
// defaults functions
// ------------------

object DialogDefaults {

    /**
     * the setup of the dialog buttons
     *
     * use [buttonsDisabled] if you do not want to show any buttons
     * use [DialogButton.DISABLED] as button if you do not want to show the respective button
     *
     * Info: Buttons with an empty text will be disabled!
     *
     * @param positive positive [DialogButton]
     * @param negative negative [DialogButton]
     */
    @Composable
    fun buttons(
        positive: DialogButton = DialogButton(stringOk()),
        negative: DialogButton = DialogButton("")
    ) = DialogButtons(
        positive,
        negative
    )

    /**
     * the setup of the dialog buttons if you do not want to show any buttons
     */
    @Composable
    fun buttonsDisabled() = DialogButtons.DISABLED

    /**
     * some additional options for the dialog
     *
     * @param dismissOnButtonClick if true, the dialog will be dismissed on button click
     */
    @Composable
    fun options(
        dismissOnButtonClick: Boolean = true
    ) = DialogOptions(
        dismissOnButtonClick
    )

    /* --8<-- [start: style-dialog] */
    /**
     * the setup of a dialog that shows as a normal dialog popup
     *
     * @param swipeDismissable if true, the dialog can be swiped away by an up/down swipe
     * @param dismissOnBackPress if true, the dialog can be dismissed by a back press
     * @param dismissOnClickOutside if true, the dialog can be dismissed by clicking outside of its borders
     * @param scrim if true, the dialog will a show a scrim behind it
     * @param options provides custom style options of the dialog
     * @param shape the [Shape] of the dialog
     * @param containerColor the [Color] of the container
     * @param iconColor the content [Color] of the icon
     * @param titleColor the content [Color] of the title
     * @param contentColor the content [Color] of the text
     */
    @Composable
    fun styleDialog(
        swipeDismissable: Boolean = false,
        // DialogProperties
        dismissOnBackPress: Boolean = true,
        dismissOnClickOutside: Boolean = true,
        scrim: Boolean = true,
        // Style
        options: StyleOptions = StyleOptions(),
        shape: Shape = DialogStyleDefaults.shape,
        containerColor: Color = DialogStyleDefaults.containerColor,
        iconColor: Color = DialogStyleDefaults.iconColor,
        titleColor: Color = DialogStyleDefaults.titleColor,
        contentColor: Color = DialogStyleDefaults.contentColor
    ): ComposeDialogStyle
            /* --8<-- [end: style-dialog] */ {
        return DialogStyle(
            swipeDismissable,
            // DialogProperties
            dismissOnBackPress,
            dismissOnClickOutside,
            scrim,
            // Style
            options,
            shape,
            containerColor,
            iconColor,
            titleColor,
            contentColor
        )
    }

    /* --8<-- [start: style-bottom-sheet] */
    /**
     * the setup of a dialog that shows as a normal dialog popup
     *
     * @param dragHandle if true, a drag handle will be shown
     * @param peekHeight the peek height calculation of the bottom sheet
     * @param expandInitially if true, the bottom sheet is initially displayed in expanded state (even if it has a peek height)
     * @param velocityThreshold the velocity threshold of the bottom sheet
     * @param positionalThreshold the positional threshold of the bottom sheet
     * @param animateShow if true, the sheet will be animated on first show
     * @param dismissOnBackPress if true, the dialog can be dismissed by a back press
     * @param dismissOnClickOutside if true, the dialog can be dismissed by clicking outside of its borders
     * @param scrim if true, the dialog will a show a scrim behind it
     * @param options provides custom style options of the dialog
     * @param shape the [Shape] of the dialog
     * @param containerColor the [Color] of the container
     * @param iconColor the content [Color] of the icon
     * @param titleColor the content [Color] of the title
     * @param contentColor the content [Color] of the text
     */
    @Composable
    fun styleBottomSheet(
        dragHandle: Boolean = true,
        peekHeight: ((containerHeight: Dp, sheetHeight: Dp) -> Dp)? = BottomSheetStyleDefaults.peekHeight,
        expandInitially: Boolean = false,
        velocityThreshold: () -> Dp = { 125.dp },
        positionalThreshold: (totalDistance: Dp) -> Dp = { 56.dp },
        animateShow: Boolean = false,
        // DialogProperties
        dismissOnBackPress: Boolean = true,
        dismissOnClickOutside: Boolean = true,
        scrim: Boolean = true,
        // Style
        options: StyleOptions = StyleOptions(),
        shape: Shape = BottomSheetStyleDefaults.shape,
        containerColor: Color = BottomSheetStyleDefaults.containerColor,
        iconColor: Color = BottomSheetStyleDefaults.iconColor,
        titleColor: Color = BottomSheetStyleDefaults.titleColor,
        contentColor: Color = BottomSheetStyleDefaults.contentColor
    ): ComposeDialogStyle
            /* --8<-- [end: style-bottom-sheet] */ {
        return BottomSheetStyle(
            dragHandle,
            peekHeight,
            expandInitially,
            velocityThreshold,
            positionalThreshold,
            animateShow,
            // DialogProperties
            dismissOnBackPress,
            dismissOnClickOutside,
            scrim,
            // Style
            options,
            shape,
            containerColor,
            iconColor,
            titleColor,
            contentColor
        )
    }

    /* --8<-- [start: style-full-screen-dialog] */
    /**
     * the setup of a dialog that shows as a normal dialog popup
     *
     * @param darkStatusBar if true, the dialog icons will be adjusted to a dark status bar
     * @param menuActions if provided, it replaces the the default close menu action
     * @param dismissOnBackPress if true, the dialog can be dismissed by a back press
     * @param toolbarColor the [Color] of the toolbar
     * @param toolbarActionColor the [Color] of the actions in the toolbar
     * @param containerColor the [Color] of the container
     * @param iconColor the content [Color] of the icon
     * @param titleColor the content [Color] of the title
     */
    @Composable
    fun styleFullscreenDialog(
        darkStatusBar: Boolean = false,
        menuActions: @Composable (RowScope.() -> Unit)? = null,
        // DialogProperties
        dismissOnBackPress: Boolean = true,
        // Style
        toolbarColor: Color = FullscreenDialogStyleDefaults.toolbarColor,
        toolbarActionColor: Color = FullscreenDialogStyleDefaults.toolbarActionColor,
        containerColor: Color = FullscreenDialogStyleDefaults.containerColor,
        iconColor: Color = FullscreenDialogStyleDefaults.iconColor,
        titleColor: Color = FullscreenDialogStyleDefaults.titleColor,
        contentColor: Color = FullscreenDialogStyleDefaults.contentColor
    ): ComposeDialogStyle
            /* --8<-- [end: style-full-screen-dialog] */ {
        return FullscreenDialogStyle(
            darkStatusBar,
            menuActions,
            // DialogProperties
            dismissOnBackPress,
            // Style
            toolbarColor,
            toolbarActionColor,
            containerColor,
            iconColor,
            titleColor,
            contentColor
        )
    }
}

// ------------------
// classes
// ------------------

/**
 * the sealed event for all dialog events (button click and dismissal)
 *
 * [DialogEvent.dismissed] the information if this event will dismiss the dialog or not
 * [DialogEvent.isPositiveButton] a convenient value to determine, if this is the event of the positive dialog button
 */
sealed class DialogEvent {

    abstract val dismissed: Boolean
    abstract val isPositiveButton: Boolean // most interesting attribute so we make it easily accessible

    /**
     * the event of a dialog button click
     *
     * @param button the [DialogButtonType] of the button that was clicked
     * @param dismissed the information if this button will dismiss the dialog or not
     */
    class Button(
        val button: DialogButtonType,
        override val dismissed: Boolean
    ) : DialogEvent() {
        override val isPositiveButton = button == DialogButtonType.Positive
        override fun toString() = "DialogEvent::Button(button=$button, dismissed=$dismissed)"
    }

    /**
     * the event of a dialog dismissal
     */
    object Dismissed : DialogEvent() {
        override val dismissed = true
        override val isPositiveButton = false
        override fun toString() = "DialogEvent::Dismissed"
    }
}

/**
 * an enum for the different dialog button types
 */
enum class DialogButtonType {
    Positive,
    Negative
}

/**
 * an interaction source for button states and option state
 *
 * @param buttonPositiveEnabled the positive button is only enabled if this state is true
 * @param buttonNegativeEnabled the negative button is only enabled if this state is true
 * @param dismissAllowed the dialog can only be dismissed if this state is true
 * @param swipeAllowed the dialog can only be swiped away if this state is true
 */
class DialogInteractionSource internal constructor(
    val buttonPositiveEnabled: MutableState<Boolean>,
    val buttonNegativeEnabled: MutableState<Boolean>,
    val dismissAllowed: MutableState<Boolean>,
    val swipeAllowed: MutableState<Boolean>
)

/**
 * a dialog state holding the current showing state and the some additional state [DialogInteractionSource] of the dialog
 */
abstract class DialogState {

    /**
     * the showing state of the dialog
     */
    abstract val visible: Boolean

    /**
     * the [DialogInteractionSource] holding other states for this dialog
     */
    abstract val interactionSource: DialogInteractionSource

    abstract fun onDismiss()

    /**
     * this will dismiss this dialog (if the [interactionSource] ([DialogInteractionSource.dismissAllowed]) does allow this)
     *
     * @return true, if the dismiss was successful
     */
    fun dismiss(): Boolean {
        if (interactionSource.dismissAllowed.value) {
            onDismiss()
        }
        return !visible
    }

    internal fun dismiss(
        onEvent: (event: DialogEvent) -> Unit
    ): Boolean {
        if (dismiss())
            onEvent(DialogEvent.Dismissed)
        return !visible
    }

    internal fun onButtonPressed(
        onEvent: (event: DialogEvent) -> Unit,
        button: DialogButtonType,
        dismiss: Boolean
    ) {
        onEvent(DialogEvent.Button(button, dismiss))
    }

    /**
     * this will determine if a button currently can be pressed (depends on the [interactionSource] ([DialogInteractionSource.buttonPositiveEnabled] or [DialogInteractionSource.buttonNegativeEnabled]))
     */
    fun isButtonEnabled(button: DialogButtonType): Boolean {
        return when (button) {
            DialogButtonType.Positive -> interactionSource.buttonPositiveEnabled.value
            DialogButtonType.Negative -> interactionSource.buttonNegativeEnabled.value
        }
    }

    /**
     * this will enable or disable a button
     *
     * @param button the [DialogButtonType] that should be enabled/disabled
     * @param enabled if true, the button will be enabled
     */
    fun enableButton(
        button: DialogButtonType,
        enabled: Boolean
    ) {
        when (button) {
            DialogButtonType.Positive -> interactionSource.buttonPositiveEnabled.value = enabled
            DialogButtonType.Negative -> interactionSource.buttonNegativeEnabled.value = enabled
        }
    }

    /**
     * this will make the dialog dismissable or not
     *
     * @param enabled if true, the dialog can be dismissed
     */
    fun dismissable(enabled: Boolean) {
        interactionSource.dismissAllowed.value = enabled
    }
}

/**
 * a dialog state holding the current showing state and the some additional state [DialogInteractionSource] of the dialog
 *
 * @param state the visibility state of the dialog
 * @param interactionSource the [DialogInteractionSource] holding other states for this dialog
 */
class DialogStateNoData(
    private val state: MutableState<Boolean>,
    interactionSource: MutableState<DialogInteractionSource>
) : DialogState() {

    override val visible by state
    override val interactionSource by interactionSource

    override fun onDismiss() {
        state.value = false
    }

    /**
     * this will show this dialog
     */
    fun show() {
        state.value = true
    }

}

/**
 * a dialog state holding the current state and the some additional state [DialogInteractionSource] of the dialog
 *
 * @param visible the visibility state of the dialog - must be derived from the state!
 * @param state the state of the dialog - if not null, the dialog is visible, otherwise not
 * @param interactionSource the [DialogInteractionSource] holding other states for this dialog
 */
class DialogStateWithData<T>(
    visible: State<Boolean>,
    private val state: MutableState<T?>,
    interactionSource: MutableState<DialogInteractionSource>,
) : DialogState() {

    override val visible by visible
    override val interactionSource by interactionSource

    override fun onDismiss() {
        state.value = null
    }

    /**
     * this will show this dialog
     */
    fun show(data: T) {
        state.value = data
    }

    /**
     * this will return the currently holded data
     */
    val data: T?
        get() = state.value

    /**
     * this will return the currently holded data
     *
     * should only be called if the dialog is shown because of a forcefully cast to a non null value!
     */
    fun requireData() = state.value!!

}

/**
 * a dialog buttons
 *
 * @param text the text of the button
 */
class DialogButton(
    val text: String = ""
) {
    companion object {
        val DISABLED = DialogButton("")
    }

    val enabled: Boolean
        get() = text.isNotEmpty()
}

/**
 * see [DialogDefaults.buttons] and [DialogDefaults.buttonsDisabled]
 *
 */
class DialogButtons internal constructor(
    val positive: DialogButton,
    val negative: DialogButton
) {
    companion object {
        val DISABLED = DialogButtons(DialogButton.DISABLED, DialogButton.DISABLED)
    }

    val enabled = positive.enabled || negative.enabled
}

class StyleOptions(
    val iconMode: IconMode = IconMode.CenterTop
) {
    enum class IconMode {
        CenterTop,
        Begin
    }

    fun copy(
        iconMode: IconMode = this.iconMode
    ) = StyleOptions(iconMode)
}

class DialogOptions internal constructor(
    val dismissOnButtonClick: Boolean
)

class DialogSpacing internal constructor(
    val spacingContentToButtons: Dp,
    val spacingContentToBottom: Dp
) {
    fun contentPadding(buttons: DialogButtons) =
        if (buttons.enabled) spacingContentToButtons else spacingContentToBottom
}