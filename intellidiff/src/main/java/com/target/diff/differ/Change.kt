package com.target.diff.differ

import java.util.ArrayList

internal sealed class Change {
  data class Value(
    val id: String,
    val old: Any,
    val current: Any
  ) : Change() {
    override fun toString(): String {
      return "${Value::class.simpleName}: $id was: $old now: $current"
    }
  }

  data class Removed(
    val id: String,
    val removed: Any
  ) : Change() {
    override fun toString(): String {
      return "${Removed::class.simpleName}: $id was: $removed now: null"
    }
  }

  data class Added(
    val id: String,
    val added: Any
  ) : Change() {
    override fun toString(): String {
      return "${Added::class.simpleName}: $id was: null now: $added"
    }
  }

  data class DifferentType(
    val id: String,
    val old: Any,
    val current: Any
  ) : Change() {
    override fun toString(): String {
      return "${DifferentType::class.simpleName}: $id was: ${old::class.simpleName} now: ${current::class.simpleName}"
    }
  }

  data class DifferentContainerSize(
    val id: String,
    val old: Any,
    val oldSize: Int,
    val current: Any,
    val currentSize: Int,
    val added: List<Change.Added>,
    val removed: List<Change.Removed>
  ) : Change() {
    override fun toString(): String {
      return "${DifferentContainerSize::class.simpleName}: $id was size: $oldSize now size: $currentSize"
    }
  }

  companion object {
    internal fun ofValue(node: Node): Change {
      return Change.Value(
        id = generateId(node),
        old = node.pair.first!!,
        current = node.pair.second!!
      )
    }

    internal fun ofAdded(node: Node): Change {
      return Change.Added(
        id = generateId(node),
        added = node.pair.second!!
      )
    }

    internal fun ofRemoved(node: Node): Change {
      return Change.Removed(
        id = generateId(node),
        removed = node.pair.first!!
      )
    }

    internal fun ofDifferentContainerSize(
      node: Node,
      oldSize: Int,
      currentSize: Int,
      changes: List<Change>
    ): Change {
      return Change.DifferentContainerSize(
        id = generateId(node),
        old = node.pair.first!!,
        oldSize = oldSize,
        current = node.pair.second!!,
        currentSize = currentSize,
        added = changes.mapNotNull {
          if (it is Added) {
            it
          } else {
            null
          }
        },
        removed = changes.mapNotNull {
          if (it is Removed) {
            it
          } else {
            null
          }
        }
      )
    }

    internal fun ofDifferentType(node: Node): Change {
      return Change.DifferentType(
        id = generateId(node),
        old = node.pair.first!!,
        current = node.pair.second!!
      )
    }

    private fun generateId(child: Node): String {
      val nodes = reverse(child)
      return nodes.joinToString(separator = "") { node ->
        when (node) {
          is RootNode -> node.pair.first?.javaClass?.name ?: node.pair.second?.javaClass?.name ?: "null"
          is FieldNode -> "." + node.parentField.name
          is KeyNode -> "[" + node.key + "]"
          is LeafNode -> "{" + (child.pair.first ?: child.pair.second) + "}"
        }
      }
    }

    private fun reverse(pair: Node): List<Node> {
      var current: Node? = pair
      val nodes = ArrayList<Node>()
      do {
        nodes.add(current!!)
        current = current.parent
      } while (current != null)
      nodes.reverse()
      return nodes
    }
  }
}
