package com.target.diff.differ

import org.junit.Assert
import org.junit.Test

class CycleTest {

  class Top(val middle: Middle)
  class Middle(val bottom: Bottom)
  class Bottom(var top: Top?, val value: Int)

  @Test
  fun `stop recursion when a cycle is detected`() {
    val five = Bottom(top = null, value = 5)
    val three = Bottom(top = null, value = 3)

    val middleForFive = Middle(bottom = five)
    val middleForThree = Middle(bottom = three)

    val topForFive = Top(middle = middleForFive)
    val topForThree = Top(middle = middleForThree)

    five.top = topForFive
    three.top = topForThree

    val change = Differ().diff(topForThree, topForFive)[0]
    when (change) {
      is Change.Value -> {
        Assert.assertEquals(change.old, 3)
        Assert.assertEquals(change.current, 5)
        Assert.assertEquals(change.id, "com.target.diff.differ.CycleTest\$Top.middle.bottom.value")
      }
      else -> {
        Assert.fail("Expecting a value change: $change")
      }
    }
  }
}
