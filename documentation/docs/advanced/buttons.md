---
icon: material/button-pointer
---

Every dialog supports up to 2 integrated buttons. Whenever you show a dialog you can decide which buttons you need.

`DialogDefaults` does offer the corresponding functions to create buttons.

```kotlin
val buttons1 = DialogDefaults.buttons(
    positive: DialogButton = DialogButton("Save"),
    negative: DialogButton = DialogButton("Cancel")
)

val noButtons = DialogDefaults.buttonsDisabled()
```

Buttons with an empty string will always be considered as disabled (they won't be rendered at all).