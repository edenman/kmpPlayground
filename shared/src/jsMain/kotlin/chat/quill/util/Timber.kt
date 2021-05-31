package chat.quill.util

// TODO
actual object Timber {
  actual fun e(message: String) {
  }

  actual fun e(t: Throwable, message: String) {
  }

  actual fun d(message: String) {
  }

  actual fun w(message: String) {
  }

  actual fun w(t: Throwable, message: String) {
  }

  actual fun v(message: String) {
  }
}

actual fun Any.objectIdentifier(): String = TODO()
