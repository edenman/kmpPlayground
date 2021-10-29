package chat.quill

import com.coffeetrainlabs.kmpplayground.PlaygroundApp

class TestPlaygroundApp : PlaygroundApp() {
  override fun initCoil() {
    // No image loading in robolectric tests.
  }

  override fun initEmojiCompat() {
    // No emojiCompat by default in robolectric tests.
  }
}
