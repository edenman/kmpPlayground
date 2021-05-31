package com.coffeetrainlabs.kmpplayground

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import timber.log.Timber

class PlaygroundEditText(context: Context, attrs: AttributeSet?) :
  AppCompatEditText(context, attrs) {
  override fun onDraw(canvas: Canvas?) {
    Timber.d("SPANZ calling EditText.onDraw")
    super.onDraw(canvas)
  }
}
