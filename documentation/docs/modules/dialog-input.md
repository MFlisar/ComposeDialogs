---
icon: material/puzzle
---

This shows a dialog with a `InputField`. All its parameters are exposed via the compose function as you can see below, which allows you to simply adjust the `InputFields` behaviour. Additinally you can attach a validator which ensures, that the dialog will only return a valid input and can't be closed otherwise.

Check out the composable and it's documentation in the code snipplet below.

Generally following can be adjusted:

* texts
* number of lines
* `TextField` options like number of lines, keyboard options, selection state...
* an optional `DialogInputValidator` can be provided

#### Example

=== "DialogInput"

    ```kotlin
    --8<-- "../demo/android/src/main/java/com/michaelflisar/composedialogs/demo/demos/InputDemos.kt:demo"
    ```

=== "DialogInputNumber"

    ```kotlin
    --8<-- "../demo/android/src/main/java/com/michaelflisar/composedialogs/demo/demos/InputDemos.kt:demo-number"
    ```
	
#### Composable

There are 2 main composables for this dialog, one for a string and for a numerical input.

=== "DialogInput"

    ```kotlin
    --8<-- "../library/modules/input/src/commonMain/kotlin/com/michaelflisar/composedialogs/dialogs/input/DialogInput.kt:constructor"
    ```

=== "DialogInputNumber"

    ```kotlin
    --8<-- "../library/modules/input/src/commonMain/kotlin/com/michaelflisar/composedialogs/dialogs/input/DialogInputNumber.kt:constructor"
    ```

#### Screenshots

| |                                                     |
|-|-----------------------------------------------------|
| ![Screenshot](../screenshots/input/demo_input1.jpg) | ![Screenshot](../screenshots/input/demo_input2.jpg) |