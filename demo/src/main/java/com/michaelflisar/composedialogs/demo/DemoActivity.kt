package com.michaelflisar.composedialogs.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.*
import com.michaelflisar.composedialogs.demo.classes.DemoStyle
import com.michaelflisar.composedialogs.demo.classes.DemoTheme
import com.michaelflisar.composedialogs.demo.demos.*
import com.michaelflisar.composedialogs.demo.views.SegmentedControl
import com.michaelflisar.testcompose.ui.theme.ComposeDialogDemoTheme

class DemoActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val selectedDemoPage = rememberSaveable { mutableIntStateOf(-1) }

            BackHandler(selectedDemoPage.value != -1) {
                selectedDemoPage.value = -1
            }

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

                        // Dialog Setting States
                        val style = rememberSaveable { mutableStateOf(DemoStyle.Dialog) }
                        val swipeDismiss = rememberSaveable { mutableStateOf(false) }
                        val showIcon = rememberSaveable { mutableStateOf(true) }

                        // UI Settings
                        Settings(theme, dynamicTheme, style, swipeDismiss, showIcon)

                        // UI Example Dialogs
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(
                                    bottom = 16.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                )
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            val s = if (style.value == DemoStyle.BottomSheet) {
                                DialogDefaults.styleBottomSheet(
                                    // dragHandle = true,
                                    //hideAnimated = true,
                                    // resizeContent = true // only use this with scrollable content!
                                    // peekHeight = 0.dp
                                )
                            } else DialogDefaults.styleDialog(swipeDismissable = swipeDismiss.value)
                            val icon: @Composable (() -> Unit)? = if (showIcon.value) {
                                { Icon(Icons.Default.Home, null) }
                            } else null

                            // -----------------
                            // Demos
                            // -----------------

                            if (selectedDemoPage.value == -1) {
                                MainButton(selectedDemoPage, 0, "Info Demos")
                                MainButton(selectedDemoPage, 1, "Input Demos")
                                MainButton(selectedDemoPage, 2, "Number Demos")
                                MainButton(selectedDemoPage, 3, "Date/Time Demos")
                                MainButton(selectedDemoPage, 4, "Color Demos")
                                MainButton(selectedDemoPage, 5, "List Demos")
                                MainButton(selectedDemoPage, 6, "Progress Demos")
                                MainButton(selectedDemoPage, 7, "Single Dialog with List")
                                MainButton(selectedDemoPage, 8, "Billing Demos")
                                Divider()
                                MainButton(selectedDemoPage, 99, "Custom Demos")
                            } else {
                                when (selectedDemoPage.value) {

                                    // Predefined dialog demos
                                    0 -> InfoDemos(s, icon)
                                    1 -> InputDemos(s, icon)
                                    2 -> NumberDemos(s, icon)
                                    3 -> DateTimeDemos(s, icon)
                                    4 -> ColorDemos(s, icon)
                                    5 -> ListDemos(s, icon)
                                    6 -> ProgressDemos(s, icon)
                                    7 -> SingleDialogWithListDemos(s, icon)
                                    8 -> BillingDemos(s, icon)

                                    // Manually created custom demos
                                    99 -> CustomDemos(s, icon)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ----------------
    // Helper functions
    // ----------------

    @Composable
    private fun MainButton(selectedDemoPage: MutableState<Int>, page: Int, text: String) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { selectedDemoPage.value = page }
        ) {
            Text(text)
        }
    }


    @Composable
    private fun ColumnScope.Settings(
        theme: MutableState<DemoTheme>,
        dynamicTheme: MutableState<Boolean>,
        style: MutableState<DemoStyle>,
        swipeDismiss: MutableState<Boolean>,
        showIcon: MutableState<Boolean>
    ) {
        val dialogSettings = rememberDialogState()
        DialogSettings(
            dialogSettings,
            theme,
            dynamicTheme,
            style,
            swipeDismiss,
            showIcon
        )
        OutlinedButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 8.dp
                ),
            onClick = {
                dialogSettings.show()
            }) {
            Text("Settings")
        }
    }

    @Composable
    private fun DialogSettings(
        state: DialogState,
        theme: MutableState<DemoTheme>,
        dynamicTheme: MutableState<Boolean>,
        style: MutableState<DemoStyle>,
        swipeDismiss: MutableState<Boolean>,
        icon: MutableState<Boolean>
    ) {
        if (state.showing) {
            Dialog(
                state = state,
                title = { Text("Settings") },
                style = DialogDefaults.styleBottomSheet(peekHeight = 0.dp) // disable peek
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "App Theme",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    SettingsRowSegmentedControl(theme, DemoTheme.values().toList())
                    SettingsRowCheckbox(dynamicTheme, "Dynamic Colors?")
                    Text(
                        "Demo Dialog Appearance",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    SettingsRowSegmentedControl(style, DemoStyle.values().toList())
                    AnimatedVisibility(visible = style.value == DemoStyle.Dialog) {
                        SettingsRowCheckbox(swipeDismiss, "Support SwipeDismiss?")
                    }
                    SettingsRowCheckbox(icon, "Show Icon?")
                }
            }
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
    private fun <T> SettingsRowSegmentedControl(
        state: MutableState<T>,
        items: List<T>
    ) {
        SegmentedControl(
            modifier = Modifier.wrapContentWidth(),
            items = items.map { it.toString() },
            selectedIndex = items.indexOf(state.value)
        ) {
            state.value = items[it]
        }
    }
}