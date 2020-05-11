package chat.quill.data

expect class Resources

expect fun Resources.getString(ref: StringRef): CharSequence

expect enum class StringRef {
  ExampleStr,
}

expect enum class ColorRef {
  Black,
  QuaternarySystemFill,
  White,
  Label,
  SecondaryLabel,
  TertiaryLabel,
}
