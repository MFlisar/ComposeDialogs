package com.michaelflisar.demo

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
suspend fun main() {
    ComposeViewport(
        // mit container id geht es nicht --> wäre aber gut, dann würde ein Loader angezeigt werden, aktuell wird der nicht angezeigt...
        // viewportContainerId = wasmSetup.canvasElementId
    ) {
        DemoApp()
    }
}