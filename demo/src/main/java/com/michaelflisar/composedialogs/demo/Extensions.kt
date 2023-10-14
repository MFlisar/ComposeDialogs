package com.michaelflisar.composedialogs.demo

import android.content.Context
import android.widget.Toast

// good enough for this demo app...
private var lastToast: Toast? = null

fun Context.showToast(text: String) {
    lastToast?.cancel()
    lastToast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
    lastToast?.show()
}