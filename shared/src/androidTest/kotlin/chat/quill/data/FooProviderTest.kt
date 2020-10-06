package chat.quill.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import kotlin.test.assertEquals

class FooProviderTest {
  @Test
  fun foo() {
    val fooProvider = FooProvider()
    runBlockingTest {
      val states = mutableListOf<String>()
      val stateSubscription = launch {
        val scope = CoroutineScope(Dispatchers.Unconfined)
        fooProvider.observe(scope)
          .collect { state ->
            states.add(state)
          }
      }
      assertEquals(1, states.size)
      assertEquals("", states[0])
      stateSubscription.cancel()
    }
  }
}
