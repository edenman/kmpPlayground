package com.coffeetrainlabs.kmpplayground

import android.content.Context
import android.graphics.Canvas
import android.net.Uri
import android.text.Editable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.util.component1
import androidx.core.util.component2
import androidx.core.view.ContentInfoCompat
import androidx.core.view.OnReceiveContentListener
import androidx.core.view.ViewCompat
import chat.quill.util.require
import timber.log.Timber

class PlaygroundEditText(context: Context, attrs: AttributeSet?) :
  AppCompatEditText(context, attrs), OnReceiveContentListener {
  init {
    // https://developer.android.com/about/versions/12/features/unified-content-api
    ViewCompat.setOnReceiveContentListener(this, arrayOf("image/*", "video/*"), this)
  }

  override fun onDraw(canvas: Canvas?) {
    Timber.d("SPANZ calling EditText.onDraw")
    super.onDraw(canvas)
  }

  var onUriAttached: ((Uri) -> Unit)? = null

  override fun getText(): Editable {
    return super.getText().require("No EditText.text?")
  }

  override fun onReceiveContent(view: View, payload: ContentInfoCompat): ContentInfoCompat? {
    val uriHandler = onUriAttached
    if (uriHandler != null) {
      val (hasUri: ContentInfoCompat?, remaining: ContentInfoCompat?) = payload.partition { item ->
        item.uri != null
      }
      if (hasUri != null) {
        val clip = hasUri.clip
        val numItems = clip.itemCount
        Timber.d("onReceiveContent fired with $numItems uris, ${remaining?.clip?.itemCount} remain")
        (0 until numItems).forEach { idx ->
          clip.getItemAt(idx).uri?.let { uri ->
            uriHandler.invoke(uri)
          }
        }
      }
      return remaining
    }
    return payload
  }
}
