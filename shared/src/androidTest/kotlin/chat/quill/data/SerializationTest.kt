package chat.quill.data

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SerializationTest {
  val json = Json {
    useAlternativeNames = false
  }

  @Test
  fun taco() {
    val food = json.decodeFromString(Food.serializer(), "{\"taco\":\"yes\"}")
    assertTrue(food is Food.Taco)
    assertEquals("yes", food.taco)
  }

  @Test
  fun burrito() {
    val food = json.decodeFromString(Food.serializer(), "{\"burrito\":\"yes\"}")
    assertTrue(food is Food.Burrito)
    assertEquals("yes", food.burrito)
    val toJson = Json.encodeToString(Food.serializer(), food)
    assertEquals("{\"burrito\":\"yes\"}", toJson)
  }

  @Test
  fun container() {
    val container = json.decodeFromString(FoodContainer.serializer(), "{\"material\": \"plastic\", \"burrito\":\"yes\"}")
    val food = container.contents
    assertTrue(food is Food.Burrito)
    assertEquals("yes", food.burrito)
  }
}
