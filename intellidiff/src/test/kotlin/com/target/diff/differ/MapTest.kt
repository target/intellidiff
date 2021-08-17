package com.target.diff.differ

import org.junit.Assert
import org.junit.Test

class MapTest {

  @Test
  fun `map mutability should not be considered`() {
    val immutable = Container(mapOf("see" to "see"))
    val mutable = Container(mutableMapOf("see" to "see"))
    Assert.assertEquals(0, Differ().diff(immutable, mutable).size)
  }

  @Test
  fun `equal maps should be equal`() {
    val old = Container(mapOf("see" to "see"))
    val current = Container(mapOf("see" to "see"))
    Assert.assertEquals(0, Differ().diff(old, current).size)
  }

  @Test
  fun `maps of different sizes should not be equal`() {
    val old = Container(mapOf("see" to "see", "again" to "again"))
    val current = Container(mapOf("see" to "see"))
    when (val change = Differ().diff(old, current).single()) {
      is Change.DifferentContainerSize -> {
        Assert.assertEquals(change.old, old.obj)
        Assert.assertEquals(change.current, current.obj)
        Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj")
      }
      else -> {
        Assert.fail("Expecting a different container size: $change")
      }
    }
  }

  @Test
  fun `maps of different types should not be equal`() {
    val old = Container(mapOf("gouda" to "gouda"))
    val current = Container(mapOf(5 to 5))
    val (add, remove) = Differ().diff(old, current)
    when (add) {
      is Change.Added -> {
        Assert.assertEquals(add.id, "com.target.diff.differ.Container.obj{5=5}")
      }
      else -> {
        Assert.fail("Expecting a different type: $add")
      }
    }
    when (remove) {
      is Change.Removed -> {
        Assert.assertEquals(remove.id, "com.target.diff.differ.Container.obj{gouda=gouda}")
      }
      else -> {
        Assert.fail("Expecting a different type: $remove")
      }
    }
  }

  @Test
  fun `maps and other collections should be different types`() {
    val old = Container(mapOf("see" to "see"))
    val current = Container(listOf("gouda", "mozzarella"))
    when (val change = Differ().diff(old, current).single()) {
      is Change.DifferentType -> {
        Assert.assertEquals(change.old, old.obj)
        Assert.assertEquals(change.current, current.obj)
        Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj")
      }
      else -> {
        Assert.fail("Expecting a different type: $change")
      }
    }
  }

  @Test
  fun `equal sorted maps should be equal`() {
    val old = Container(sortedMapOf("see" to "see", "here" to "there"))
    val current = Container(sortedMapOf("see" to "see", "here" to "there"))
    Assert.assertEquals(0, Differ().diff(old, current).size)
  }

  @Test
  fun `sorted maps of different sizes should not be equal`() {
    val old = Container(sortedMapOf("see" to "see", "here" to "there"))
    val current = Container(sortedMapOf("see" to "see"))
    when (val change = Differ().diff(old, current).single()) {
      is Change.DifferentContainerSize -> {
        Assert.assertEquals(change.old, old.obj)
        Assert.assertEquals(change.current, current.obj)
        Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj")
      }
      else -> {
        Assert.fail("Expecting a different container size: $change")
      }
    }
  }

  @Test
  fun `sorted maps of different types should not be equal`() {
    val old = Container(sortedMapOf("key" to "value"))
    val current = Container(sortedMapOf(9 to 5))
    val (keyChange, valueChange) = Differ().diff(old, current)
    when (valueChange) {
      is Change.DifferentType -> {
        Assert.assertEquals(valueChange.old, "value")
        Assert.assertEquals(valueChange.current, 5)
        Assert.assertEquals(valueChange.id, "com.target.diff.differ.Container.obj[key]")
      }
      else -> {
        Assert.fail("Expecting a different type: $valueChange")
      }
    }
    when (keyChange) {
      is Change.DifferentType -> {
        Assert.assertEquals(keyChange.old, "key")
        Assert.assertEquals(keyChange.current, 9)
        Assert.assertEquals(keyChange.id, "com.target.diff.differ.Container.obj[key]")
      }
      else -> {
        Assert.fail("Expecting a different type: $keyChange")
      }
    }
  }

  @Test
  fun `sorted maps and other maps should be different types`() {
    val old = Container(sortedMapOf("see" to "see"))
    val current = Container(mapOf("see" to "see"))
    when (val change = Differ().diff(old, current).single()) {
      is Change.DifferentType -> {
        Assert.assertEquals(change.old, old.obj)
        Assert.assertEquals(change.current, current.obj)
        Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj")
      }
      else -> {
        Assert.fail("Expecting a different type: $change")
      }
    }
  }

  @Test
  fun `unordered maps with same keys but different values should diff values`() {
    val old = Container(mapOf("a" to listOf(1, 2, 3)))
    val current = Container(mapOf("a" to listOf(1, 20, 3)))
    when (val change = Differ().diff(old, current).single()) {
      is Change.Value -> {
        Assert.assertEquals(change.old, 2)
        Assert.assertEquals(change.current, 20)
        Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj[a][1]")
      }
      else -> {
        Assert.fail("Expecting a different type: $change")
      }
    }
  }

  @Test
  fun `maps with null keys should have the correct change id`() {
    val old = Container(mapOf(null to Pair(1, mapOf(null to 2))))
    val current = Container(mapOf(null to Pair(1, mapOf(null to 20))))
    when (val change = Differ().diff(old, current).single()) {
      is Change.Value -> {
        Assert.assertEquals(change.old, 2)
        Assert.assertEquals(change.current, 20)
        Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj[null].second[null]")
      }
      else -> {
        Assert.fail("Expecting a different type: $change")
      }
    }
  }

  @Test
  fun `multiple sorted map changes should be ordered naturally`() {
    val old = Container(sortedMapOf("a" to "0", "b" to "1"))
    val current = Container(sortedMapOf("A" to "1", "B" to "2"))
    val changes = Differ().diff(old, current)
    Assert.assertEquals(4, changes.size)
    when (val change = changes[0]) {
      is Change.Value -> {
        Assert.assertEquals(change.old, "a")
        Assert.assertEquals(change.current, "A")
        Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj[a]")
      }
      else -> {
        Assert.fail("Expecting a different type: $change")
      }
    }
    when (val change = changes[1]) {
      is Change.Value -> {
        Assert.assertEquals(change.old, "0")
        Assert.assertEquals(change.current, "1")
        Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj[a]")
      }
      else -> {
        Assert.fail("Expecting a different type: $change")
      }
    }
    when (val change = changes[2]) {
      is Change.Value -> {
        Assert.assertEquals(change.old, "b")
        Assert.assertEquals(change.current, "B")
        Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj[b]")
      }
      else -> {
        Assert.fail("Expecting a different type: $change")
      }
    }
    when (val change = changes[3]) {
      is Change.Value -> {
        Assert.assertEquals(change.old, "1")
        Assert.assertEquals(change.current, "2")
        Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj[b]")
      }
      else -> {
        Assert.fail("Expecting a different type: $change")
      }
    }
  }

  @Test
  fun `multiple unsorted map changes should be ordered naturally`() {
    val old = Container(mapOf("a" to "0", "b" to "1"))
    val current = Container(mapOf("a" to "1", "b" to "2"))
    val changes = Differ().diff(old, current)
    Assert.assertEquals(2, changes.size)
    when (val change = changes[0]) {
      is Change.Value -> {
        Assert.assertEquals(change.old, "0")
        Assert.assertEquals(change.current, "1")
        Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj[a]")
      }
      else -> {
        Assert.fail("Expecting a different type: $change")
      }
    }
    when (val change = changes[1]) {
      is Change.Value -> {
        Assert.assertEquals(change.old, "1")
        Assert.assertEquals(change.current, "2")
        Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj[b]")
      }
      else -> {
        Assert.fail("Expecting a different type: $change")
      }
    }
  }
}
