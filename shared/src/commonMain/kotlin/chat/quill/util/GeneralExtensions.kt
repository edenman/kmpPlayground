package chat.quill.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <T> T?.require(message: String): T {
  return this ?: throw IllegalStateException(message)
}
