package com.target.diff.writer

import com.target.diff.differ.Change
import org.junit.Assert
import org.junit.Test

internal class IntelliDiffWriterTest {

  @Test
  fun `write message adds message to builder`() {
    val expected = "<message>"
    val actual = composeDiff {
      writeMessage("<message>")
    }

    Assert.assertEquals(
      expected,
      actual
    )
  }

  @Test
  fun `write null messages adds nothing to builder`() {
    val expected = ""
    val actual = composeDiff {
      writeMessage(null)
    }

    Assert.assertEquals(
      expected,
      actual
    )
  }

  @Test
  fun `write header adds header to builder`() {
    val expected = "\n\n### IntelliDiff:\n"
    val actual = composeDiff {
      writeHeader()
    }

    Assert.assertEquals(
      expected,
      actual
    )
  }

  @Test
  fun `write field adds field to builder`() {
    val expected = StringBuilder()
      .append("\n")
      .append("* changeId\n")
      .append("|--> expected: old\n")
      .append("|-->   actual: current\n")
      .toString()

    val actual = composeDiff {
      writeFieldDiff(changeId = "changeId", hint = HintWriter().empty(), old = "old", current = "current")
    }

    Assert.assertEquals(
      expected,
      actual
    )
  }

  @Test
  fun `write change adds field to builder`() {
    val change = Change.Value(
      id = "java.lang.String",
      old = "<old>",
      current = "<right>"
    )
    val expected = StringBuilder()
      .append("\n")
      .append("* ${change.id}\n")
      .append("|--> expected: ${change.old}\n")
      .append("|-->   actual: ${change.current}\n")
      .toString()
    val actual = composeDiff {
      writeChange(change)
    }

    Assert.assertEquals(
      expected,
      actual
    )
  }

  @Test
  fun `write changes adds fields to builder`() {
    val changes = listOf(
      Change.Added(
        id = "java.lang.Integer",
        added = 1
      )
    )
    val expected = StringBuilder()
      .append("\n")
      .append("* ${changes[0].id}\n")
      .append("|-->(content: null)--> expected: null\n")
      .append("|                  -->   actual: ${changes[0].added}\n")
      .toString()
    val actual = composeDiff {
      writeChanges(changes)
    }

    Assert.assertEquals(
      expected,
      actual
    )
  }

  /**
   * This test exist the keep exception message formatting consistent in a way that IDEs expect
   * such as IntelliJ/AndroidStudio that attempt to parse expected/actual to maintain nice
   * styling as though a consumer were just using JUnit (without this library)
   */
  @Test
  fun `write JUnit block adds to builder`() {
    val expected = "\nexpected:<true> but was:<false>\n"
    val actual = composeDiff {
      writeJUnitBlock(true, false)
    }

    Assert.assertEquals(
      expected,
      actual
    )
  }

  /**
   * Printed values should be horizontally aligned.
   *
   * This could be perceived as though there is additional elements
   * between the colon and the start of visible characters
   *
   * ```
   * * java.lang.String
   * |--> expected: This
   * |--> actual:   That
   * ```
   *
   * This allows content to start at the same location but both
   * have equal spacing between the colon and beginning of content
   *
   * ```
   * * java.lang.String
   * |--> expected: This
   * |-->   actual: That
   * ```
   */
  @Test
  fun `expected and actual values should be left justified`() {
    val expected = StringBuilder()
      .appendLine()
      .appendLine("* <change-id>")
      .appendLine("|--> expected: This")
      .appendLine("|-->   actual: That")
      .toString()

    val actual = composeDiff {
      writeFieldDiff(
        changeId = "<change-id>",
        hint = Hint("", ""),
        old = "This",
        current = "That"
      )
    }

    Assert.assertEquals(
      expected,
      actual
    )
  }
}
