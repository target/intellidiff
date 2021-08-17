package com.target.diff.differ

import java.util.SortedMap
import java.util.SortedSet

interface CollectionInstructions {
  val first: Iterable<*>
  val firstSize: Int
  val second: Iterable<*>
  val secondSize: Int
}

interface MapInstructions {
  val first: Iterable<Map.Entry<*, *>>
  val firstSize: Int
  val second: Iterable<Map.Entry<*, *>>
  val secondSize: Int
}

sealed class ContainerInstructions {
  data class OrderedCollection(
    override val first: Iterable<*>,
    override val firstSize: Int,
    override val second: Iterable<*>,
    override val secondSize: Int
  ) : ContainerInstructions(), CollectionInstructions

  data class UnorderedCollection(
    override val first: Iterable<*>,
    override val firstSize: Int,
    override val second: Iterable<*>,
    override val secondSize: Int
  ) : ContainerInstructions(), CollectionInstructions

  data class OrderedMap(
    override val first: Iterable<Map.Entry<*, *>>,
    override val firstSize: Int,
    override val second: Iterable<Map.Entry<*, *>>,
    override val secondSize: Int
  ) : ContainerInstructions(), MapInstructions

  data class UnorderedMap(
    override val first: Iterable<Map.Entry<*, *>>,
    override val firstSize: Int,
    override val second: Iterable<Map.Entry<*, *>>,
    override val secondSize: Int
  ) : ContainerInstructions(), MapInstructions

  companion object {
    /**
     * Provides instructions on how to evaluate two objects if they are matching containers, otherwise returns null if
     * the types aren't matching.
     */
    internal fun from(first: Any, second: Any): ContainerInstructions? {
      return when {
        first is Array<*> && second is Array<*> -> {
          OrderedCollection(
            first = first.asIterable(),
            firstSize = first.size,
            second = second.asIterable(),
            secondSize = second.size
          )
        }
        first is List<*> && second is List<*> -> {
          OrderedCollection(
            first = first,
            firstSize = first.size,
            second = second,
            secondSize = second.size
          )
        }
        first is SortedSet<*> && second is SortedSet<*> -> {
          OrderedCollection(
            first = first,
            firstSize = first.size,
            second = second,
            secondSize = second.size
          )
        }
        first is Collection<*> && second is Collection<*> -> {
          if (first is List<*> || first is SortedSet<*> || second is List<*> || second is SortedSet<*>) {
            // Don't evaluate type mismatches.
            null
          } else {
            UnorderedCollection(
              first = first,
              firstSize = first.size,
              second = second,
              secondSize = second.size
            )
          }
        }
        first is SortedMap<*, *> && second is SortedMap<*, *> -> {
          OrderedMap(
            first = first.entries,
            firstSize = first.size,
            second = second.entries,
            secondSize = second.size
          )
        }
        first is Map<*, *> && second is Map<*, *> -> {
          if (first is SortedMap<*, *> || second is SortedMap<*, *>) {
            // Don't evaluate type mismatches.
            null
          } else {
            UnorderedMap(
              first = first.entries,
              firstSize = first.size,
              second = second.entries,
              secondSize = second.size
            )
          }
        }
        else -> null
      }
    }
  }
}
