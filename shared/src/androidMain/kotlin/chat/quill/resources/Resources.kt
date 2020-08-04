package chat.quill.resources

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.graphics.ColorUtils
import chat.quill.shareddata.R
import com.squareup.phrase.Phrase
import kotlin.math.roundToInt

actual typealias Resources = android.content.res.Resources

actual fun Resources.getString(ref: StringRef): CharSequence {
  return getString(ref.resID)
}

actual fun Resources.getParameterizedString(ref: StringRef): ParameterizedStringBuilder {
  return Phrase.from(this, ref.resID)
}

actual typealias ParameterizedStringBuilder = Phrase

actual class BitmapRef(val bitmap: Bitmap) {
  actual val width = bitmap.width
  actual val height = bitmap.height
}

actual typealias ColorValue = Int

/**
 * hue: 0-360
 * saturation: 0-100
 * light: 0-100
 */
actual fun colorValueFromHSL(hue: Int, saturation: Int, light: Int): ColorValue {
  val hueF = hue.toFloat()
  val saturationF = saturation.toFloat() / 100f
  val lightF = light.toFloat() / 100f
  return ColorUtils.HSLToColor(floatArrayOf(hueF, saturationF, lightF))
}

actual fun ColorRef.toColorValue(resources: Resources): ColorValue {
  @Suppress("DEPRECATION")
  return resources.getColor(this.resID)
}

actual fun ColorValue.withAlpha(alpha: Float): ColorValue {
  return ColorUtils.setAlphaComponent(this, (255 * alpha).roundToInt())
}

actual fun CharSequence.italic(): CharSequence {
  return withSpan(StyleSpan(Typeface.ITALIC))
}

actual fun CharSequence.withColor(color: ColorValue): CharSequence {
  return withSpan(ForegroundColorSpan(color))
}

fun CharSequence.bold(): CharSequence {
  return withSpan(StyleSpan(Typeface.BOLD))
}

fun CharSequence.strikethrough(): CharSequence {
  return withSpan(StrikethroughSpan())
}

fun CharSequence.withSpan(span: Any): CharSequence {
  val spannable = (this as? Spannable) ?: SpannableStringBuilder(this)
  spannable.setSpan(span)
  return spannable
}

fun Spannable.setSpan(span: Any, start: Int = 0, end: Int = length) {
  setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
}

actual fun getUIMode(resources: Resources): UIMode {
  when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
    Configuration.UI_MODE_NIGHT_YES -> return UIMode.Dark
    else -> return UIMode.Light
  }
}

actual enum class ColorRef(@ColorRes val resID: Int) {
  MyColorOne(R.color.purple200),
  MyColorTwo(R.color.purple500),
}

actual enum class ImageRef(@DrawableRes val resID: Int) {
  MyImageOne(R.drawable.ic_launcher_background),
  MyImageTwo(R.drawable.ic_dummy),
}

actual enum class StringRef(@StringRes val resID: Int) {
  MyStringOne(R.string.my_string_one),
  MyStringTwo(R.string.my_string_two),
}
