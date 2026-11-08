[![Maven Central](https://img.shields.io/maven-central/v/io.github.mflisar.composedialogs/core?color=blue)](https://central.sonatype.com/artifact/io.github.mflisar.composedialogs/core) ![API](https://img.shields.io/badge/api-24%2B-brightgreen.svg) ![Kotlin](https://img.shields.io/github/languages/top/MFlisar/ComposeDialogs.svg?color=blueviolet) ![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin_Multiplatform-blue?label=Kotlin)
# ComposeDialogs
![Platforms](https://img.shields.io/badge/PLATFORMS-black?style=for-the-badge)

This library offers you an easily extendible compose framework for modal dialogs and allows to show them as a **dialog**, **bottom sheet** or even as **full screen dialog**.

> [!NOTE]
> All features are splitted into separate modules, just include the modules you want to use!

> [!TIP]
> Try the interactive demo:
> 
> [![Open Interactive Demo](https://img.shields.io/badge/ComposeDialogs_Demo-2962FF?style=for-the-badge&logo=googlechrome&logoColor=white)](https://mflisar.github.io/ComposeDialogs/demo/)

# Table of Contents

- [Supported Platforms](#computer-supported-platforms)
- [Versions](#arrow_right-versions)
- [Setup](#wrench-setup)
- [Usage](#rocket-usage)
- [Demo](#sparkles-demo)
- [API](#books-api)
- [Other Libraries](#bulb-other-libraries)

# :computer: Supported Platforms

| Module | Notes |
|---|---|
| core | provides the basic dialog classes and styles |
| dialog-color | provides a color dialog |
| dialog-date | provides a date picker dialog |
| dialog-frequency | provides a frequency dialog |
| dialog-info | provides an info dialog |
| dialog-input | provides an input dialog |
| dialog-list | provides a list dialog |
| dialog-menu | provides a menu dialog |
| dialog-number | provides a number picker dialog |
| dialog-progress | provides a progress dialog |
| dialog-time | provides a time dialog |
| ui-color | provides toolkit-neutral UI for the color picker |
| ui-date | provides toolkit-neutral UI for the date picker |
| ui-frequency | provides toolkit-neutral UI for the frequency picker |
| ui-time | provides toolkit-neutral UI for the time picker |

# :arrow_right: Versions

| Dependency | Version |
|---|---|
| Kotlin | `2.4.21` |
| Jetbrains Compose | `1.12.2` |
| Jetbrains Compose Material3 | `1.9.0` |

> :warning: Following experimental annotations are used:
> - **OptIn**
>   - `androidx.compose.material3.ExperimentalMaterial3Api` (4x)
>   - `androidx.compose.ui.ExperimentalComposeUiApi` (7x)
>
> I try to use as less experimental features as possible, but in this case the ones above are needed!

# :wrench: Setup

<details open>

<summary><b>Using Version Catalogs</b></summary>

<br>

Define the dependencies inside your **libs.versions.toml** file.

```toml
[versions]

composedialogs = "<LATEST-VERSION>"

[libraries]

composedialogs-core = { module = "io.github.mflisar.composedialogs:core", version.ref = "composedialogs" }
composedialogs-dialog-color = { module = "io.github.mflisar.composedialogs:dialog-color", version.ref = "composedialogs" }
composedialogs-dialog-date = { module = "io.github.mflisar.composedialogs:dialog-date", version.ref = "composedialogs" }
composedialogs-dialog-frequency = { module = "io.github.mflisar.composedialogs:dialog-frequency", version.ref = "composedialogs" }
composedialogs-dialog-info = { module = "io.github.mflisar.composedialogs:dialog-info", version.ref = "composedialogs" }
composedialogs-dialog-input = { module = "io.github.mflisar.composedialogs:dialog-input", version.ref = "composedialogs" }
composedialogs-dialog-list = { module = "io.github.mflisar.composedialogs:dialog-list", version.ref = "composedialogs" }
composedialogs-dialog-menu = { module = "io.github.mflisar.composedialogs:dialog-menu", version.ref = "composedialogs" }
composedialogs-dialog-number = { module = "io.github.mflisar.composedialogs:dialog-number", version.ref = "composedialogs" }
composedialogs-dialog-progress = { module = "io.github.mflisar.composedialogs:dialog-progress", version.ref = "composedialogs" }
composedialogs-dialog-time = { module = "io.github.mflisar.composedialogs:dialog-time", version.ref = "composedialogs" }
composedialogs-ui-color = { module = "io.github.mflisar.composedialogs:ui-color", version.ref = "composedialogs" }
composedialogs-ui-date = { module = "io.github.mflisar.composedialogs:ui-date", version.ref = "composedialogs" }
composedialogs-ui-frequency = { module = "io.github.mflisar.composedialogs:ui-frequency", version.ref = "composedialogs" }
composedialogs-ui-time = { module = "io.github.mflisar.composedialogs:ui-time", version.ref = "composedialogs" }
```

And then use the definitions in your projects **build.gradle.kts** file like following:

```java
implementation(libs.composedialogs.core)
implementation(libs.composedialogs.dialog.color)
implementation(libs.composedialogs.dialog.date)
implementation(libs.composedialogs.dialog.frequency)
implementation(libs.composedialogs.dialog.info)
implementation(libs.composedialogs.dialog.input)
implementation(libs.composedialogs.dialog.list)
implementation(libs.composedialogs.dialog.menu)
implementation(libs.composedialogs.dialog.number)
implementation(libs.composedialogs.dialog.progress)
implementation(libs.composedialogs.dialog.time)
implementation(libs.composedialogs.ui.color)
implementation(libs.composedialogs.ui.date)
implementation(libs.composedialogs.ui.frequency)
implementation(libs.composedialogs.ui.time)
```

</details>

<details>

<summary><b>Direct Dependency Notation</b></summary>

<br>

Simply add the dependencies inside your **build.gradle.kts** file.

```kotlin
val composedialogs = "<LATEST-VERSION>"

implementation("io.github.mflisar.composedialogs:core:${composedialogs}")
implementation("io.github.mflisar.composedialogs:dialog-color:${composedialogs}")
implementation("io.github.mflisar.composedialogs:dialog-date:${composedialogs}")
implementation("io.github.mflisar.composedialogs:dialog-frequency:${composedialogs}")
implementation("io.github.mflisar.composedialogs:dialog-info:${composedialogs}")
implementation("io.github.mflisar.composedialogs:dialog-input:${composedialogs}")
implementation("io.github.mflisar.composedialogs:dialog-list:${composedialogs}")
implementation("io.github.mflisar.composedialogs:dialog-menu:${composedialogs}")
implementation("io.github.mflisar.composedialogs:dialog-number:${composedialogs}")
implementation("io.github.mflisar.composedialogs:dialog-progress:${composedialogs}")
implementation("io.github.mflisar.composedialogs:dialog-time:${composedialogs}")
implementation("io.github.mflisar.composedialogs:ui-color:${composedialogs}")
implementation("io.github.mflisar.composedialogs:ui-date:${composedialogs}")
implementation("io.github.mflisar.composedialogs:ui-frequency:${composedialogs}")
implementation("io.github.mflisar.composedialogs:ui-time:${composedialogs}")
```

</details>

# :rocket: Usage

#### Basic examples

```kotlin
// create and remember a state
val state = rememberDialogState()

// Create a dialog composable
if (state.visible) {
    DialogInfo(
        state = state,
        info = "Some "
    )
}

// show the dialog inside a button press event or similar
Button(onClick = { state.show() }) {
    Text("Show Dialog")
}
```

Alternatively you can hold any `saveable` state inside dialog state. If the dialog state holds any data, it will consider itself as visible.

```kotlin
// create and remember a state (e.g. selected list index as in this example)
val state = rememberDialogState<Int>(data = null)

// show a dialog if necessary
if (state.showing)
{
    val data = state.requireData() // in this example the data is the list index
    DialogInfo(
        state = state,
        info = "Data = $data"
    )
}

// a list that uses the dialog
val items = 1..100
LazyColumn {
    items.forEach {
        item(key = it) {
            Button(onClick = { state.show(it) }) {
                Text("Item $it")
            }
        }
    }
}
```

#### ViewModel / usage outside of composable

If you want to hold the dialog state inside a `ViewModel` or similar you can use the `dialogStateOf` functions.

```kotlin

// simple usage
val dialog = dialogStateOf() // a state that holds no data, just visibility information
val dialog = dialogStateOf<String>(data = null) // a state that holds some data

// if the state should survive process death, you can use following pattern with `SavedStateHandle`
private val KEY = "dialog_data"
val dialog = dialogStateOf(
	data = savedStateHandle.get<String?>(KEY),
	onStateChanged = { savedStateHandle[KEY] = it }
)
```

#### Styling/Customisation and events

All dialog (also custom ones) do support styling like:

* optional icon (including style like `CenterTop` and `Begin`)
* dialog style (dialog, bottom sheet, fullscreen)
* custom buttons (zero, one or two) with custom texts
* a options variable to set up features like `dismissOnButtonClick`, `dismissOnBackPress` and `dismissOnClickOutside`
* an event callback for the close event and button click event

Check out the advanced region on the side for more details.

#### Dialogs

Check out all the modules to find out more about the available implementations.

# :sparkles: Demo

A [demo project](demo) is included.

# :books: API

Check out the [API documentation](https://MFlisar.github.io/ComposeDialogs/api/).

# :bulb: Other Libraries

You can find more of my multiplatform libraries that work well together [here](https://mflisar.github.io/Libraries/).

# :twisted_rightwards_arrows: Compatibility

When combining my libraries, you can find compatibility information [here](https://mflisar.github.io/Libraries/compatibilities/).
