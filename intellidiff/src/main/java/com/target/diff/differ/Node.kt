package com.target.diff.differ

import java.lang.reflect.Field

internal sealed class Node {
  abstract val pair: Pair<*, *>
  abstract val parent: Node?
}

/** Node that contains the top-level values being compared. */
internal data class RootNode(override val pair: Pair<*, *>) : Node() {
  override val parent: Node? get() = null
}

/** Node for values which cannot be compared more deeply. */
internal data class LeafNode(
  override val pair: Pair<*, *>,
  override val parent: Node
) : Node()

/** Node who's values are stored in a [Field]. */
internal data class FieldNode(
  override val pair: Pair<*, *>,
  override val parent: Node,
  val parentField: Field
) : Node()

/**
 * Node who's values are stored in a container.
 *
 * * For ordered collections (i.e. [List]), this is the index into the container.
 * * For unordered collections (i.e. [Set]) this is the old value.
 * * For [Map], this is the key for the value that changed, or the old key if key changed.
 */
internal data class KeyNode(
  override val pair: Pair<*, *>,
  override val parent: Node,
  val key: Any?
) : Node()
