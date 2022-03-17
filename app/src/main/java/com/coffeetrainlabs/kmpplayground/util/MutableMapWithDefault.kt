package com.coffeetrainlabs.kmpplayground.util

/**
 * Kotlin has a few helpers that _try_ to do this, but they all have flaws:
 * - withDefault returns you a default value but doesn't put that default value in the map AND it still has a nullable
 *   return type for "get".
 * - getOrDefault has the same "doesn't insert" issue.
 * - getOrPut is the behavior we want but it's noisy and hard to enforce across all consumers of this class
 * The only way to get null-safety and insertion (the behavior you want for an on-demand cache) is to make our own type.
 *
 * Note: MutableMapWithDefault would be a nicer name for this but there's an IDE bug where it doesn't get auto-imported
 * because there's a private interface with that name in the kotlin stdlib.
 */
class MutableMapWithDefault<K, V> private constructor(
  private val initializer: (K) -> V,
  private val actual: MutableMap<K, V>
) : MutableMap<K, V> by actual {
  constructor(initializer: (K) -> V) : this(initializer, mutableMapOf())

  /**
   * This is the crux of the whole operation (note that we're returning a `V` instead of a `V?`)
   */
  override fun get(key: K): V = actual.getOrPut(key) { initializer.invoke(key) }
}
