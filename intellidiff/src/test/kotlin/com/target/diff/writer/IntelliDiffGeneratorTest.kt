package com.target.diff.writer

import com.target.diff.configuration.configuration
import com.target.diff.differ.Change
import org.junit.Assert
import org.junit.Test

class IntelliDiffGeneratorTest {

  @Test
  fun `different container size change adds size hints`() {
    val old = listOf("one", "two", "three")
    val current = listOf("one")

    Assert.assertEquals(
      composeDiff {
        writeHeader()
        writeFieldDiff(
          changeId = "java.util.Arrays\$ArrayList",
          hint = Hint(
            expected = "-->(size: 3)",
            actual = "-->(size: 1)"
          ),
          old = old,
          current = current
        )
        writeJUnitBlock(expected = old, actual = current)
      },
      IntelliDiff(configuration { }).calculateDiff(expected = old, actual = current).asFailureMessage()
    )
  }

  @Test
  fun `different type change adds class name hints`() {
    val string = "c"
    val char = 'c'

    Assert.assertEquals(
      composeDiff {
        writeHeader()
        writeFieldDiff(
          changeId = "${Change.DifferentType::class.simpleName}",
          hint = Hint(
            expected = "-->(type: kotlin.String)",
            actual = "-->(type: kotlin.Char)  "
          ),
          old = string,
          current = char
        )
        writeJUnitBlock(expected = string, actual = char)
      },
      IntelliDiff(configuration { }).calculateDiff(
        expected = string,
        actual = char
      ).asFailureMessage()
    )
  }

  @Test
  fun `string content hint present when expected and actual contain strings`() {
    val old = "Plumbis sale in Aisle 42  "
    val current = ""

    Assert.assertEquals(
      composeDiff {
        writeHeader()
        writeFieldDiff(
          changeId = "java.lang.String",
          hint = Hint(
            expected = "-->(content: trim-alert)",
            actual = "-->(content: empty)     "
          ),
          old = old,
          current = current
        )
        writeJUnitBlock(expected = old, actual = current)
      },
      IntelliDiff(configuration { }).calculateDiff(expected = old, actual = current).asFailureMessage()
    )
  }

  @Test
  fun `string content hint present when expected contains a string and actual is null`() {
    val old = ""
    val current = null

    Assert.assertEquals(
      composeDiff {
        writeHeader()
        writeFieldDiff(
          changeId = "java.lang.String",
          hint = Hint(
            expected = "-->(content: empty)",
            actual = "-->(content: null) "
          ),
          old = old,
          current = current
        )
        writeJUnitBlock(expected = old, actual = current)
      },
      IntelliDiff(configuration { }).calculateDiff(expected = old, actual = current).asFailureMessage()
    )
  }

  @Test
  fun `string content hint present when expected is null and actual contains a string`() {
    val old = null
    val current = "    "

    Assert.assertEquals(
      composeDiff {
        writeHeader()
        writeFieldDiff(
          changeId = "java.lang.String",
          hint = Hint(
            expected = "-->(content: null) ",
            actual = "-->(content: blank)"
          ),
          old = old,
          current = current
        )
        writeJUnitBlock(expected = old, actual = current)
      },
      IntelliDiff(configuration { }).calculateDiff(expected = old, actual = current).asFailureMessage()
    )
  }
}
