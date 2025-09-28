package com.michaelflisar.composedialogs.demo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.demo.classes.DemoStyle
import com.michaelflisar.composedialogs.demo.demos.BillingDemos
import com.michaelflisar.composedialogs.demo.demos.ColorDemos
import com.michaelflisar.composedialogs.demo.demos.CustomDemos
import com.michaelflisar.composedialogs.demo.demos.DateTimeDemos
import com.michaelflisar.composedialogs.demo.demos.InfoDemos
import com.michaelflisar.composedialogs.demo.demos.InputDemos
import com.michaelflisar.composedialogs.demo.demos.ListDemos
import com.michaelflisar.composedialogs.demo.demos.MenuDemos
import com.michaelflisar.composedialogs.demo.demos.NumberDemos
import com.michaelflisar.composedialogs.demo.demos.ProgressDemos
import com.michaelflisar.composedialogs.demo.demos.SingleDialogWithListDemos
import com.michaelflisar.democomposables.DemoCheckbox
import com.michaelflisar.democomposables.DemoSegmentedControl
import com.michaelflisar.democomposables.examples.Demo
import com.michaelflisar.democomposables.examples.DemoExamplesContent
import com.michaelflisar.democomposables.examples.DemoExamplesList
import com.michaelflisar.democomposables.examples.rememberSelectedDemo
import com.michaelflisar.democomposables.layout.DemoCollapsibleRegion
import com.michaelflisar.democomposables.layout.DemoColumn
import com.michaelflisar.democomposables.layout.rememberDemoExpandedRegions
import kotlin.collections.minusAssign

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DemoContent(
    modifier: Modifier,
    showInfo: (info: String) -> Unit,
) {
    // ------------
    // settings
    // ------------

    val demoStyle = rememberSaveable { mutableStateOf(DemoStyle.Dialog) }
    val swipeDismiss = rememberSaveable { mutableStateOf(false) }
    val showIcon = rememberSaveable { mutableStateOf(true) }
    //val testFullscreenLevel = remember { mutableStateOf(0) }
    //val test: (@Composable () -> Unit)? = if (testFullscreenLevel.value > 0) {
    //    {
    //        IconButton(
    //            onClick = { testFullscreenLevel.value -= 1 }
    //        ) {
    //            Icon(Icons.Default.ArrowBack, null)
    //        }
    //    }
    //} else null

    val style = when (demoStyle.value) {
        DemoStyle.Dialog -> DialogDefaults.styleDialog(
            swipeDismissable = swipeDismiss.value
            // ...
        )
        DemoStyle.Fullscreen -> DialogDefaults.styleFullscreenDialog(
            darkStatusBar = MaterialTheme.colorScheme.primary.luminance() < .5f, // demo has a primary toolbar...
            //navigationIcon = test,
            //menuActions = {
            //    IconButton(
            //        onClick = {
            //            testFullscreenLevel.value += 1
            //        }
            //    ) {
            //        Icon(Icons.Default.Add, null)
            //    }
            //}
        )
        DemoStyle.BottomSheet -> DialogDefaults.styleBottomSheet(
            dragHandle = true,
            animateShow = true
            // ...
        )
    }

    val icon: @Composable (() -> Unit)? = if (showIcon.value) {
        { Icon(Icons.Default.Home, null) }
    } else null

    // ------------
    // demos
    // ------------

    val demos = listOf(
        Demo.Example("Info Demos") { InfoDemos(style, icon, showInfo) },
        Demo.Example("Input Demos") { InputDemos(style, icon, showInfo) },
        Demo.Example("Number Demos") { NumberDemos(style, icon, showInfo) },
        Demo.Example("Date/Time Demos") { DateTimeDemos(style, icon, showInfo) },
        Demo.Example("Color Demos") { ColorDemos(style, icon, showInfo) },
        Demo.Example("List Demos") { ListDemos(style, icon, showInfo) },
        Demo.Example("Progress Demos") { ProgressDemos(style, icon, showInfo) },
        Demo.Example("Single Dialog with List") {
            SingleDialogWithListDemos(
                style,
                icon,
                showInfo
            )
        },
        Demo.Example("Billing Demos") { BillingDemos(style, icon, showInfo) },
        Demo.Example("Menu Demos") { MenuDemos(style, icon, showInfo) },
        Demo.Divider,
        Demo.Example("Custom Demos") { CustomDemos(style, icon, showInfo) },
    )

    // ------------
    // UI
    // ------------

    val selectedDemo = rememberSelectedDemo()
    val collapsibleRegion = rememberDemoExpandedRegions(ids = listOf(1, 2), single = false)
    DemoColumn(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        if (selectedDemo.intValue == -1) {
            // LEVEL 0 - settings + demo list
            DemoCollapsibleRegion("Settings", 1, collapsibleRegion) {
                DemoColumn {
                    DemoSegmentedControl(state = demoStyle, items = DemoStyle.entries)
                    AnimatedVisibility(visible = demoStyle.value == DemoStyle.Dialog) {
                        DemoCheckbox(checked = swipeDismiss, title = "Support SwipeDismiss?")
                    }
                    DemoCheckbox(checked = showIcon, title = "Show Icon?")
                }
            }
            DemoCollapsibleRegion("Demos", 2, collapsibleRegion) {
                DemoExamplesList(selectedDemo, demos)
            }
        } else {
            // LEVEL 1 CONTENT - selected demo page
            DemoExamplesContent(selectedDemo, demos)
        }
    }
}