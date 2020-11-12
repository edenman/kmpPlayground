package chat.quill.data

import kotlinx.serialization.Serializable

@Serializable(with = FoodSerializer::class)
sealed class Food {
  @Serializable
  data class Taco(val taco: String) : Food()

  @Serializable
  data class Burrito(val burrito: String) : Food()

  object Unknown : Food()
}

class FoodSerializer : OneOfSerializer<Food>(Food::class, Food.Unknown)
