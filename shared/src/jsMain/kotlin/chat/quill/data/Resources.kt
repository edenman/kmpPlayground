package chat.quill.data

actual class Resources

actual fun Resources.getString(ref: StringRef): CharSequence {
  return "TODO"
}

actual enum class StringRef {
  ExampleStr,
}

actual enum class ColorRef {
  Black, QuaternarySystemFill, White, Label, SecondaryLabel, TertiaryLabel,
}
