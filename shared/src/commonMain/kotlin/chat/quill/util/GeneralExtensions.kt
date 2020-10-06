package chat.quill.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <T> T?.require(message: String): T {
  return this ?: throw IllegalStateException(message)
}

// Adapted from https://github.com/Kotlin/kotlinx.coroutines/issues/1147#issuecomment-639397185
fun <T, R> Flow<T>.mapOnThread(thread: CoroutineScope, transform: suspend (T) -> R): Flow<R> = this
  .map { thread.async { transform(it) } }
  .map { it.await() }
