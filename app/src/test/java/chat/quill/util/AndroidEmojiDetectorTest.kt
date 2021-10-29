package chat.quill.util

import android.app.Application
import android.os.Build
import android.os.Looper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.emoji2.bundled.BundledEmojiCompatConfig
import androidx.emoji2.text.EmojiCompat
import androidx.test.core.app.ApplicationProvider
import chat.quill.TestPlaygroundApp
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
@Config(
  sdk = [Build.VERSION_CODES.P], // So Robolectric doesn't require JDK9+
  application = TestPlaygroundApp::class
)
class AndroidEmojiDetectorTest {
  @get:Rule
  val executorRule = InstantTaskExecutorRule()

  @Test
  fun emoji() {
    val app = ApplicationProvider.getApplicationContext<Application>()
    EmojiCompat.init(BundledEmojiCompatConfig(app))
    Thread.sleep(1000L) // Wait for init to get kicked off
    val emojiCompat = EmojiCompat.get()
    val countDownLatch = CountDownLatch(1)
    emojiCompat.registerInitCallback(
      object : EmojiCompat.InitCallback() {
        override fun onInitialized() {
          countDownLatch.countDown()
        }
      }
    )
    shadowOf(Looper.getMainLooper()).idle()
    assertThat(countDownLatch.await(5L, TimeUnit.SECONDS)).isTrue()
    assertThat(emojiCompat.isInitialized()).isTrue()
    val emojiDetector = AndroidEmojiDetector
    assertThat(emojiDetector.isAllEmoji("")).isFalse()
    assertThat(emojiDetector.isAllEmoji("bro")).isFalse()
    assertThat(emojiDetector.isAllEmoji("\uD83D\uDC4B bro \uD83D\uDC4B")).isFalse()
    assertThat(emojiDetector.isAllEmoji("\uD83D\uDC4B")).isTrue()
    assertThat(emojiDetector.isAllEmoji("\uD83D\uDC4B\uD83D\uDC4B")).isTrue()
  }
}
