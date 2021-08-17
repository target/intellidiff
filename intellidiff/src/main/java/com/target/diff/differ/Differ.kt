package com.target.diff.differ

import com.target.diff.configuration.IntelliDiffConfiguration
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.math.BigDecimal
import java.math.BigInteger
import java.util.Date
import kotlin.reflect.KClass
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.declaredMemberFunctions

internal class Differ(configuration: IntelliDiffConfiguration = IntelliDiffConfiguration.default()) {

  private val valueTypes = mergeValueTypes(configuration.valueTypes())
  private val customHash: MutableMap<KClass<*>, Boolean> = mutableMapOf()
  private val cachedFields: MutableMap<Class<*>, Collection<Field>> = mutableMapOf()

  fun diff(old: Any?, current: Any?): List<Change> {
    val changes: MutableList<Change> = mutableListOf()
    val engine = Engine()

    engine.needsEvaluation(RootNode(pair = Pair(old, current)))

    while (true) {
      val node = engine.next() ?: break
      val pair = node.pair
      if (pair.first === pair.second) {
        continue
      }
      if (pair.first == null) {
        changes.add(Change.ofAdded(node))
        continue
      }
      if (pair.second == null) {
        changes.add(Change.ofRemoved(node))
        continue
      }
      val first = pair.first!!
      val second = pair.second!!
      val containerInstructions = ContainerInstructions.from(first, second)
      when {
        (containerInstructions == null && first.javaClass != second.javaClass) -> {
          changes.add(Change.ofDifferentType(node))
        }
        valueTypes.contains(first::class) || first.javaClass.isEnum -> {
          if (first != second) {
            changes.add(Change.ofValue(node))
          }
        }
        containerInstructions != null -> {
          changes.addAll(evaluateContainer(instructions = containerInstructions, parent = node, engine = engine))
        }
        else -> {
          val fields = getDeclaredFields(first::class.java)
          for (field in fields) {
            engine.needsEvaluation(
              FieldNode(
                pair = Pair(field.get(first), field.get(second)),
                parent = node,
                parentField = field
              )
            )
          }
        }
      }
    }
    return changes
  }

  internal class Engine {
    private val visited: MutableSet<Pair<*, *>> = mutableSetOf()
    private val queue: MutableList<Node> = mutableListOf()

    fun needsEvaluation(node: Node) {
      if (!visited.contains(node.pair)) {
        // This pair hasn't been evaluated yet.
        queue.add(node)
      }
    }

    fun next(): Node? {
      if (queue.isEmpty()) {
        return null
      }
      val node = queue.removeAt(0)
      visited.add(node.pair)
      return node
    }
  }

  private fun evaluateContainer(
    instructions: ContainerInstructions,
    parent: Node,
    engine: Engine
  ): List<Change> {
    return when (instructions) {
      is ContainerInstructions.OrderedCollection -> {
        evaluateOrderedCollections(
          instructions = instructions,
          parent = parent,
          engine = engine
        )
      }
      is ContainerInstructions.UnorderedCollection -> {
        evaluateUnorderedCollections(
          instructions = instructions,
          parent = parent,
          engine = engine
        )
      }
      is ContainerInstructions.OrderedMap -> {
        evaluateOrderedMaps(
          instructions = instructions,
          parent = parent,
          engine = engine
        )
      }
      is ContainerInstructions.UnorderedMap -> {
        evaluateUnorderedMaps(
          instructions = instructions,
          parent = parent,
          engine = engine
        )
      }
    }
  }

  private fun evaluateOrderedCollections(
    instructions: CollectionInstructions,
    parent: Node,
    engine: Engine
  ): List<Change> {
    if (instructions.firstSize != instructions.secondSize) {
      return evaluateUnorderedCollections(
        instructions = instructions,
        parent = parent,
        engine = engine,
        withRecursion = false
      )
    }
    var index = 0
    val secondIterator = instructions.second.iterator()
    instructions.first.forEach {
      engine.needsEvaluation(
        node = KeyNode(
          pair = Pair(it, secondIterator.next()),
          parent = parent,
          key = index
        )
      )
      index++
    }
    return listOf()
  }

  private fun evaluateUnorderedCollections(
    instructions: CollectionInstructions,
    parent: Node,
    engine: Engine,
    withRecursion: Boolean = true
  ): List<Change> {
    val changes: MutableList<Change> = mutableListOf()
    val hasDifferentSize = instructions.firstSize != instructions.secondSize
    val firstCache: MutableMap<Int, Any?> = mutableMapOf()
    val secondCache: MutableMap<Int, Any?> = mutableMapOf()
    instructions.first.forEach {
      firstCache[deepHashCode(it)] = it
    }
    instructions.second.forEach {
      val secondHashCode = deepHashCode(it)
      secondCache[secondHashCode] = it
      if (firstCache[secondHashCode] == null) {
        // We can't find the same object in the first container
        changes.add(Change.ofAdded(node = LeafNode(Pair(null, it), parent)))
      }
    }
    instructions.first.forEach {
      val secondChild = secondCache[deepHashCode(it)]
      if (secondChild == null) {
        // We can't find the same object in the second container
        changes.add(Change.ofRemoved(node = LeafNode(Pair(it, null), parent)))
      } else if (!hasDifferentSize && withRecursion) {
        // Do deeper comparison on the children
        engine.needsEvaluation(node = KeyNode(pair = Pair(it, secondChild), parent = parent, key = it))
      }
    }
    if (hasDifferentSize) {
      return listOf(Change.ofDifferentContainerSize(parent, instructions.firstSize, instructions.secondSize, changes))
    }
    return changes
  }

  private fun evaluateOrderedMaps(
    instructions: MapInstructions,
    parent: Node,
    engine: Engine
  ): List<Change> {
    if (instructions.firstSize != instructions.secondSize) {
      return evaluateUnorderedMaps(
        instructions = instructions,
        parent = parent,
        engine = engine,
        withRecursion = false
      )
    }
    val first = instructions.first
    val secondIterator = instructions.second.iterator()
    first.forEach {
      val secondEntry = secondIterator.next()
      engine.needsEvaluation(node = KeyNode(pair = Pair(it.key, secondEntry.key), parent = parent, key = it.key))
      engine.needsEvaluation(node = KeyNode(pair = Pair(it.value, secondEntry.value), parent = parent, key = it.key))
    }
    return listOf()
  }

  private fun evaluateUnorderedMaps(
    instructions: MapInstructions,
    parent: Node,
    engine: Engine,
    withRecursion: Boolean = true
  ): List<Change> {
    val changes: MutableList<Change> = mutableListOf()
    val hasDifferentSize = instructions.firstSize != instructions.secondSize

    // entry.key hash code -> entry
    // Cache based off of only the key's hashcode for better value diffing
    val firstCache: MutableMap<Int, Map.Entry<*, *>> = mutableMapOf()
    val secondCache: MutableMap<Int, Map.Entry<*, *>> = mutableMapOf()

    instructions.first.forEach {
      firstCache[deepHashCode(it.key)] = it
    }

    instructions.second.forEach {
      val secondHashCode = deepHashCode(it.key)
      secondCache[secondHashCode] = it
      if (firstCache[secondHashCode] == null) {
        // We can't find the same object in the first map
        changes.add(Change.ofAdded(node = LeafNode(Pair(null, it), parent)))
      }
    }
    instructions.first.forEach {
      val secondChild = secondCache[deepHashCode(it.key)]
      if (secondChild == null) {
        // We can't find the same object in the second map
        changes.add(Change.ofRemoved(node = LeafNode(Pair(it, null), parent)))
      } else if (!hasDifferentSize && withRecursion) {
        // Do deeper comparison on the children
        engine.needsEvaluation(node = KeyNode(pair = Pair(it.key, secondChild.key), parent = parent, key = it.key))
        engine.needsEvaluation(node = KeyNode(pair = Pair(it.value, secondChild.value), parent = parent, key = it.key))
      }
    }
    if (hasDifferentSize) {
      return listOf(Change.ofDifferentContainerSize(parent, instructions.firstSize, instructions.secondSize, changes))
    }
    return changes
  }

  private fun getDeclaredFields(clazz: Class<*>): Collection<Field> {
    if (cachedFields.containsKey(clazz)) {
      return cachedFields[clazz]!!
    }
    val fields = ArrayList<Field>()
    var current: Class<*>? = clazz
    while (current != null) {
      val currentFields = current.declaredFields
      for (field in currentFields) {
        if (!field.isAccessible) {
          field.isAccessible = true
        }
        val modifiers = field.modifiers
        // Ignore static / inner classes / transient
        if (!Modifier.isStatic(modifiers) && !field.name.startsWith("this$") && !Modifier.isTransient(modifiers)) {
          fields.add(field)
        }
      }
      current = current.superclass
    }
    cachedFields[clazz] = fields
    return fields
  }

  private fun deepHashCode(any: Any?): Int {
    val visited: MutableSet<Any> = mutableSetOf()
    val stack: MutableList<Any?> = mutableListOf()
    stack.add(any)
    var hash = 0
    while (stack.isNotEmpty()) {
      val obj = stack.removeAt(stack.lastIndex)
      if (obj == null || visited.contains(obj)) {
        continue
      }
      visited.add(obj)
      if (obj is Array<*>) {
        for (current in obj) {
          stack.add(current)
        }
        continue
      }
      if (obj is Collection<*>) {
        hash += obj.hashCode()
        continue
      }
      if (obj is Map<*, *>) {
        hash += obj.hashCode()
        continue
      }
      if (obj is Enum<*>) {
        hash += obj.hashCode()
        continue
      }
      if (hasCustomHashCode(obj::class)) {
        hash += obj.hashCode()
        continue
      }
      val fields = getDeclaredFields(obj::class.java)
      for (field in fields) {
        stack.add(field.get(obj))
      }
    }
    return hash
  }

  private fun hasCustomHashCode(clazz: KClass<*>): Boolean {
    if (valueTypes.contains(clazz)) {
      return true
    }
    if (customHash.containsKey(clazz)) {
      return customHash[clazz]!!
    }
    val allTypes = listOf(clazz) + clazz.allSuperclasses.filter { it.qualifiedName != "kotlin.Any" }
    val hasCustomHashCode = allTypes.any { type ->
      type.declaredMemberFunctions.any { it.name == "hashCode" }
    }
    customHash[clazz] = hasCustomHashCode
    return hasCustomHashCode
  }

  private fun mergeValueTypes(valueTypes: List<KClass<*>>): Set<KClass<*>> {
    val requiredTypes = mutableSetOf(
      Int::class,
      Long::class,
      Short::class,
      Byte::class,
      Boolean::class,
      Char::class,
      Float::class,
      Double::class,
      String::class,
      CharSequence::class,
      Float::class,
      Double::class,
      Enum::class,
      Date::class,
      BigDecimal::class,
      BigInteger::class
    )

    return requiredTypes.apply { addAll(valueTypes) }
  }
}
