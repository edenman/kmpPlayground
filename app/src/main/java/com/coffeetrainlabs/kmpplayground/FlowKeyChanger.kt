package com.coffeetrainlabs.kmpplayground

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.transition.Transition.TransitionListener
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import flow.Direction
import flow.Direction.BACKWARD
import flow.Direction.FORWARD
import flow.Direction.REPLACE
import flow.KeyChanger
import flow.State
import flow.TraversalCallback
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class FlowKeyChanger(activity: MainActivity) : KeyChanger {
  private val screenContainer by lazy { activity.findViewById(R.id.screen_container) as ViewGroup }
  override fun changeKey(
    outgoingState: State?,
    incomingState: State,
    direction: Direction,
    incomingContexts: MutableMap<Any, Context>,
    callback: TraversalCallback
  ) {
    val to = incomingState.getKey<Screen>()
    when (direction) {
      FORWARD -> {
        val newView = buildIncomingView(to, incomingContexts, incomingState)
        screenContainer.addView(newView)
        callback.onTraversalCompleted()
      }
      BACKWARD -> {
        screenContainer.removeViewAt(screenContainer.childCount - 1)
        if (screenContainer.childCount == 0) {
          // We're going backwards and we didn't have old view state: add the incoming view
          val incomingView = buildIncomingView(to, incomingContexts, incomingState)
          screenContainer.addView(incomingView)
        }
        callback.onTraversalCompleted()
      }
      REPLACE -> {
        screenContainer.removeAllViews()
        screenContainer.addView(buildIncomingView(to, incomingContexts, incomingState))
        callback.onTraversalCompleted()
      }
    }
  }

  private fun doContainerTransform(from: View, to: View): Flow<Boolean> {
    val flow = Channel<Boolean>()
    val now = System.currentTimeMillis()
    val transform = MaterialContainerTransform().apply {
      startView = from
      endView = to
      setPathMotion(MaterialArcMotion())
      scrimColor = Color.TRANSPARENT
      duration = 300L
      addListener(object : TransitionListener {
        override fun onTransitionEnd(transition: androidx.transition.Transition) {
          println("ERICZ ${System.currentTimeMillis() - now}ms elapsed")
          flow.offer(true)
          flow.close()
        }

        override fun onTransitionResume(transition: androidx.transition.Transition) {
        }

        override fun onTransitionPause(transition: androidx.transition.Transition) {
        }

        override fun onTransitionCancel(transition: androidx.transition.Transition) {
          flow.offer(false)
          flow.close()
        }

        override fun onTransitionStart(transition: androidx.transition.Transition) {
        }
      })
    }
    TransitionManager.beginDelayedTransition(screenContainer, transform)
    return flow.receiveAsFlow()
  }

  private fun buildIncomingView(to: Screen, contexts: Map<Any, Context>, state: State): View {
    val layoutResId = to.layoutResId()
    val context = contexts[to]
    val view = LayoutInflater.from(context).inflate(layoutResId, screenContainer, false)
    state.restore(view)
    return view
  }
}
