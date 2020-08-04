package chat.quill.data

import kotlin.reflect.KClass

actual val <T : Any> KClass<T>.nestedClassesX: Collection<KClass<*>>
  get() = TODO("Not yet implemented")
