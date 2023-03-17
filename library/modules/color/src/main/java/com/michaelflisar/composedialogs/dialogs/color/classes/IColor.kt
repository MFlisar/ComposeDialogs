package com.michaelflisar.composedialogs.dialogs.color.classes

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat

internal sealed interface IColor {

    val color: Int
    val label: String

    fun getCustomDrawable(): Drawable? = null

    fun get(context: Context): Int {
        return ContextCompat.getColor(context, color)
    }

    fun getColor(context: Context) = Color(get(context))
}