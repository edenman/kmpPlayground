package chat.quill.resources

import platform.UIKit.UIColor
import kotlin.math.min

actual class Resources

actual fun Resources.getString(ref: StringRef): CharSequence {
  return "TODO"
}

actual fun Resources.getParameterizedString(ref: StringRef): ParameterizedStringBuilder {
  return ParameterizedStringBuilder(ref)
}

actual class ParameterizedStringBuilder(val ref: StringRef) {
  actual fun put(key: String, value: CharSequence): ParameterizedStringBuilder {
    return this // TODO
  }

  actual fun putOptional(key: String, value: CharSequence?): ParameterizedStringBuilder {
    return this // TODO
  }

  actual fun format(): CharSequence {
    return "" // TODO
  }
}

actual class BitmapRef {
  actual val width: Int
    get() = TODO("Not yet implemented")
  actual val height: Int
    get() = TODO("Not yet implemented")
}

actual enum class ColorRef {
  MyColorOne,
  MyColorTwo,
}

// TODO this can probably be done better
actual class ColorValue(val uiColor: UIColor)

/**
 * hue: 0-360
 * saturation: 0-100
 * light: 0-100
 */
actual fun colorValueFromHSL(hue: Int, saturation: Int, light: Int): ColorValue {
  val hueF: Double = hue.toDouble() / 360f
  val originalSaturation: Double = saturation.toDouble() / 100f
  val lightness: Double = light.toDouble() / 100f

  val brightnessF: Double = lightness + originalSaturation * min(lightness, 1 - lightness)
  val saturationF: Double = if (brightnessF == 0.0) 0.0 else 2 - 2 * lightness / brightnessF

  val uiColor = UIColor(hue = hueF, saturation = saturationF, brightness = brightnessF, alpha = 1.0)
  return ColorValue(uiColor)
}

actual fun ColorRef.toColorValue(resources: Resources): ColorValue {
  return ColorValue(UIColor()) // TODO
}

actual fun ColorValue.withAlpha(alpha: Float): ColorValue {
  return ColorValue(uiColor.colorWithAlphaComponent(alpha.toDouble()))
}

actual fun getUIMode(resources: Resources): UIMode {
  return UIMode.Light // TODO
}

actual fun CharSequence.italic(): CharSequence {
  // TODO this needs some thought, .styled(with: .font(font.italic())) requires a font which I didn't realize
  return this
}

actual fun CharSequence.withColor(color: ColorValue): CharSequence {
  return this // TODO
}

actual enum class ImageRef {
  MyImageOne,
  MyImageTwo,
}

actual enum class StringRef {
  MyStringOne,
  MyStringTwo,
}
