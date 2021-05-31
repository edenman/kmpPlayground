package com.coffeetrainlabs.kmpplayground

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ReplacementSpan
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import chat.quill.util.Timber
import chat.quill.util.objectIdentifier
import coil.request.ImageRequest
import com.coffeetrainlabs.kmpplayground.databinding.TacoViewBinding
import java.lang.ref.WeakReference

class TacoView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
  private val binding by viewBinding(TacoViewBinding::bind)

  override fun onFinishInflate() {
    super.onFinishInflate()
    binding.addEmojiButton.setOnClickListener {
      val size = resources.getDimensionPixelSize(R.dimen.padding_2x)
      val span = CustomImageSpan(size)
      imageLoader.enqueue(
        ImageRequest.Builder(context)
          .data("https://media.giphy.com/media/2KFEbDuo2hZTy/giphy.gif")
          .size(size)
          .target(
            object : coil.target.Target {
              override fun onError(error: Drawable?) {
              }

              override fun onSuccess(result: Drawable) {
                Timber.d("ERICZ setting drawable on span ${span.objectIdentifier()}")
                (result as? Animatable)?.let { animatable ->
                  Timber.d("ERICZ calling start on animatable for span ${span.objectIdentifier()}")
                  animatable.start()
                  span.textView?.invalidate()
                }
                span.drawable = result
              }
            }
          )
          .build()
      )
      val withSpan = "X".withSpan(span) as SpannableStringBuilder
      binding.field.append(withSpan)
    }
  }
}

fun CharSequence.withSpan(span: Any, withPriority: Boolean = false): Spannable {
  val spannable = (this as? Spannable) ?: SpannableStringBuilder(this)
  spannable.setSpan(span, 0, spannable.length, withPriority = withPriority)
  return spannable
}

fun Spannable.setSpan(span: Any, start: Int = 0, end: Int = length, withPriority: Boolean = false) {
  var flags = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
  if (withPriority) {
    flags = flags.or(1 shl Spannable.SPAN_PRIORITY_SHIFT and Spannable.SPAN_PRIORITY)
  }
  setSpan(span, start, end, flags)
}

// This is mostly a copy of ImageSpan's logic, with a few tweaks:
// 1) No support for Uris, we know we're always setting a Drawable, so there's no need for WeakRef
// 2) Hardcode the baseline alignment and fix the translation bug when there's no other text.
// 3) Add MarkupSpan implementation for the markup representation of this image
// 4) Allow setting the drawable asynchronously.
class CustomImageSpan(private val size: Int) :
  ReplacementSpan() {
  var drawable: Drawable? = null
    set(value) {
      Timber.d("ERICZ ${objectIdentifier()} got new drawable, invalidating textView ${textView?.objectIdentifier()}")
      field = value!!
      value.setBounds(0, 0, size, size)
      textView?.invalidate()
    }

  private var textViewRef = WeakReference<TextView?>(null)

  var textView: TextView?
    get() = textViewRef.get()
    set(value) {
      textViewRef = WeakReference(value)
      if (drawable != null) {
        // We already loaded the bitmap: invalidate the TextView to make sure it renders
        value?.invalidate()
      }
    }

  override fun getSize(
    paint: Paint,
    text: CharSequence?,
    start: Int,
    end: Int,
    fm: Paint.FontMetricsInt?,
  ): Int {
    val rect = drawable?.bounds ?: Rect(0, 0, size, size)
    Timber.d("ERICZ Span ${objectIdentifier()}.getSize, gonna return ${rect.right}")

    if (fm != null) {
      Timber.d("ERICZ Span ${objectIdentifier()}.getSize setting font metrics")
      fm.ascent = -rect.bottom
      fm.descent = 0

      fm.top = fm.ascent
      fm.bottom = 0
    }

    return rect.right
  }

  override fun draw(
    canvas: Canvas,
    text: CharSequence?,
    start: Int,
    end: Int,
    x: Float,
    top: Int,
    y: Int,
    bottom: Int,
    paint: Paint,
  ) {
    Timber.d("ERICZ Span ${objectIdentifier()}.draw with drawable? ${drawable != null}")
    Timber.w(Throwable(), "ERICZ HI")
    drawable?.let { drawable ->
      // Short-circuit in the case where the _only_ thing in this TextView is this ImageSpan, and just
      // draw the image at 0,0.
      if (bottom - top == drawable.bounds.height()) {
        drawable.draw(canvas)
      } else {
        // There is other text: align to the baseline of the text.
        canvas.save()

        var transY = bottom - drawable.bounds.bottom
        transY -= paint.fontMetricsInt.descent
        canvas.translate(x, transY.toFloat())
        drawable.draw(canvas)
        canvas.restore()
      }
    }
  }
}
