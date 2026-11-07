package com.michaelflisar.composedialogs.core

/**
 * An enum for the different dialog event types.
 */
enum class DialogEventType {
    ButtonPositive,
    ButtonNegative,
    Dismissed
}

/**
 * The sealed event for all dialog events (button click and dismissal).
 */
sealed class DialogEvent {

    abstract val dismissed: Boolean

    @Deprecated(
        "use [DialogEvent::type] instead",
        replaceWith = ReplaceWith("type == DialogEventType.ButtonPositive")
    )
    abstract val isPositiveButton: Boolean

    abstract val type: DialogEventType

    /**
     * The event of a dialog button click.
     */
    class Button(
        val button: DialogButtonType,
        override val dismissed: Boolean
    ) : DialogEvent() {
        @Deprecated(
            "use [DialogEvent::type] instead",
            replaceWith = ReplaceWith("type == DialogEventType.ButtonPositive")
        )
        override val isPositiveButton = button == DialogButtonType.Positive

        override val type = when (button) {
            DialogButtonType.Positive -> DialogEventType.ButtonPositive
            DialogButtonType.Negative -> DialogEventType.ButtonNegative
        }

        override fun toString() = "DialogEvent::Button(button=$button, dismissed=$dismissed)"
    }

    /**
     * The event of a dialog dismissal.
     */
    object Dismissed : DialogEvent() {
        override val dismissed = true

        @Deprecated(
            "use [DialogEvent::type] instead",
            replaceWith = ReplaceWith("type == DialogEventType.ButtonPositive")
        )
        override val isPositiveButton = false

        override val type = DialogEventType.Dismissed

        override fun toString() = "DialogEvent::Dismissed"
    }
}

/**
 * An enum for the different dialog button types.
 */
enum class DialogButtonType {
    Positive,
    Negative
}
