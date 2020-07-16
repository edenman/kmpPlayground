package chat.quill.data

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonInput
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonOutput
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

@Serializable(with = FoodSerializer::class)
sealed class Food {
  @Serializable
  data class Taco(val taco: String) : Food()

  @Serializable
  data class Burrito(val burrito: String) : Food()
  object Unknown : Food()
}

fun <PARENT : Any, CHILD : PARENT> KClass<PARENT>.realNestedClasses(): List<KClass<CHILD>> {
  return nestedClasses.filter { clazz -> clazz.simpleName != "Unknown" && clazz.simpleName != "Companion" } as List<KClass<CHILD>>
}

class FoodSerializer : OneOfSerializer<Food>(Food::class, Food.Unknown)

open class OneOfSerializer<T : Any>(clazz: KClass<T>, private val unknown: T) : KSerializer<T> {
  private val fieldNameToSerializer by lazy {
    clazz.realNestedClasses()
      .associate { clazz -> clazz.simpleName!!.decapitalize() to clazz.serializer() }
  }

  override fun deserialize(decoder: Decoder): T {
    val input =
      decoder as? JsonInput ?: throw SerializationException("This class can be loaded only by Json")
    val tree =
      input.decodeJson() as? JsonObject ?: throw SerializationException("Expected JsonObject")
    fieldNameToSerializer.forEach { (name, serializer) ->
      if (tree.containsKey(name)) {
        return input.json.fromJson(serializer, tree)
      }
    }
    return unknown
  }

  override fun serialize(encoder: Encoder, value: T) {
    val output =
      encoder as? JsonOutput ?: throw SerializationException("This class can be saved only by Json")

    @Suppress("UNCHECKED_CAST")
    val serializer = value::class.serializer() as KSerializer<T>
    output.encodeJson(output.json.toJson(serializer, value))
  }

  override val descriptor: SerialDescriptor = SerialDescriptor("OneOfSerializer")
}
