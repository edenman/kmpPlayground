package chat.quill.util

// Implementations should use maxEmoji to shortcut processing and return false if the String
// contains more emoji than the max.
interface EmojiDetector {
  fun isAllEmoji(str: String, maxEmoji: Int? = null): Boolean
}
