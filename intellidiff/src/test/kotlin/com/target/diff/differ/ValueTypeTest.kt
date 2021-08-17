package com.target.diff.differ

import com.target.diff.configuration.configuration
import org.junit.Assert
import org.junit.Test
import java.util.Date

class ValueTypeTest {

  @Test
  fun `adding value types allows you to check equality instead of reflectively investigating properties`() {
    val differ = Differ(configuration { addValueType(Date::class) })
    val old = Container(Date(42424242L))
    val current = Container(Date(-420L))
    val change = differ.diff(old, current)[0]
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
}
