package com.target.diff.differ

import org.junit.Assert
import org.junit.Test

class ListTest {

  @Test
  fun `list mutability should not be considered`() {
    val immutable = Container(listOf("see"))
    val mutable = Container(mutableListOf("see"))
    Assert.assertEquals(0, Differ().diff(immutable, mutable).size)
  }

  @Test
  fun `equal lists should be equal`() {
    val old = Container(listOf("see", "again"))
    val current = Container(listOf("see", "again"))
    Assert.assertEquals(0, Differ().diff(old, current).size)
  }

  @Test
  fun `list of different sizes should not be equal`() {
    val old = Container(listOf("see", "again"))
    val current = Container(listOf("see"))
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
  fun `lists of different types should not be equal`() {
    val old = Container(listOf("gouda"))
    val current = Container(listOf(5))
    val change = Differ().diff(old, current)[0]
    when (change) {
      is Change.DifferentType -> {
        Assert.assertEquals(change.old, (old.obj as List<*>)[0])
        Assert.assertEquals(change.current, (current.obj as List<*>)[0])
        Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj[0]")
      }
      else -> {
        Assert.fail("Expecting a different type: $change")
      }
    }
  }

  @Test
  fun `different collections should be different types`() {
    val old = Container(arrayOf("gouda", "mozzarella"))
    val current = Container(listOf("gouda", "mozzarella"))
    val change = Differ().diff(old, current)[0]
    when (change) {
      is Change.DifferentType -> {
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
  fun `multiple list changes should be ordered naturally`() {
    val old = Container(listOf("a", "b", "c", "d"))
    val current = Container(listOf("a", "B", "C", "d"))
    val changes = Differ().diff(old, current)
    Assert.assertEquals(2, changes.size)
    when (val change = changes[0]) {
      is Change.Value -> {
        Assert.assertEquals(change.old, "b")
        Assert.assertEquals(change.current, "B")
        Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj[1]")
      }
      else -> {
        Assert.fail("Expecting a different type: $change")
      }
    }
    when (val change = changes[1]) {
      is Change.Value -> {
        Assert.assertEquals(change.old, "c")
        Assert.assertEquals(change.current, "C")
        Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj[2]")
      }
      else -> {
        Assert.fail("Expecting a different type: $change")
      }
    }
  }

  @Test
  fun `multiple nested list changes should be ordered naturally`() {
    val old = Container(listOf("a", listOf("x", "y"), "b", listOf("i", "j")))
    val current = Container(listOf("A", listOf("X", "y"), "B", listOf("I", "j")))
    val changes = Differ().diff(old, current)
    Assert.assertEquals(4, changes.size)
    when (val change = changes[0]) {
      is Change.Value -> {
        Assert.assertEquals(change.old, "a")
        Assert.assertEquals(change.current, "A")
        Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj[0]")
      }
      else -> {
        Assert.fail("Expecting a different type: $change")
      }
    }
    when (val change = changes[1]) {
      is Change.Value -> {
        Assert.assertEquals(change.old, "b")
        Assert.assertEquals(change.current, "B")
        Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj[2]")
      }
      else -> {
        Assert.fail("Expecting a different type: $change")
      }
    }
    when (val change = changes[2]) {
      is Change.Value -> {
        Assert.assertEquals(change.old, "x")
        Assert.assertEquals(change.current, "X")
        Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj[1][0]")
      }
      else -> {
        Assert.fail("Expecting a different type: $change")
      }
    }
    when (val change = changes[3]) {
      is Change.Value -> {
        Assert.assertEquals(change.old, "i")
        Assert.assertEquals(change.current, "I")
        Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj[3][0]")
      }
      else -> {
        Assert.fail("Expecting a different type: $change")
      }
    }
  }
}
