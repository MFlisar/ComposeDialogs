## Color Dialog

| Preview                                                    | | Module |
|:-----------------------------------------------------------| :- | :- |
| ![Preview](../screenshots/light/demo_color1.jpg "Preview") | ![Preview](../screenshots/light/demo_color2.jpg "Preview") | `color` |

This shows a color selector dialog. A table with predefined material colors as well as a customisation page will be shown. Alpha support can be enabled optionally.

```kotlin
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
    title: String? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    specialOptions: SpecialOptions = DialogDefaults.defaultColorDialogSpecialOptions(),
    onEvent: (event: DialogEvent) -> Unit = {}
)
```