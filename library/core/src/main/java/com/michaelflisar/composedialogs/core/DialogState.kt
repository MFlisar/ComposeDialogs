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

// ------------------------
// Primitives - Byte/Char/Short/Float/CharSequence
// ------------------------

@Composable
fun rememberDialogState(
    data: Byte?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogState(data = data, autoSaver(), showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: Char?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogState(data, autoSaver(), showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: Short?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogState(data, autoSaver(), showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: Float?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogState(data, autoSaver(), showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: CharSequence?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogState(data, autoSaver(), showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

// ------------------------
// Arrays - Byte/Short/Char/Float/CharSequence/Parcelable
// ------------------------

@Composable
fun rememberDialogState(
    data: Array<Byte>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogState(data, autoSaver(), showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: Array<Short>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogState(data, autoSaver(), showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: Array<Char>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogState(data, autoSaver(), showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: Array<Float>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogState(data, autoSaver(), showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: Array<CharSequence>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogState(data, autoSaver(), showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: Array<Parcelable>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogState(data, autoSaver(), showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun <T : Parcelable> rememberDialogStateSparceArray(
    data: SparseArray<T>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogState(data, autoSaver(), showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

// ------------------------
// Lists - Parcelable/Int/String/CharSequence
// => own "names" because of confilciting java class definitions (all are ArrayLists!)
// ------------------------

@Composable
fun <T : Parcelable> rememberDialogStateListParcelable(
    data: ArrayList<T>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogState(data, autoSaver(), showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogStateListInt(
    data: ArrayList<Int>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogState(data, autoSaver(), showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogStateListString(
    data: ArrayList<String>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogState(data, autoSaver(), showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogStateListCharSequence(
    data: ArrayList<CharSequence>?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogState(data, autoSaver(), showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

// ------------------------
// Non Primitives
// ------------------------

@Composable
fun rememberDialogState(
    data: Size?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogState(data, autoSaver(), showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: SizeF?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogState(data, autoSaver(), showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun rememberDialogState(
    data: Bundle?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogState(data, autoSaver(), showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

// ------------------------
// Parcelable/Serialisabled
// ------------------------

@Composable
fun <T : Parcelable> rememberDialogState(
    data: T?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogState(data, autoSaver(), showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

@Composable
fun <T : Serializable> rememberDialogState(
    data: T?,
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
) = rememberDialogState(data, autoSaver(), showing, buttonPositiveEnabled, buttonNegativeEnabled, dismissAllowed, swipeAllowed)

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