package com.coffeetrainlabs.kmpplayground

import android.app.Application
import android.os.Build
import androidx.emoji2.text.EmojiCompat
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.fetch.VideoFrameFileFetcher
import coil.fetch.VideoFrameUriFetcher
import coil.util.DebugLogger
import timber.log.Timber

lateinit var imageLoader: ImageLoader

@Suppress("unused")
open // AndroidManifest.xml references this
class PlaygroundApp : Application() {
  override fun onCreate() {
    super.onCreate()
    initCoil()
    Timber.plant(Timber.DebugTree())
    initEmojiCompat()
  }

  open fun initCoil() {
    imageLoader = createImageLoader(this)
  }

  open fun initEmojiCompat() {
    EmojiCompat.init(this)
  }

  private fun createImageLoader(app: PlaygroundApp): ImageLoader {
    return ImageLoader.Builder(app)
      .crossfade(true)
      .logger(DebugLogger())
      .bitmapPoolingEnabled(false) // https://github.com/coil-kt/coil/issues/546
      .componentRegistry {
        // animated GIF support.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
          add(ImageDecoderDecoder(app))
        } else {
          add(GifDecoder())
        }
        add(SvgDecoder(app))
        add(VideoFrameUriFetcher(app))
        add(VideoFrameFileFetcher(app))
      }
      .build()
  }
}
