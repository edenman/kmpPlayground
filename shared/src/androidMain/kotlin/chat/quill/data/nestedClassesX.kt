package chat.quill.data

import kotlin.reflect.KClass

actual val <T : Any> KClass<T>.nestedClassesX: Collection<KClass<*>>
  get() = nestedClasses

actual val <T : Any> KClass<T>.primaryConstructor: QConstructor<T>
  get() = object : QConstructor<T> {
    private val constructor by lazy { constructors.first() }
    override fun invoke(vararg args: Any?): T {
      return constructor.call(args)
    }
  }
