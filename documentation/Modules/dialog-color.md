This shows a color selector dialog. A table with predefined material colors as well as a customisation page will be shown. Alpha support can be enabled optionally.

Check out the composable and it's documentation in the code snipplet below.

Generally following can be adjusted:

* alpha support yes/no
* shape of a color composable inside the color grid (rect, rounded rect, circle, ...)
* the number of columns in the color grid
* the style of how RGB(A) values are displayed (either as a number in the range of [0, 255] or as a percentage value [0%, 100%])

#### Example

<!-- snippet: ColorDemos::demo -->
```kt
if (state.visible) {
    val color = rememberDialogColor(Color.Blue.copy(alpha = .5f))
    DialogColor(
        state = state,
        color = color,
        alphaSupported = true,
        icon = icon,
        title = { Text("Color Dialog") },
        style = style,
        onEvent = {
            if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                val hex = color.value.toArgb().toUInt().toString(16).padStart(8, '0')
                showInfo("Selected color: #$hex")
            } else {
                showInfo("Event $it")
            }
        }
    )
}
```
<!-- endSnippet -->

#### Composable

<!-- snippet: DialogColor::constructor -->
```kt
/**
 * Shows a color dialog
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param color the selected color state
 * @param texts the texts ([DialogColor.Texts]) that are used inside this dialog - use [DialogColorDefaults.texts] to provide your own data
 * @param alphaSupported if true, the dialog supports color alpha values
 * @param shape the shape of the color cells
 * @param gridSize the size of the color grid
 * @param labelStyle the [DialogColor.LabelStyle] for the color picker
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DialogColor(
    // Base Dialog - State
    state: DialogState,
    // Custom - Required
    color: MutableState<Color>,
    // Custom - Optional
    texts: DialogColor.Texts = DialogColorDefaults.texts(),
    alphaSupported: Boolean = true,
    shape: Shape = MaterialTheme.shapes.small,
    gridSize: Int = if (isLandscape()) 6 else 4,
    labelStyle: DialogColor.LabelStyle = DialogColor.LabelStyle.Value,
    // Base Dialog - Optional
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: DialogOptions = DialogDefaults.options(),
    onEvent: (event: DialogEvent) -> Unit = {}
)
```
<!-- endSnippet -->

#### Screenshots

|                                                     | | | |
|-----------------------------------------------------|-|-|-|
| ![Screenshot](../screenshots/color/demo_color1.jpg) | ![Screenshot](../screenshots/color/demo_color2.jpg) | ![Screenshot](../screenshots/color/demo_color3.jpg) | ![Screenshot](../screenshots/color/demo_color4.jpg) |
