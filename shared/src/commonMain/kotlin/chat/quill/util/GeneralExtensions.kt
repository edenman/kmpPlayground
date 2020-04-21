package chat.quill.util

fun <T> T?.require(message: String): T {
  return this ?: throw IllegalStateException(message)
}
