package com.coffeetrainlabs.kmpplayground

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import chat.quill.data.Foo
import chat.quill.data.FooProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class MainActivity : Activity() {
  private val fooProvider = FooProvider()
  private var job: Job? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_activity)
    findViewById<View>(R.id.click_me).setOnClickListener { fooProvider.onClick() }
  }

  override fun onResume() {
    super.onResume()
    job = mainThread.launch {
      val fooChannel: ConflatedBroadcastChannel<List<Foo>> = fooProvider.fooChannel
      fooChannel.consumeEach { fooList ->
        findViewById<TextView>(R.id.foos).text = fooList.joinToString { foo -> foo.str }
      }
    }
  }

  override fun onPause() {
    job?.cancel()
    job = null
    super.onPause()
  }
}
