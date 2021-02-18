package chat.quill.data

// TODO actually implement as it makes sense for Swift
@Suppress("CanBeParameter")
actual class QDate actual constructor(actual val apiDate: Long) {
  actual fun iso8601(): String = "" // TODO
  actual fun getHourOfDay(): Int = TODO()
  actual fun truncateTime(): QDate = TODO()
}

actual fun now(): QDate {
// TODO actually implement as it makes sense for Swift
  return QDate(0)
}
