@file:UseSerializers(QDateNullableSerializer::class, QDateSerializer::class)
package chat.quill.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers

@Serializable(with = FoodSerializer::class)
sealed class Food {
  @Serializable
  data class Taco(val taco: String) : Food()

  @Serializable
  data class Burrito(val burrito: String) : Food()
  object Unknown : Food()
}

class FoodSerializer : OneOfSerializer<Food>(Food::class, Food.Unknown)

@Serializable
data class Dogs(
  val good: Boolean = false,
  val veryGood: Boolean = false,
  val theBest: Boolean = false,
  val actualNumberOne: Boolean = false,
  val allUniverseSquad: Boolean = false,
  val goat: Boolean = false,
) {
  @Transient
  val theyreAllGoodDogs = good ||
      veryGood ||
      theBest ||
      actualNumberOne ||
      allUniverseSquad ||
      goat
}

@Serializer(forClass = FoodContainer::class)
private object DefaultFoodContainerSerializer

object FoodContainerSerializer :
  ContainsOneOfSerializer<FoodContainer, Food>(
    Food::class,
    DefaultFoodContainerSerializer
  )

@Serializable(with = FoodContainerSerializer::class)
data class FoodContainer(
  val material: String,
  val size: Int,
  override val contents: Food,
) : HasOneOfContents<Food>
