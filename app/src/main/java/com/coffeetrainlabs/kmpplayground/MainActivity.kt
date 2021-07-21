package com.coffeetrainlabs.kmpplayground

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import com.coffeetrainlabs.kmpplayground.databinding.MainActivityBinding
import flow.Flow
import flow.KeyDispatcher
import flow.KeyParceler

class MainActivity : Activity() {
  private lateinit var binding: MainActivityBinding
  private val flowKeyChanger = FlowKeyChanger(this)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = MainActivityBinding.inflate(layoutInflater)
    setContentView(binding.root)
  }

  override fun attachBaseContext(baseContext: Context) {
    val wrappedContext = Flow.configure(baseContext, this)
      .keyParceler(KotlinParceler())
      .defaultKey(HomeScreen)
      .dispatcher(KeyDispatcher.configure(this, flowKeyChanger).build())
      .install()
    super.attachBaseContext(wrappedContext)
  }

  override fun onBackPressed() {
    if (!Flow.get(this).goBack()) {
      super.onBackPressed()
    }
  }
}

class KotlinParceler : KeyParceler {
  override fun toParcelable(key: Any): Parcelable {
    return key as Parcelable
  }

  override fun toKey(parcelable: Parcelable): Any {
    return parcelable
  }
}
