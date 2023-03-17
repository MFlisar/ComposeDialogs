package com.michaelflisar.composedialogs.demo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.*
import com.michaelflisar.composedialogs.demo.views.SegmentedControl
import com.michaelflisar.composedialogs.dialogs.info.DialogInfo
import com.michaelflisar.composedialogs.dialogs.info.DialogInput
import com.michaelflisar.testcompose.ui.theme.ComposeDialogDemoTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DemoActivity : ComponentActivity() {

    enum class DemoStyle {
        Dialog,
        BottomSheet
    }

    enum class DemoTheme {
        System,
        Dark,
        Light;

        @Composable
        fun isDark(): Boolean {
            return when (this) {
                System -> isSystemInDarkTheme()
                Dark -> true
                Light -> false
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val theme = rememberSaveable { mutableStateOf(DemoTheme.System) }
            val dynamicTheme = rememberSaveable { mutableStateOf(false) }

            ComposeDialogDemoTheme(
                darkTheme = theme.value.isDark(),
                dynamicColor = dynamicTheme.value
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        TopAppBar(
                            title = { Text(stringResource(R.string.app_name)) },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            // Settings
                            val style = rememberSaveable { mutableStateOf(DemoStyle.Dialog) }
                            val swipeDismiss = rememberSaveable { mutableStateOf(false) }
                            val showIcon = rememberSaveable { mutableStateOf(true) }

                            Text(text = "Settings", style = MaterialTheme.typography.titleLarge)
                            OutlinedCard {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Settings(theme, dynamicTheme, style, swipeDismiss, showIcon)
                                }

                            }

                            // -----------------
                            // Settings for dialogs
                            // -----------------

                            val s = if (style.value == DemoStyle.BottomSheet) {
                                DialogDefaults.styleBottomSheet(
                                    // dragHandle = true,
                                    //hideAnimated = true,
                                    // resizeContent = true // only use this with scrollable content!
                                    // peekHeight = 0.dp
                                )
                            } else DialogDefaults.styleDialog(swipeDismissable = swipeDismiss.value)
                            val icon = if (showIcon.value) {
                                DialogDefaults.icon(rememberVectorPainter(Icons.Default.Home))
                            } else null

                            // -----------------
                            // Buttons for Demo Dialogs...
                            // -----------------

                            Text(text = "Demos", style = MaterialTheme.typography.titleLarge)

                            DemoDialogRegion("Info Dialog")
                            DemoDialogInfo1(s, icon)
                            DemoDialogInfo2(s, icon)

                            DemoDialogRegion("Input Dialog")
                            DemoDialogInput1(s, icon, "Hello")

                            DemoDialogRegion("Custom Dialog")
                            DemoDialogCustom1(s, icon)
                            DemoDialogCustom2(s, icon)
                        }
                    }
                }
            }
        }
    }

    // ----------------
    // Demos
    // ----------------

    @Composable
    private fun DemoDialogInfo1(style: DialogStyle, icon: DialogIcon?) {

        val state = rememberDialogState()
        if (state.showing) {
            DialogInfo(
                state = state,
                title = "Dialog Info",
                info = "Simple Info Dialog",
                icon = icon,
                style = style,
                onEvent = { event ->
                    showToast("Event $event")
                }
            )
        }
        DemoDialogButton(
            state,
            Icons.Default.Info,
            "Dialog Info 1",
            "Shows a basic info dialog"
        )
    }

    @Composable
    private fun DemoDialogInfo2(style: DialogStyle, icon: DialogIcon?) {

        val state = rememberDialogState(
            showing = false,
            buttonPositiveEnabled = false,
            dismissAllowed = false
        )
        if (state.showing) {

            var icon by remember { mutableStateOf(icon) }
            var time by rememberSaveable { mutableStateOf(10) }
            val iconDone = DialogDefaults.icon(rememberVectorPainter(Icons.Default.Check))
            LaunchedEffect(Unit) {
                launch {
                    while (time > 0) {
                        delay(1000)
                        time--
                    }
                    state.enableButton(DialogButtonType.Positive, true)
                    state.dismissable(true)
                    icon = iconDone
                }
            }

            DialogInfo(
                state = state,
                title = "Dialog Info 2",
                info = if (time == 0) "Dialog can be dismissed" else "Dialog can be dismissed in $time seconds...",
                icon = icon,
                style = style,
                onEvent = {
                    showToast("Event $it")
                }
            )
        }
        DemoDialogButton(
            state,
            Icons.Default.Info,
            "Dialog Info 2",
            "Shows a simple dialog with updating text that can't be closed until before 10s are over"
        )
    }

    @Composable
    private fun DemoDialogInput1(style: DialogStyle, icon: DialogIcon?, text: String) {

        val state = rememberDialogState(
            showing = false,
            buttonPositiveEnabled = text.isNotEmpty(),
            dismissAllowed = text.isNotEmpty()
        )
        if (state.showing) {

            // special state for input dialog
            val input = rememberSaveable { mutableStateOf(text) }

            // input dialog
            DialogInput(
                state = state,
                title = "Dialog Input",
                input = input,
                inputLabel = "Input",
                icon = icon,
                style = style,
                onEvent = {
                    if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                        // we should probably handle the input value in this case
                        showToast("Submitted Input: ${input.value}")
                    } else {
                        showToast("Event $it")
                    }
                },
                validator = DialogInput.InputValidator(
                    validate = {
                        if (it.isNotEmpty())
                            null
                        else
                            "Empty input is not allowed!"
                    }
                ),
                onTextStateChanged = { valid, text ->
                    state.enableButton(DialogButtonType.Positive, valid)
                }
            )
        }
        DemoDialogButton(
            state,
            Icons.Default.TextFields,
            "Show Dialog Input",
            "Shows an input dialog that does not accept an empty input"
        )
    }

    @Composable
    private fun DemoDialogCustom1(style: DialogStyle, icon: DialogIcon?) {
        val state = rememberDialogState()
        if (state.showing) {
            Dialog(
                state = state,
                style = style,
                icon = icon,
                title = "Custom Dialog"
            ) {
                var checked by rememberSaveable { mutableStateOf(false) }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = checked, onCheckedChange = { checked = it })
                    Text(text = "Show Details")
                }
                AnimatedVisibility(visible = checked) {
                    Column {
                        Text(text = "Detail 1...")
                        Text(text = "Detail 2...")
                        Text(text = "Detail 3...")
                    }
                }
            }
        }
        DemoDialogButton(
            state,
            Icons.Default.Build,
            "Show Custom Dialog (Resizing)",
            "Shows a custom dialog with resizeable content"
        )
    }

    @Composable
    private fun DemoDialogCustom2(style: DialogStyle, icon: DialogIcon?) {
        val state = rememberDialogState()
        if (state.showing) {
            Dialog(
                state = state,
                style = style,
                icon = icon,
                title = "Custom Dialog"
            ) {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    for (i in 1..50)
                        Text(modifier = Modifier.fillMaxWidth(), text = "Row $i")
                }
            }
        }
        DemoDialogButton(
            state,
            Icons.Default.Build,
            "Show Custom Dialog (Nested Scrolling)",
            "Shows a custom dialog with scrollable content"
        )
    }

    // ----------------
    // Helper functions
    // ----------------

    @Composable
    private fun Settings(
        theme: MutableState<DemoTheme>,
        dynamicTheme: MutableState<Boolean>,
        style: MutableState<DemoStyle>,
        swipeDismiss: MutableState<Boolean>,
        icon: MutableState<Boolean>
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SettingsRowSpinner(theme, "Theme", DemoTheme.values().toList())
            SettingsRowCheckbox(dynamicTheme, "Dynamic Colors?")
            SettingsRowSpinner(style, "Style", DemoStyle.values().toList())
            AnimatedVisibility(
                visible = style.value == DemoStyle.Dialog
            ) {
                SettingsRowCheckbox(swipeDismiss, "Dialog - SwipeDismiss?")
            }
            SettingsRowCheckbox(icon, "Icon?")
        }
    }

    @Composable
    private fun SettingsRowCheckbox(state: MutableState<Boolean>, title: String) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = state.value, onCheckedChange = { state.value = it })
            Text(title)
        }
    }

    @Composable
    private fun <T> SettingsRowSpinner(
        state: MutableState<T>,
        title: String,
        items: List<T>
    ) {

        SegmentedControl(
            modifier = Modifier.wrapContentWidth(),
            items = items.map { it.toString() }
        ) {
            state.value = items[it]
        }

        /*Spinner(
            modifier = Modifier.wrapContentWidth(),
            title,
            items,
            state.value
        ) { index, item ->
            state.value = item
        }*/
    }

    @Composable
    private fun DemoDialogRegion(title: String) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
    }

    @Composable
    private fun DemoDialogButton(
        state: DialogState,
        icon: ImageVector,
        label: String,
        description: String
    ) {
        ElevatedCard(
            modifier = Modifier
        ) {
            Row(modifier = Modifier
                .fillMaxSize()
                .clickable { state.show() }
                .padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = ""
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                ) {
                    Text(text = label, style = MaterialTheme.typography.titleSmall)
                    Text(text = description, style = MaterialTheme.typography.bodySmall)
                }
            }

        }
    }

    var lastToast: Toast? = null

    private fun showToast(text: String) {
        lastToast?.cancel()
        lastToast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
        lastToast?.show()
    }
}