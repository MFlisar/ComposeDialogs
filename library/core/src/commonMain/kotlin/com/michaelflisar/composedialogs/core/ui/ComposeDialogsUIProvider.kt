package com.michaelflisar.composedialogs.core.ui

internal object ComposeDialogsUIProvider {
    val provider: ComposeDialogsUI.Provider
        get() = throw IllegalStateException(
            "No UI implementation is available. Use a provider module such as implementation:material3."
        )
}
