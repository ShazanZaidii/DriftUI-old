package com.example.driftui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.reflect.KProperty


// -----------------------------------------
// STATE<T>
// -----------------------------------------

class Binding<T>(
    val value: T,
    val set: (T) -> Unit
)

class State<T>(initial: T) {
    private val s = mutableStateOf(initial)

    val value get() = s.value

    operator fun getValue(thisRef: Any?, property: KProperty<*>) = s.value
    operator fun setValue(thisRef: Any?, property: KProperty<*>, v: T) { s.value = v }

    fun set(v: T) { s.value = v }

    fun binding() = Binding(s.value) { s.value = it }

    fun toggle() {
        if (s.value is Boolean) {
            // Safe cast and flip the value
            @Suppress("UNCHECKED_CAST")
            s.value = !(s.value as Boolean) as T
        } else {
            // Optional: Log an error or ignore if not a Boolean state
            println("Drift UI Error: .toggle() called on non-Boolean State.")
        }
    }
    fun getSetter(): (T) -> Unit = { newValue ->
        s.value = newValue
    }


}

// -----------------------------------------
// Published<T>
// -----------------------------------------

typealias Published<T> = State<T>


// =============================================================================================
// MVVM (SwiftUI-like)
// =============================================================================================


open class ObservableObject : ViewModel()

@Composable
inline fun <reified T : ObservableObject> StateObject(): T {
    return viewModel<T>()
}

@Composable
inline fun <reified T : ObservableObject> EnvironmentObject(): T {

    val lifecycleOwner = LocalLifecycleOwner.current
    val storeOwner = lifecycleOwner as ViewModelStoreOwner

    // NOTE: Your Compose version supports viewModel(owner)
    return viewModel<T>(storeOwner)
}

@Composable
inline fun <reified T : ObservableObject> ObservedObject(noinline factory: () -> T): T {
    return remember { factory() }
}