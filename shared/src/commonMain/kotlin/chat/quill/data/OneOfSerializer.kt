package chat.quill.data

import chat.quill.util.require
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

// TODO get rid of this when https://youtrack.jetbrains.com/issue/KT-25871 drops
expect val <T : Any> KClass<T>.nestedClassesX: Collection<KClass<*>>

private fun <PARENT : Any, CHILD : PARENT> KClass<PARENT>.realNestedClasses(): List<KClass<CHILD>> {
  @Suppress("UNCHECKED_CAST")
  return nestedClassesX.filter { clazz ->
    clazz.simpleName != "Unknown" && clazz.simpleName != "Companion"
  } as List<KClass<CHILD>>
}

open class OneOfSerializer<T : Any>(clazz: KClass<T>, private val unknown: T) : KSerializer<T> {
  private val fieldNameToSerializer by lazy {
    clazz.realNestedClasses()
      .associate { clazz -> clazz.simpleName!!.decapitalize() to clazz.serializer() }
  }

  override fun deserialize(decoder: Decoder): T {
    val input = decoder as? JsonDecoder
      ?: throw SerializationException("This class can be loaded only by Json")
    val tree = input.decodeJsonElement() as? JsonObject
      ?: throw SerializationException("Expected JsonObject")
    fieldNameToSerializer.forEach { (name, serializer) ->
      if (tree.containsKey(name)) {
        return input.json.decodeFromJsonElement(serializer, tree)
      }
    }
    return unknown
  }

  override fun serialize(encoder: Encoder, value: T) {
    val output = encoder as? JsonEncoder
      ?: throw SerializationException("This class can be saved only by Json")

    @Suppress("UNCHECKED_CAST")
    val serializer = value::class.serializer() as KSerializer<T>
    output.encodeJsonElement(output.json.encodeToJsonElement(serializer, value))
  }

  override val descriptor = buildClassSerialDescriptor("OneOfSerializer")
}

interface HasOneOfContents<T> {
  val contents: T
}

open class ContainsOneOfSerializer<CONTAINER : HasOneOfContents<ONEOF>, ONEOF : Any>(
  oneOfClass: KClass<ONEOF>,
  defaultContainerSerializer: KSerializer<CONTAINER>
) : JsonTransformingSerializer<CONTAINER>(defaultContainerSerializer) {
  private val oneOfFieldNames by lazy {
    oneOfClass.realNestedClasses().map { clazz -> clazz.simpleName!!.decapitalize() }
  }

  override fun transformDeserialize(element: JsonElement): JsonElement {
    val map = (element as JsonObject).toMutableMap()
    oneOfFieldNames.forEach { name ->
      if (map.contains(name)) {
        val removed = map.remove(name).require("Contains == true but couldn't remove?")
        map["contents"] = JsonObject(mapOf(name to removed))
        return@forEach
      }
    }
    if (!map.containsKey("contents")) {
      map["contents"] = JsonObject(mapOf())
    }
    return JsonObject(map)
  }

  override fun transformSerialize(element: JsonElement): JsonElement {
    val map = (element as JsonObject).toMutableMap()
    val contents = map.remove("contents").require("No contents to remove?") as JsonObject
    val key = contents.keys.first()
    map[key] = contents[key].require("No value?")
    return JsonObject(map)
  }

  override val descriptor = buildClassSerialDescriptor("ContainsOneOfSerializer")
}
