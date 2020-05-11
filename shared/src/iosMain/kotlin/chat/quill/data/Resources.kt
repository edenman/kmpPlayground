package chat.quill.data

import platform.UIKit.UIColor
import platform.UIKit.labelColor
import platform.UIKit.quaternarySystemFillColor
import platform.UIKit.secondaryLabelColor
import platform.UIKit.systemGray2Color
import platform.UIKit.tertiaryLabelColor

actual class Resources

actual fun Resources.getString(ref: StringRef): CharSequence {
  return "TODO"
}

actual enum class StringRef {
  ExampleStr,
}

actual enum class ColorRef(val uiColor: UIColor) {
  Black(UIColor.blackColor),
  QuaternarySystemFill(UIColor.quaternarySystemFillColor),
  White(UIColor.whiteColor),
  Label(UIColor.labelColor),
  SecondaryLabel(UIColor.secondaryLabelColor),
  TertiaryLabel(UIColor.tertiaryLabelColor),
}
