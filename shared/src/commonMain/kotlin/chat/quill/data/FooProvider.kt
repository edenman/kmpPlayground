package chat.quill.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FooProvider {
  private val fooFlow = MutableStateFlow(listOf(Foo("Add")))

  fun onClick() {
    val existing = fooFlow.value
    fooFlow.value = existing.toMutableList().plus(Foo("omg${existing.size}"))
  }

  fun observe(): Flow<List<String>> = fooFlow.map { foos -> foos.map { foo -> foo.str } }
}

data class Foo(val str: String)

// Adapted from https://github.com/Kotlin/kotlinx.coroutines/issues/1147#issuecomment-639397185
fun <T, R> Flow<T>.mapOnThread(thread: CoroutineScope, transform: suspend (T) -> R): Flow<R> {
  return this
    .map { input -> thread.async { transform(input) } }
    .map { deferred -> deferred.await() }
}
