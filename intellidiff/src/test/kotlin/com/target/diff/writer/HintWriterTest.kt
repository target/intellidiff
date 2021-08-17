package com.target.diff.writer

import com.target.tools.attempt
import org.junit.Assert
import org.junit.Test

class HintWriterTest {

  private val writer = HintWriter(
    openSymbol = "-->(",
    closeSymbol = ")",
    alignmentSymbol = " "
  )

  @Test
  fun `empty hint contains only empty values`() {
    val empty = writer.empty()
    Assert.assertEquals(
      "",
      empty.expected
    )
    Assert.assertEquals(
      "",
      empty.actual
    )
  }

  @Test
  fun `formatting for empty content should be ignored`() {
    Assert.assertEquals(
      "",
      writer.format(content = "")
    )
  }

  @Test
  fun `formatting for non empty content should include content with enclosing symbols`() {
    Assert.assertEquals(
      "${writer.openSymbol}size: 12${writer.closeSymbol}",
      writer.format(content = "size: 12")
    )
  }

  @Test
  fun `align skips width adjustments when its size is greater than provided size`() {
    Assert.assertEquals(
      "something",
      writer.align(content = "something", size = 0)
    )
  }

  @Test
  fun `align skips width adjustments when its size is equal to provided size`() {
    val input = "something"
    Assert.assertEquals(
      input,
      writer.align(
        content = input,
        size = input.length
      )
    )
  }

  @Test
  fun `empty inputs to write should match empty implementation`() {
    Assert.assertEquals(
      writer.empty(),
      writer.write(
        expected = "",
        actual = ""
      )
    )
  }

  @Test
  fun `hint given only expected contains formatted expected and width adjusted actual`() {
    val expected = "contents: empty"
    val hint = writer.write(expected = expected, actual = "")

    Assert.assertEquals(
      hint.expected.length,
      hint.actual.length
    )

    Assert.assertEquals(
      "${writer.openSymbol}$expected${writer.closeSymbol}",
      hint.expected
    )

    Assert.assertEquals(
      writer.alignmentSymbol.repeat(hint.expected.length),
      hint.actual
    )
  }

  @Test
  fun `hint given only actual contains formatted actual and with adjusted expected`() {
    val actual = "contents: empty"
    val hint = writer.write(expected = "", actual = actual)

    Assert.assertEquals(
      hint.expected.length,
      hint.actual.length
    )

    Assert.assertEquals(
      "${writer.openSymbol}$actual${writer.closeSymbol}",
      hint.actual
    )

    Assert.assertEquals(
      writer.alignmentSymbol.repeat(hint.actual.length),
      hint.expected
    )
  }

  @Test
  fun `same length expected and actual yield same size hints`() {
    val hint = writer.write(expected = "size: 9", actual = "size: 1")

    Assert.assertEquals(
      hint.expected.length,
      hint.actual.length
    )
  }

  @Test
  fun `when expected hint is shorter than actual it must be width adjusted to the same size`() {
    val hint = writer.write(expected = "size: 8", actual = "size: 10")

    Assert.assertEquals(
      hint.expected.length,
      hint.actual.length
    )
  }

  @Test
  fun `when actual hint is shorter than expected it must be width adjusted to the same size`() {
    val hint = writer.write(expected = "size: 10", actual = "size: 8")

    Assert.assertEquals(
      hint.expected.length,
      hint.actual.length
    )
  }

  @Test
  fun `writer declared with multiple alignment symbols throws`() {
    Assert.assertNotNull(
      attempt {
        HintWriter(
          openSymbol = "(",
          closeSymbol = ")",
          alignmentSymbol = "--"
        )
      }
    )
  }
}
