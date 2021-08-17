package com.target.diff.writer

import com.target.diff.differ.Differ
import org.junit.Assert
import org.junit.Test

class HintsTest {

  @Test
  fun `collection size hint is generated when container sizes differ`() {
    val writer = HintWriter()
    val hints = Hints(writer = writer)
    val change = Differ().diff(old = listOf("One", "Two"), current = listOf("One"))[0]
    Assert.assertEquals(
      writer.write(
        expected = "size: 2",
        actual = "size: 1"
      ),
      hints.generate(change)
    )
  }

  @Test
  fun `null classification test`() {
    val hints = Hints()
    Assert.assertEquals("content: null", hints.classifyContent(null))
  }

  @Test
  fun `type classification test`() {
    val hints = Hints()
    Assert.assertEquals("content: empty", hints.classifyContent(""))
    Assert.assertEquals("content: blank", hints.classifyContent("       "))
    Assert.assertEquals("content: trim-alert", hints.classifyContent("     something  "))
    Assert.assertEquals("", hints.classifyContent("something"))
  }
}
