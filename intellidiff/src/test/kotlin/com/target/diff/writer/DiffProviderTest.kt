package com.target.diff.writer

import com.target.diff.configuration.configuration
import com.target.diff.differ.Differ
import org.junit.Assert
import org.junit.Test

internal class DiffProviderTest {

  @Test
  fun `real provider generates diff in expected format`() {
    val message = "Demand better tools!"
    val old = Project(name = "IntelliDiff", ready = false)
    val current = Project(name = "IntelliDiff", ready = true)

    val expected = composeDiff {
      writeMessage(message)
      writeHeader()
      writeChanges(Differ().diff(old, current))
      writeJUnitBlock(old, current)
    }

    val actual = IntelliDiff(configuration { }).calculateDiff(
      message = message,
      expected = old,
      actual = current
    ).asFailureMessage()

    Assert.assertEquals(expected, actual)
  }
}

data class Project(val name: String, val ready: Boolean)
