This library offers 3 styles (actually 4) for dialog. You can always provide the style to a dialog composable and customise it however you want.

`DialogDefaults` does offer the corresponding functions to create a style.

#### Dialog Style

<!-- snippet: Dialog::style-dialog -->
```kt
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
```
<!-- endSnippet -->

#### Bottom Sheet Style

<!-- snippet: Dialog::style-bottom-sheet -->
```kt
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
```
<!-- endSnippet -->

#### Full Screen Dialog Style

<!-- snippet: Dialog::style-full-screen-dialog -->
```kt
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
 * @param contentColor the content [Color]
 * @param navigationIcon provides the navigation icon - a close icon by default but you can provide a custom one here (that toggles between close and back arrow depending on your custom logic)
 * @param applyContentPadding if true, the content padding will be applied
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
    contentColor: Color = FullscreenDialogStyleDefaults.contentColor,
    // added in 3.0.1
    navigationIcon: (@Composable () -> Unit)? = null,
    applyContentPadding: Boolean = true
): ComposeDialogStyle
```
<!-- endSnippet -->

#### Desktop Dialog Style

On windows you can also use a windows window based dialog style.

<!-- snippet: Dialog::style-windows-dialog -->
```kt
@Composable
fun DialogDefaults.styleWindowsDialog(
    dialogTitle: String,
    position: WindowPosition = WindowPosition(Alignment.Center),
    width: Dp = 800.dp,
    height: Dp = 600.dp,
    // Style
    iconColor: Color = DialogStyleDefaults.iconColor,
    titleColor: Color = DialogStyleDefaults.titleColor,
    contentColor: Color = DialogStyleDefaults.contentColor
): ComposeDialogStyle
```
<!-- endSnippet -->
