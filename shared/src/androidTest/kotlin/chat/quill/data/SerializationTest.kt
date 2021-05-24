package chat.quill.data

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SerializationTest {
  @Test
  fun taco() {
    val food = Json.decodeFromString(Food.serializer(), "{\"taco\":\"yes\"}")
    assertTrue(food is Food.Taco)
    assertEquals("yes", food.taco)
  }

  @Test
  fun burrito() {
    val food = Json.decodeFromString(Food.serializer(), "{\"burrito\":\"yes\"}")
    assertTrue(food is Food.Burrito)
    assertEquals("yes", food.burrito)
    val toJson = Json.encodeToString(Food.serializer(), food)
    assertEquals("{\"burrito\":\"yes\"}", toJson)
  }

  @Test
  fun container() {
    val container = Json.decodeFromString(FoodContainer.serializer(), "{\"material\": \"plastic\", \"contents\": {\"burrito\":\"yes\"}}")
    val food = container.contents
    assertTrue(food is Food.Burrito)
    assertEquals("yes", food.burrito)
  }
}
