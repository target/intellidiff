package com.target.diff.differ

import org.junit.Assert
import org.junit.Test

enum class TestEnum {
  A,
  B,
  C
}

enum class OtherEnum {
  A,
  B,
  C
}

class EnumTest {

  @Test
  fun `enums diff like primitive types, not objects`() {
    val old = TestEnum.A
    val current = TestEnum.B
    val changes = Differ().diff(old, current)
    Assert.assertEquals(1, changes.size)
    when (val change = changes.single()) {
      is Change.Value -> {
        Assert.assertEquals(change.old, TestEnum.A)
        Assert.assertEquals(change.current, TestEnum.B)
        Assert.assertEquals(change.id, "com.target.diff.differ.TestEnum")
      }
      else -> {
        Assert.fail("Expecting a value change: $change")
      }
    }
  }

  @Test
  fun `enums of different types are different`() {
    val old = TestEnum.A
    val current = OtherEnum.A
    val change = Differ().diff(old, current).single()
    when (change) {
      is Change.DifferentType -> {
        Assert.assertEquals(change.old, TestEnum.A)
        Assert.assertEquals(change.current, OtherEnum.A)
        Assert.assertEquals(change.id, "com.target.diff.differ.TestEnum")
      }
      else -> {
        Assert.fail("Expecting a value change: $change")
      }
    }
  }
}
