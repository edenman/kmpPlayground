package chat.quill.data

sealed interface CrossSealedInterface {
  sealed interface WithFoo : CrossSealedInterface {
    val foo: String
  }

  sealed interface WithBar : CrossSealedInterface {
    val bar: String
  }

  sealed class A : CrossSealedInterface {
    data class AFoo(override val foo: String) : A(), WithFoo
    data class ABar(override val bar: String) : A(), WithBar
    data class AFooBar(override val foo: String, override val bar: String) : A(), WithFoo, WithBar
  }

  sealed class B : CrossSealedInterface {
    data class BFoo(override val foo: String) : B(), WithFoo
    data class BBar(override val bar: String) : B(), WithBar
    data class BFooBar(override val foo: String, override val bar: String) : B(), WithFoo, WithBar
  }
}
