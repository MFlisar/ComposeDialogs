---
icon: material/run
---

Every dialog offers an `onEvent` callback. You can handle the dialog events inside there like e.g. if dialog was closed or if a dialog button was clicked.

```kotlin
DialogInfo(
    state = state,
    info = "some info",
    onEvent = {

        // Variant 1
        when (event) {
            is DialogEvent.Button -> {
                if (event.button == DialogButtonType.Positive) {
                    // positive button was pressed
                } else {
                    // negatvie button was pressed
                }
            }
            is DialogEvent.Dismissed -> {
                // dialog was dismissed by click outside or back press
            }
        }

        // Variant 2
        // many times you only want to handle the positive button click
        if (event.isPositiveButton) {
            // positive button was pressed
        }
    }
)
```