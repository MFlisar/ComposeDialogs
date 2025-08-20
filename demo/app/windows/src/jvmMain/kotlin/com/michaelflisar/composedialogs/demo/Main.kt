package com.michaelflisar.composedialogs.demo

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.michaelflisar.democomposables.DemoScaffold

fun main() {
    application {
        Window(
            title = "Compose Themer Demo",
            onCloseRequest = ::exitApplication,
            state = rememberWindowState(
                position = WindowPosition(Alignment.Center),
                width = 800.dp,
                height = 600.dp
            )
        ) {
            MaterialTheme {
                DemoScaffold { modifier, showInfo ->
                    DemoContent(
                        modifier = modifier,
                        showInfo = showInfo
                    )
                }
            }
        }
    }
}