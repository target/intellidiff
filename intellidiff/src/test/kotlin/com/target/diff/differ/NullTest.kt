package com.target.diff.differ

import org.junit.Assert
import org.junit.Test

class NullTest {

  @Test
  fun `a null to present property should be added`() {
    val old = Container(null)
    val current = Container("wave")
    val change = Differ().diff(old, current)[0]
    when (change) {
      is Change.Added -> {
        Assert.assertEquals(null, old.obj)
        Assert.assertEquals(change.added, current.obj)
        Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj")
      }
      else -> {
        Assert.fail("Expecting an added change: $change")
      }
    }
  }

  @Test
  fun `a present to null property should be removed`() {
    val old = Container("wave")
    val current = Container(null)
    val change = Differ().diff(old, current)[0]
    when (change) {
      is Change.Removed -> {
        Assert.assertEquals(change.removed, old.obj)
        Assert.assertEquals(null, current.obj)
        Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj")
      }
      else -> {
        Assert.fail("Expecting a removed change: $change")
      }
    }
  }

  @Test
  fun `two null properties should be equal`() {
    val old = Container(null)
    val current = Container(null)
    Assert.assertEquals(0, Differ().diff(old, current).size)
  }

  @Test
  fun `two null should be equal`() {
    Assert.assertEquals(0, Differ().diff(null, null).size)
  }
}
