package com.michaelflisar.composedialogs.core


import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.saveable.rememberSaveable

// Data Types:
// BaseBundle
//   * Boolean, Int, Long, Double, String
//   * BooleanArray, IntArray, LongArray, DoubleArray, StringArray
// Bundle
//   * Byte, Char, Short, Float, CharSequence, Parcelable, Size, SizeF
//   * ParcelableArray, ParcelableArrayList, SparseParcelableArray
//   * IntegerArrayList, StringArrayList, CharSequenceArrayList
//   * Serializable
//   * ByteArray, ShortArray, CharArray, FloatArray, CharSequenceArray
//   * Bundle

// ------------------------
// Custom Data - here the user MUST provide a Saver
// ------------------------

/*
 be aware that T must be saveable by auto saver or ou must provide a custom saver!
 */
@Composable
fun <T : Any> rememberDialogState(
    data: T?,
    saver: Saver<MutableState<T?>, out Any> = autoSaver(),
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
): DialogStateWithData<T> {

    // showing should survive, even screen rotations and activity recreations
    val showing = rememberSaveable { mutableStateOf(showing) }

    // extra data - should survice screen rotations and activity recreates BUT must be reset if dialog is dismissed
    val d = rememberSaveable(saver = saver) { mutableStateOf(data) }
    if (!showing.value) {
        d.value = data
    }

    // interaction state should be reset whenever the dialog is reshown
    val interaction = remember(showing.value) {
        mutableStateOf(
            DialogInteractionSource(
                buttonPositiveEnabled = mutableStateOf(buttonPositiveEnabled),
                buttonNegativeEnabled = mutableStateOf(buttonNegativeEnabled),
                dismissAllowed = mutableStateOf(dismissAllowed),
                swipeAllowed = mutableStateOf(swipeAllowed)
            )
        )
    }
    return DialogStateWithData(showing, d, interaction)
}

// ------------------------
// Simple - NO data
// ------------------------

@Composable
fun rememberDialogState(
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
): DialogState {

    // showing should survive, even screen rotations and activity recreations
    val showing = rememberSaveable { mutableStateOf(showing) }

    // interaction state should be reset whenever the dialog is reshown
    val interaction = remember(showing.value) {
        mutableStateOf(
            DialogInteractionSource(
                buttonPositiveEnabled = mutableStateOf(buttonPositiveEnabled),
                buttonNegativeEnabled = mutableStateOf(buttonNegativeEnabled),
                dismissAllowed = mutableStateOf(dismissAllowed),
                swipeAllowed = mutableStateOf(swipeAllowed)
            )
        )
    }
    return DialogState(showing, interaction)
}