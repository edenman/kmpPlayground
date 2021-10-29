package chat.quill.util

import android.text.SpannableString
import androidx.emoji2.text.EmojiCompat
import androidx.emoji2.text.TypefaceEmojiSpan

object AndroidEmojiDetector : EmojiDetector {
  // EmojiCompat is as good as it gets for Android emoji detection, but it doesn't have a
  // pure-function mode so we have to "process" the String and then analyze the resulting
  // SpannableString to see if the whole String is emoji.
  override fun isAllEmoji(str: String, maxEmoji: Int?): Boolean {
    if (str.isEmpty()) {
      return false
    }
    val emojiCompat = EmojiCompat.get()
    if (!emojiCompat.isInitialized()) {
      return false
    }
    val replaceStrategyAll =
      EmojiCompat.REPLACE_STRATEGY_ALL // Always replace emoji with span so we can analyze.
    val parsed =
      emojiCompat.process(str, 0, str.length, maxEmoji ?: Int.MAX_VALUE, replaceStrategyAll)
    if (parsed !is SpannableString) {
      return false
    }
    val spans = parsed.getSpans(0, parsed.length, TypefaceEmojiSpan::class.java)
    val isEmojiArr = BooleanArray(parsed.length)
    for (span in spans) {
      val start: Int = parsed.getSpanStart(span)
      val end: Int = parsed.getSpanEnd(span)
      for (idx in start until end) {
        isEmojiArr[idx] = true
      }
    }
    return isEmojiArr.find { isEmoji -> !isEmoji } == null
  }
}

fun EmojiCompat.isInitialized(): Boolean = loadState == EmojiCompat.LOAD_STATE_SUCCEEDED
