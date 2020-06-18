package com.coffeetrainlabs.kmpplayground

import android.os.Looper
import android.view.View
import androidx.viewbinding.ViewBinding
import chat.quill.util.require
import flow.Flow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

val immediateMainThread = CoroutineScope(Dispatchers.Main.immediate)
val postToMainThread = CoroutineScope(Dispatchers.Main)
val ioThread = CoroutineScope(Dispatchers.IO)

inline fun <T : ViewBinding> View.viewBinding(crossinline binder: (View) -> T) =
  lazyMainThread { binder.invoke(this) }

fun isMainThread(): Boolean = Looper.getMainLooper() == Looper.myLooper()

fun checkMainThread() {
  check(isMainThread(), { "This method must only be called from the main thread!" })
}

fun <T : Screen> View.currentScreen() =
  // Only ever accessed from main thread.
  lazyMainThread {
    Flow.getKey<T>(this).require("No screen for context on $this?")
  }

fun <T> lazyMainThread(initializer: () -> T): Lazy<T> = LazyMainThreadImpl(initializer)

private object UninitializedValue

/**
 * The normal lazy implementations are _almost_ what we want, but there's no way to enforce a single
 * thread in the lazy(LazyThreadSafetyMode.NONE) case.  So this is just a fork of that class but
 * with a checkMainThread on every access.
 *
 * (The other LazyThreadSafetyMode options aren't ideal on Android: SYNCHRONIZED is expensive and
 * PUBLICATION is nondeterministic)
 */
private class LazyMainThreadImpl<out T>(initializer: () -> T) : Lazy<T> {
  private var initializer: (() -> T)? = initializer
  private var _value: Any? = UninitializedValue

  override val value: T
    get() {
      checkMainThread() // Make sure this value is only ever accessed from the main thread

      val localVal = _value
      if (localVal !== UninitializedValue) {
        @Suppress("UNCHECKED_CAST")
        return localVal as T
      }

      val typedValue = initializer.require("Initializer is required").invoke()
      _value = typedValue
      initializer = null
      return typedValue
    }

  override fun isInitialized(): Boolean = _value !== UninitializedValue

  override fun toString(): String =
    if (isInitialized()) value.toString() else "Lazy value not initialized yet."
}
