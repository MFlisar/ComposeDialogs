package com.michaelflisar.composedialogs.core

import android.os.Bundle
import android.os.Parcelable
import android.util.Size
import android.util.SizeF
import android.util.SparseArray
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.saveable.rememberSaveable
import java.io.Serializable


// we define functions for all different supported types of Bundle to avoid,
// that the user is not aware of the need that we need a saveable state for the dialog!
// => because this would end in the usage of autoSaver which would crash in this case which would only happen if the dialog state is saved...

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

@Composable
fun <T> rememberDialogState(
    data: T,
    saver: Saver<MutableState<T>, out Any>,
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

// ------------------------
// base function for all bundle supported data types
// ------------------------

@Composable
private fun <T> rememberDialogStateAutoSaver(
    data: T?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogState(data = data, autoSaver(), showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

// ------------------------
// BaseBundle - Boolean, Int, Long, Double, String
// ------------------------

@Composable
fun rememberDialogState(
    data: Boolean?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: Int?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: Long?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: Double?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: String?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

// ------------------------
// BaseBundle - BooleanArray, IntArray, LongArray, DoubleArray, StringArray
// ------------------------

@Composable
fun rememberDialogState(
    data: Array<Boolean>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: Array<Int>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: Array<Long>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: Array<Double>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: Array<String>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

// ------------------------
// Bundle - Byte, Char, Short, Float, CharSequence, Parcelable, Size, SizeF
// ------------------------

@Composable
fun rememberDialogState(
    data: Byte?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: Char?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: Short?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: Float?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: CharSequence?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun <T : Parcelable> rememberDialogState(
    data: T?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: Size?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: SizeF?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

// ------------------------
// Bundle - ParcelableArray, ParcelableArrayList, SparseParcelableArray
// ------------------------

@Composable
fun <T: Parcelable> rememberDialogState(
    data: Array<T>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun <T: Parcelable> rememberDialogStateArrayList(
    data: ArrayList<T>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun <T: Parcelable> rememberDialogState(
    data: SparseArray<T>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)


// ------------------------
// Bundle - IntegerArrayList, StringArrayList, CharSequenceArrayList
// ------------------------

@Composable
fun rememberDialogStateArrayListInt(
    data: ArrayList<Int>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogStateArrayListString(
    data: ArrayList<String>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogStateArrayListCharSequence(
    data: ArrayList<CharSequence>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)


// ------------------------
// Bundle - Serializable
// ------------------------

@Composable
fun <T: Serializable> rememberDialogState(
    data: T?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

// ------------------------
// Bundle - ByteArray, ShortArray, CharArray, FloatArray, CharSequenceArray
// ------------------------

@Composable
fun rememberDialogState(
    data: Array<Byte>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: Array<Short>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: Array<Char>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: Array<Float>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: Array<CharSequence>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

// ------------------------
// Bundle - Bundle
// ------------------------

@Composable
fun rememberDialogState(
    data: Bundle?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogStateAutoSaver(data = data, showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)