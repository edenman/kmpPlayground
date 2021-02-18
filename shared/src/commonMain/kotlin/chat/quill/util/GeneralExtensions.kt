package chat.quill.util

fun <T> T?.require(message: String): T {
  return this ?: throw IllegalStateException(message)
}

fun <K, V> Map<K, V?>.filterNullValues(): Map<K, V> {
  @Suppress("UNCHECKED_CAST") // Trust me I just removed the nulls
  return filterValues { value -> value != null } as Map<K, V>
}

fun <T> List<T>.truncate(maxLength: Int?): List<T> {
  return if (maxLength != null && size > maxLength) subList(0, maxLength) else this
}

fun <T> List<T>.indexOrNull(predicate: (T) -> Boolean): Int? {
  val idx = indexOfFirst(predicate)
  if (idx == -1) {
    return null
  }
  return idx
}

fun <T> List<T>.withNewItemAt(index: Int, item: T): List<T> {
  val mutable = this.toMutableList()
  mutable.add(index, item)
  return mutable
}
