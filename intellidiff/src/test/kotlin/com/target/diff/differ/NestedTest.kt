package com.target.diff.differ

import org.junit.Assert
import org.junit.Test

class NestedTest {

  data class SomeComplexClass(
    val string: String,
    val list: List<String>,
    val middleClass: MiddleClass
  )

  data class MiddleClass(
    val innerClass: InnerClass?,
    val boolean: Boolean
  )

  data class InnerClass(
    val value: Int
  )

  private val old = SomeComplexClass(
    string = ":wave:",
    list = listOf("something", "here"),
    middleClass = MiddleClass(
      boolean = true,
      innerClass = InnerClass(
        value = 100
      )
    )
  )

  @Test
  fun `two equal data classes should be equal`() {
    Assert.assertEquals(0, Differ().diff(old, old.copy()).size)
  }

  @Test
  fun `can return multiple differences`() {
    val current = SomeComplexClass(
      string = ":wave:",
      list = listOf("something", "here", "different"),
      middleClass = MiddleClass(
        boolean = false,
        innerClass = null
      )
    )
    val changes = Differ().diff(old, current)
    Assert.assertEquals(3, changes.size)
    for (change in changes) {
      when (change) {
        is Change.Value -> {
          Assert.assertEquals(change.old, old.middleClass.boolean)
          Assert.assertEquals(change.current, current.middleClass.boolean)
          Assert.assertEquals(change.id, "com.target.diff.differ.NestedTest\$SomeComplexClass.middleClass.boolean")
        }
        is Change.DifferentContainerSize -> {
          Assert.assertEquals(change.old, old.list)
          Assert.assertEquals(change.current, current.list)
          Assert.assertEquals(change.id, "com.target.diff.differ.NestedTest\$SomeComplexClass.list")
          Assert.assertEquals(change.removed.size, 0)
          val added = change.added[0]
          Assert.assertEquals(added.added, current.list[2])
          Assert.assertEquals(added.id, "com.target.diff.differ.NestedTest\$SomeComplexClass.list{different}")
        }
        is Change.Removed -> {
          Assert.assertEquals(change.removed, old.middleClass.innerClass)
          Assert.assertEquals(null, current.middleClass.innerClass)
          Assert.assertEquals(change.id, "com.target.diff.differ.NestedTest\$SomeComplexClass.middleClass.innerClass")
        }
        else -> throw AssertionError("Unexpected change: $change")
      }
    }
  }

  @Test
  fun `multiple changes are ordered naturally`() {
    val current = SomeComplexClass(
      string = ":sparkles:",
      list = listOf("completely", "different"),
      middleClass = MiddleClass(
        boolean = false,
        innerClass = InnerClass(
          value = 101
        )
      )
    )
    val changes = Differ().diff(old, current)
    Assert.assertEquals(5, changes.size)
    when (val change = changes[0]) {
      is Change.Value -> {
        Assert.assertEquals(change.old, ":wave:")
        Assert.assertEquals(change.current, ":sparkles:")
        Assert.assertEquals(change.id, "com.target.diff.differ.NestedTest\$SomeComplexClass.string")
      }
      else -> {
        Assert.fail("Expecting a different type: $change")
      }
    }
    when (val change = changes[1]) {
      is Change.Value -> {
        Assert.assertEquals(change.old, "something")
        Assert.assertEquals(change.current, "completely")
        Assert.assertEquals(change.id, "com.target.diff.differ.NestedTest\$SomeComplexClass.list[0]")
      }
      else -> {
        Assert.fail("Expecting a different type: $change")
      }
    }
    when (val change = changes[2]) {
      is Change.Value -> {
        Assert.assertEquals(change.old, "here")
        Assert.assertEquals(change.current, "different")
        Assert.assertEquals(change.id, "com.target.diff.differ.NestedTest\$SomeComplexClass.list[1]")
      }
      else -> {
        Assert.fail("Expecting a different type: $change")
      }
    }
    when (val change = changes[3]) {
      is Change.Value -> {
        Assert.assertEquals(change.old, true)
        Assert.assertEquals(change.current, false)
        Assert.assertEquals(change.id, "com.target.diff.differ.NestedTest\$SomeComplexClass.middleClass.boolean")
      }
      else -> {
        Assert.fail("Expecting a different type: $change")
      }
    }
    when (val change = changes[4]) {
      is Change.Value -> {
        Assert.assertEquals(change.old, 100)
        Assert.assertEquals(change.current, 101)
        Assert.assertEquals(
          change.id,
          "com.target.diff.differ.NestedTest\$SomeComplexClass.middleClass.innerClass.value"
        )
      }
      else -> {
        Assert.fail("Expecting a different type: $change")
      }
    }
  }
}
