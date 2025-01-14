---
icon: material/palette-swatch
---

This library offers 3 styles (actually 4) for dialog. You can always provide the style to a dialog composable and customise it however you want.

`DialogDefaults` does offer the corresponding functions to create a style.

=== "Dialog Style"

    ```kotlin
    --8<-- "../library/core/src/commonMain/kotlin/com/michaelflisar/composedialogs/core/Dialog.kt:81:107"
    ```

=== "Bottom Sheet Style"

    ```kotlin
    --8<-- "../library/core/src/commonMain/kotlin/com/michaelflisar/composedialogs/core/Dialog.kt:121:157"
    ```

=== "Full Screen Dialol Style"

    ```kotlin
    --8<-- "../library/core/src/commonMain/kotlin/com/michaelflisar/composedialogs/core/Dialog.kt:176:201"
    ```

=== "Desktop Dialog Style"

    On windows you can also use a windows window based dialog style.

    ```kotlin
    --8<-- "../library/core/src/jvmMain/kotlin/com/michaelflisar/composedialogs/core/DialogDefaultsExtensions.kt:12:22"
    ```
