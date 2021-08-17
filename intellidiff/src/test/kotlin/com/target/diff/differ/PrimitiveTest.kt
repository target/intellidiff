package com.target.diff.differ

import org.junit.Assert
import org.junit.Test

class PrimitiveTest {
  @Test
  fun `unequal ints should not be equal`() {
    val change = Differ().diff(1, 2)[0]
    when (change) {
      is Change.Value -> {
        Assert.assertEquals(change.old, 1)
        Assert.assertEquals(change.current, 2)
        Assert.assertEquals(change.id, "java.lang.Integer")
      }
      else -> {
        Assert.fail("Expecting a value change: $change")
      }
    }
  }

  @Test
  fun `equal ints should be equal`() {
    Assert.assertEquals(0, Differ().diff(2, 2).size)
  }

  @Test
  fun `unequal floats should not be equal`() {
    val change = Differ().diff(1.2f, 2.7f)[0]
    when (change) {
      is Change.Value -> {
        Assert.assertEquals(change.old, 1.2f)
        Assert.assertEquals(change.current, 2.7f)
        Assert.assertEquals(change.id, "java.lang.Float")
      }
      else -> {
        Assert.fail("Expecting a value change: $change")
      }
    }
  }

  @Test
  fun `equal floats should be equal`() {
    Assert.assertEquals(0, Differ().diff(-1.2f, -1.2f).size)
  }

  @Test
  fun `unequal strings should not be equal`() {
    val change = Differ().diff("Taco", "Johns")[0]
    when (change) {
      is Change.Value -> {
        Assert.assertEquals(change.old, "Taco")
        Assert.assertEquals(change.current, "Johns")
        Assert.assertEquals(change.id, "java.lang.String")
      }
      else -> {
        Assert.fail("Expecting a value change: $change")
      }
    }
  }

  @Test
  fun `equal strings should be equal`() {
    Assert.assertEquals(0, Differ().diff("Taco", "Taco").size)
  }

  @Test
  fun `unequal longs should not be equal`() {
    val change = Differ().diff(1L, 2L)[0]
    when (change) {
      is Change.Value -> {
        Assert.assertEquals(change.old, 1L)
        Assert.assertEquals(change.current, 2L)
        Assert.assertEquals(change.id, "java.lang.Long")
      }
      else -> {
        Assert.fail("Expecting a value change: $change")
      }
    }
  }

  @Test
  fun `equal longs should be equal`() {
    Assert.assertEquals(0, Differ().diff(2L, 2L).size)
  }

  @Test
  fun `unequal chars should not be equal`() {
    val change = Differ().diff('c', 'x')[0]
    when (change) {
      is Change.Value -> {
        Assert.assertEquals(change.old, 'c')
        Assert.assertEquals(change.current, 'x')
        Assert.assertEquals(change.id, "java.lang.Character")
      }
      else -> {
        Assert.fail("Expecting a value change: $change")
      }
    }
  }

  @Test
  fun `equal chars should be equal`() {
    Assert.assertEquals(0, Differ().diff('x', 'x').size)
  }

  @Test
  fun `unequal bytes should not be equal`() {
    val change = Differ().diff(Byte.MAX_VALUE, Byte.MIN_VALUE)[0]
    when (change) {
      is Change.Value -> {
        Assert.assertEquals(change.old, Byte.MAX_VALUE)
        Assert.assertEquals(change.current, Byte.MIN_VALUE)
        Assert.assertEquals(change.id, "java.lang.Byte")
      }
      else -> {
        Assert.fail("Expecting a value change: $change")
      }
    }
  }

  @Test
  fun `equal bytes should be equal`() {
    Assert.assertEquals(0, Differ().diff(0x23, 0x23).size)
  }

  @Test
  fun `unequal doubles should not be equal`() {
    val change = Differ().diff(Double.MAX_VALUE, Double.MIN_VALUE)[0]
    when (change) {
      is Change.Value -> {
        Assert.assertEquals(change.old, Double.MAX_VALUE)
        Assert.assertEquals(change.current, Double.MIN_VALUE)
        Assert.assertEquals(change.id, "java.lang.Double")
      }
      else -> {
        Assert.fail("Expecting a value change: $change")
      }
    }
  }

  @Test
  fun `equal doubles should be equal`() {
    Assert.assertEquals(0, Differ().diff(Double.MAX_VALUE, Double.MAX_VALUE).size)
  }

  @Test
  fun `unequal shorts should not be equal`() {
    val change = Differ().diff(Short.MAX_VALUE, Short.MIN_VALUE)[0]
    when (change) {
      is Change.Value -> {
        Assert.assertEquals(change.old, Short.MAX_VALUE)
        Assert.assertEquals(change.current, Short.MIN_VALUE)
        Assert.assertEquals(change.id, "java.lang.Short")
      }
      else -> {
        Assert.fail("Expecting a value change: $change")
      }
    }
  }

  @Test
  fun `equal shorts should be equal`() {
    Assert.assertEquals(0, Differ().diff(Short.MAX_VALUE, Short.MAX_VALUE).size)
  }

  @Test
  fun `unequal booleans should not be equal`() {
    val change = Differ().diff(true, false)[0]
    when (change) {
      is Change.Value -> {
        Assert.assertEquals(change.old, true)
        Assert.assertEquals(change.current, false)
        Assert.assertEquals(change.id, "java.lang.Boolean")
      }
      else -> {
        Assert.fail("Expecting a value change: $change")
      }
    }
  }

  @Test
  fun `equal booleans should be equal`() {
    Assert.assertEquals(0, Differ().diff(true, true).size)
  }

  @Test
  fun `unequal primitive in a container should not be equal`() {
    val old = Container("gouda")
    val current = Container("goo")
    val change = Differ().diff(old, current)[0]
    when (change) {
      is Change.Value -> {
        Assert.assertEquals(change.old, old.obj)
        Assert.assertEquals(change.current, current.obj)
        Assert.assertEquals(change.id, "com.target.diff.differ.Container.obj")
      }
      else -> {
        Assert.fail("Expecting a value change: $change")
      }
    }
  }

  @Test
  fun `equal arrays should be equal`() {
    val old = arrayListOf("see")
    val current = arrayListOf("see")
    Assert.assertEquals(0, Differ().diff(old, current).size)
  }

  @Test
  fun `comparing an array to a list should be a different type`() {
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
        Assert.fail("Expecting a different type: $change")
      }
    }
  }

  @Test
  fun `content equal CharSequences of the same type are equal`() {
    val old = Container(MyCharSequence(":sparkles:"))
    val current = Container(MyCharSequence(":sparkles:"))
    val changes = Differ().diff(old, current)
    Assert.assertEquals(0, changes.size)
  }

  @Test
  fun `content equal CharSequences of different types are not equal`() {
    val old = Container(":sparkles:")
    val current = Container(MyCharSequence(":sparkles:"))
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
}

data class MyCharSequence(private val data: String) : CharSequence {
  override val length: Int get() = data.length
  override fun get(index: Int): Char = data[index]
  override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = data.subSequence(startIndex, endIndex)
}
