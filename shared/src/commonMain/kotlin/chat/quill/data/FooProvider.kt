package chat.quill.data

import kotlinx.coroutines.channels.ConflatedBroadcastChannel

class FooProvider {
  val fooChannel = ConflatedBroadcastChannel<List<Foo>>(listOf())

  fun onClick() {
    val existing = fooChannel.value
    fooChannel.offer(existing.toMutableList().plus(Foo("omg${existing.size}")))
  }
}

data class Foo(val str: String)
