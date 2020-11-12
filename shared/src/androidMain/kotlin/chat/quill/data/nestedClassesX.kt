package chat.quill.data

import app.cash.exhaustive.Exhaustive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlin.reflect.KClass

actual val <T : Any> KClass<T>.nestedClassesX: Collection<KClass<*>>
  get() = nestedClasses

private val foodFlow = MutableStateFlow<Food>(Food.Unknown)

fun doSomething(): Flow<String> {
  return foodFlow.map { food ->
    @Exhaustive
    when (food) {
      is Food.Burrito -> return@map "burr"
      is Food.Taco -> return@map "tacooooooos"
      Food.Unknown -> TODO()
    }
  }
}
