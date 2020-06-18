package com.coffeetrainlabs.kmpplayground

import android.content.Context
import android.graphics.Color
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.transition.doOnEnd
import androidx.core.view.children
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import flow.Direction
import flow.Direction.BACKWARD
import flow.Direction.FORWARD
import flow.Direction.REPLACE
import flow.KeyChanger
import flow.State
import flow.TraversalCallback

class FlowKeyChanger(activity: MainActivity) : KeyChanger {
  private val screenContainer by lazy { activity.findViewById(R.id.screen_container) as ViewGroup }
  override fun changeKey(
    outgoingState: State?,
    incomingState: State,
    direction: Direction,
    incomingContexts: MutableMap<Any, Context>,
    callback: TraversalCallback
  ) {
    val from = outgoingState?.getKey<Screen>()
    val to = incomingState.getKey<Screen>()
    when (direction) {
      FORWARD -> {
        val currentScreenView = screenContainer.children.lastOrNull()
        val clickedRow = (currentScreenView as? HomeScreenView)?.clickedRow
        val newView = buildIncomingView(to, incomingContexts, incomingState)
        screenContainer.addView(newView)
        if (from is HomeScreen && to is DetailScreen && clickedRow != null) {
          doContainerTransform(clickedRow, newView, callback)
        } else {
          callback.onTraversalCompleted()
        }
      }
      BACKWARD -> {
        val detailScreen = screenContainer.children.lastOrNull() as? DetailScreenView
        val homeScreenView = screenContainer.getChildAt(0) as? HomeScreenView
        val clickedRow = homeScreenView?.clickedRow
        if (from is DetailScreen && to is HomeScreen && clickedRow != null && detailScreen != null) {
          doContainerTransform(detailScreen, clickedRow, callback)
          // This kicks off the transition.  Because magic.
          screenContainer.removeViewAt(screenContainer.childCount - 1)
        } else {
          screenContainer.removeViewAt(screenContainer.childCount - 1)
          if (screenContainer.childCount == 0) {
            // We're going backwards and we didn't have old view state: add the incoming view
            val incomingView = buildIncomingView(to, incomingContexts, incomingState)
            screenContainer.addView(incomingView)
          }
          callback.onTraversalCompleted()
        }
      }
      REPLACE -> {
        screenContainer.removeAllViews()
        screenContainer.addView(buildIncomingView(to, incomingContexts, incomingState))
        callback.onTraversalCompleted()
      }
    }
  }

  private fun doContainerTransform(from: View, to: View, callback: TraversalCallback) {
    val now = System.currentTimeMillis()
    val transform = MaterialContainerTransform().apply {
      startView = from
      endView = to
      pathMotion = MaterialArcMotion()
      scrimColor = Color.TRANSPARENT
      duration = 300L
      doOnEnd {
        println("ERICZ ${System.currentTimeMillis() - now}ms elapsed")
        callback.onTraversalCompleted()
      }
    }
    TransitionManager.beginDelayedTransition(screenContainer, transform)
  }

  private fun buildIncomingView(to: Screen, contexts: Map<Any, Context>, state: State): View {
    val layoutResId = to.layoutResId()
    val context = contexts[to]
    val view = LayoutInflater.from(context).inflate(layoutResId, screenContainer, false)
    state.restore(view)
    return view
  }
}
