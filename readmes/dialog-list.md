## List Dialog

| Preview | | Module |
| :- | :- | :- |
| ![Preview](../screenshots/dark/demo_list1.jpg "Preview") | ![Preview](../screenshots/dark/demo_list2.jpg "Preview") | `list` |
| ![Preview](../screenshots/dark/demo_list3.jpg "Preview") | ![Preview](../screenshots/dark/demo_list4.jpg "Preview") | `list` |

This shows a dialog with a list of items. Rendering, selection mode and more is adjustable.

Here you can create a dialog based on static list data like following:

https://github.com/MFlisar/ComposeDialogs/blob/50ff476087bfe675cb85a609bb8d1eebe72f0bca/library/modules/list/src/commonMain/kotlin/com/michaelflisar/composedialogs/dialogs/list/DialogList.kt#L64-L83

But you can also create list with an asynchronous loader function like following:

https://github.com/MFlisar/ComposeDialogs/blob/50ff476087bfe675cb85a609bb8d1eebe72f0bca/library/modules/list/src/commonMain/kotlin/com/michaelflisar/composedialogs/dialogs/list/DialogList.kt#L123-L148
