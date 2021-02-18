package chat.quill.util

actual object Timber {
  actual fun d(message: String) {
    timber.log.Timber.d(message)
  }

  actual fun e(message: String) {
    timber.log.Timber.e(message)
  }

  actual fun e(t: Throwable, message: String) {
    timber.log.Timber.e(t, message)
  }

  actual fun w(message: String) {
    timber.log.Timber.w(message)
  }

  actual fun w(t: Throwable, message: String) {
    timber.log.Timber.w(t, message)
  }

  actual fun v(message: String) {
    timber.log.Timber.v(message)
  }
}
