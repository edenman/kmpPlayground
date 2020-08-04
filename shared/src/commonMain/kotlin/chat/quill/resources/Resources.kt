package chat.quill.resources

expect class Resources

expect fun Resources.getString(ref: StringRef): CharSequence

expect fun Resources.getParameterizedString(ref: StringRef): ParameterizedStringBuilder

expect class ParameterizedStringBuilder {
  fun put(key: String, value: CharSequence): ParameterizedStringBuilder
  fun putOptional(key: String, value: CharSequence?): ParameterizedStringBuilder
  fun format(): CharSequence
}

expect class BitmapRef {
  val width: Int
  val height: Int
}

expect enum class ColorRef {
  MyColorOne,
  MyColorTwo,
}

expect class ColorValue

/**
 * hue: 0-360
 * saturation: 0-100
 * light: 0-100
 */
expect fun colorValueFromHSL(hue: Int, saturation: Int, light: Int): ColorValue

expect fun ColorRef.toColorValue(resources: Resources): ColorValue

expect fun ColorValue.withAlpha(alpha: Float): ColorValue

expect fun getUIMode(resources: Resources): UIMode

enum class UIMode { Light, Dark }

expect fun CharSequence.italic(): CharSequence

expect fun CharSequence.withColor(color: ColorValue): CharSequence

// This is clunky but I don't want to add hundreds of custom images to the enum.
sealed class PotentiallyDynamicImageRef {
  data class Fixed(val imageRef: ImageRef) : PotentiallyDynamicImageRef()
  data class Dynamic(val name: String, val fallback: ImageRef) : PotentiallyDynamicImageRef()
}

expect enum class ImageRef {
  MyImageOne,
  MyImageTwo,
}

expect enum class StringRef {
  MyStringOne,
  MyStringTwo,
}

