package com.coffeetrainlabs.kmpplayground

import android.content.res.Resources
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.annotation.IdRes
import androidx.core.view.children
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import chat.quill.util.require
import com.google.android.material.tabs.TabLayout
import flow.Flow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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


fun ViewGroup.showOnlyChildFadeIn(showMe: View, @IdRes vararg hideViews: Int) {
  children.forEach { child ->
    if (child.alpha != 0f && hideViews.contains(child.id)) {
      child.animate().cancel()
      child.alpha = 0f
    } else if (child.alpha != 1f && child === showMe) {
      child.animate().alpha(1f)
    }
  }
}

fun <T : View> View.findOrInflate(stub: ViewStub, onInflate: ((T) -> Unit)? = null): T {
  @Suppress("UNCHECKED_CAST")
  return stubViewIfInflated<T>(stub) ?: run {
    val inflated = stub.inflate() as T
    onInflate?.invoke(inflated)
    return@run inflated
  }
}

fun <T : View> View.stubViewIfInflated(stub: ViewStub): T? {
  @Suppress("UNCHECKED_CAST")
  return findViewById<T>(stub.inflatedId)
}

// Adapted from TabLayoutMediator.  We don't want the tab-text-updating behavior of that class but
// we do want to synchronize the scrolling+indicator behavior.
fun synchronizeTabsAndViewPager(tabs: TabLayout, viewPager: ViewPager2) {
  tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
    override fun onTabReselected(tab: TabLayout.Tab) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
      viewPager.setCurrentItem(tab.position, true)
    }
  })
  viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
    private var previousScrollState = ViewPager2.SCROLL_STATE_IDLE
    private var scrollState = ViewPager2.SCROLL_STATE_IDLE

    override fun onPageScrollStateChanged(state: Int) {
      previousScrollState = scrollState
      scrollState = state
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
      // Update the indicator if we're not settling after being idle. This is caused
      // from a setCurrentItem() call and will be handled by an animation from
      // onPageSelected() instead.
      val updateIndicator =
        !(scrollState == ViewPager2.SCROLL_STATE_SETTLING && previousScrollState == ViewPager2.SCROLL_STATE_IDLE)
      if (updateIndicator) {
        tabs.setScrollPosition(position, positionOffset, false, updateIndicator)
      }
    }

    override fun onPageSelected(position: Int) {
      if (tabs.selectedTabPosition != position && position < tabs.tabCount) {
        tabs.selectTab(tabs.getTabAt(position))
      }
    }
  })
}

fun View.idName(): String = safeResourceName(id)

fun View.safeResourceName(idToLookup: Int?): String {
  return safeResourceName(resources, idToLookup)
}

private fun safeResourceName(resources: Resources, idToLookup: Int?): String {
  if (idToLookup == null) {
    return "null"
  }
  try {
    return resources.getResourceEntryName(idToLookup)
  } catch (e: Resources.NotFoundException) { // In case the ID doesn't exist.
    return "NotFoundException($idToLookup)"
  }
}

fun View.observeUntilDetach(
  coroutineScope: CoroutineScope,
  thing: suspend CoroutineScope.() -> Unit,
): Job {
  val job = coroutineScope.launch(block = thing)
  when (val tag = tag) {
    is ObserveUntilDetachListener -> tag.add(job)
    null -> {
      val observeUntilDetachListener = ObserveUntilDetachListener(mutableListOf(job))
      addOnAttachStateChangeListener(observeUntilDetachListener)
      this.tag = observeUntilDetachListener
    }
    else -> throw IllegalStateException("observeUntilDetach: ${idName()} already had a tag $tag")
  }
  (tag as? ObserveUntilDetachListener)?.add(job)
  return job
}

fun View.unsubscribeAll() {
  (tag as? ObserveUntilDetachListener)?.unsubscribeFromJobs()
}

private class ObserveUntilDetachListener(val jobs: MutableList<Job>) :
  View.OnAttachStateChangeListener {
  override fun onViewAttachedToWindow(view: View) {}

  override fun onViewDetachedFromWindow(view: View) {
    view.removeOnAttachStateChangeListener(this)
    view.tag = null
    unsubscribeFromJobs()
  }

  fun add(job: Job) {
    jobs.add(job)
  }

  fun unsubscribeFromJobs() {
    jobs.forEach { job -> job.cancel() }
    jobs.clear()
  }
}
