package chat.quill.util

expect object Timber {
  fun e(message: String)
  fun e(t: Throwable, message: String)
  fun w(message: String)
  fun w(t: Throwable, message: String)
  fun d(message: String)
  fun v(message: String)
}

expect fun Any.objectIdentifier(): String
