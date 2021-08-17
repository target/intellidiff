package com.target.diff.differ

import org.junit.Assert
import org.junit.Test

class SetTest {
  @Test
  fun `set mutability should not be considered`() {
    val immutable = Container(setOf("see"))
    val mutable = Container(mutableSetOf("see"))
    Assert.assertEquals(0, Differ().diff(immutable, mutable).size)
  }

  @Test
  fun `equal sets should be equal`() {
    val old = Container(setOf("see", "again"))
    val current = Container(setOf("see", "again"))
    Assert.assertEquals(0, Differ().diff(old, current).size)
  }

  @Test
  fun `sets of different sizes should not be equal`() {
    val old = Container(setOf("see", "again"))
    val current = Container(setOf("see"))
    val change = Differ().diff(old, current)[0]
    when (change) {
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
  fun `unsorted sets of different types should not be equal`() {
    val old = Container(setOf("gouda"))
    val current = Container(setOf(5))
    val changes = Differ().diff(old, current)
    Assert.assertTrue(changes.size == 2)
    for (change in changes) {
      when (change) {
        is Change.Added -> {
          Assert.assertEquals(change.added, (current.obj as Set<*>).first())
          Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj{5}")
        }
        is Change.Removed -> {
          Assert.assertEquals(change.removed, (old.obj as Set<*>).first())
          Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj{gouda}")
        }
        else -> {
          Assert.fail("Expecting a added or removed changed types: $change")
        }
      }
    }
  }

  @Test
  fun `sets and other collections should be different types`() {
    val old = Container(setOf("gouda", "mozzarella"))
    val current = Container(listOf("gouda", "mozzarella"))
    val change = Differ().diff(old, current)[0]
    when (change) {
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
  fun `equal sorted sets should be equal`() {
    val old = Container(sortedSetOf("gouda", "mozzarella"))
    val current = Container(sortedSetOf("gouda", "mozzarella"))
    Assert.assertEquals(0, Differ().diff(old, current).size)
  }

  @Test
  fun `sorted sets of different sizes should not be equal`() {
    val old = Container(sortedSetOf("see", "again"))
    val current = Container(sortedSetOf("see"))
    val change = Differ().diff(old, current)[0]
    when (change) {
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
  fun `sorted sets of different types should not be equal`() {
    val old = Container(sortedSetOf("gouda"))
    val current = Container(sortedSetOf(5))
    val change = Differ().diff(old, current)[0]
    when (change) {
      is Change.DifferentType -> {
        Assert.assertEquals(change.old, (old.obj as Set<*>).first())
        Assert.assertEquals(change.current, (current.obj as Set<*>).first())
        Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj[0]")
      }
      else -> {
        Assert.fail("Expecting a different type: $change")
      }
    }
  }

  @Test
  fun `sorted sets and other sets should be different types`() {
    val old = Container(sortedSetOf("gouda", "mozzarella"))
    val current = Container(setOf("gouda", "mozzarella"))
    val change = Differ().diff(old, current)[0]
    when (change) {
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
  fun `multiple sorted set changes should be ordered naturally`() {
    val old = Container(sortedSetOf("a", "b"))
    val current = Container(sortedSetOf("A", "B"))
    val changes = Differ().diff(old, current)
    Assert.assertEquals(2, changes.size)
    when (val change = changes[0]) {
      is Change.Value -> {
        Assert.assertEquals(change.old, "a")
        Assert.assertEquals(change.current, "A")
        Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj[0]")
      }
      else -> {
        Assert.fail("Expecting a different container size: $change")
      }
    }
    when (val change = changes[1]) {
      is Change.Value -> {
        Assert.assertEquals(change.old, "b")
        Assert.assertEquals(change.current, "B")
        Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj[1]")
      }
      else -> {
        Assert.fail("Expecting a different container size: $change")
      }
    }
  }
}
