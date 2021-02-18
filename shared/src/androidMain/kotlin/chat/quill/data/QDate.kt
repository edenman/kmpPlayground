package chat.quill.data

import android.annotation.SuppressLint
import chat.quill.util.require
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

val calendarThreadLocal = KotlinNonNullableThreadLocal { Calendar.getInstance() }

@delegate:SuppressLint("SimpleDateFormat")
private val iso8601Format by lazy { SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX") }

actual class QDate actual constructor(actual val apiDate: Long) : Date(apiDate) {
  actual fun iso8601(): String {
    return iso8601Format.format(this)
  }

  actual fun getHourOfDay(): Int {
    val cal = calendarThreadLocal.get()
    cal.time = this
    return cal.get(Calendar.HOUR_OF_DAY)
  }

  actual fun truncateTime(): QDate {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return QDate(calendar.time.time)
  }
}

actual fun now(): QDate {
  return QDate(System.currentTimeMillis())
}

class KotlinNonNullableThreadLocal<T>(private val create: () -> T) : ThreadLocal<T>() {
  override fun initialValue(): T = create.invoke()
  override fun get(): T = super.get().require("No value set?")
}
