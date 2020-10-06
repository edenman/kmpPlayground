package chat.quill.data

import chat.quill.util.mapOnThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FooProvider {
  val fooFlow = MutableStateFlow<List<Foo>>(listOf())

  fun onClick() {
    val existing = fooFlow.value
    fooFlow.value = existing.toMutableList().plus(Foo("omg${existing.size}"))
  }

  fun observe(coroutineScope: CoroutineScope): Flow<String> {
    return fooFlow.mapOnThread(coroutineScope) { list ->
      return@mapOnThread list[-1].toString()
    }
  }
}

data class Foo(val str: String)
