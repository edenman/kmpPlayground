package chat.quill.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class FooProviderTest {
  @Test
  fun foo() {
    val fooProvider = FooProvider()
    runBlockingTest {
      println("OMGOMGOMGOMGOMGOMGOMG")
      val states = mutableListOf<String>()
      val stateSubscription = launch {
        println("inside launch, calling observe")
        val scope = CoroutineScope(Dispatchers.Unconfined)
        fooProvider.observe(scope)
          .collect { state ->
            println("observable fired, adding to states")
            states.add(state)
          }
        println("collect done")
      }
      println("launch block done")
      assertEquals(1, states.size)
      assertEquals("", states[0])
      stateSubscription.cancel()
    }
  }
}
