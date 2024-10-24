## Billing Dialog

This shows a dialog with the prices and names of your products. It also shows if a product is already owned and allows to buy unowned products by clicking them.

```kotlin
fun DialogBilling(
    state: DialogState,
    // custom settings
    products: List<DialogBilling.BillingProduct>,
    texts: DialogBilling.Texts = DialogBillingDefaults.texts(),
    // Base Dialog - Optional
    title: String? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    //buttons: DialogButtons = DialogDefaults.buttons(),
    //options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
)
```