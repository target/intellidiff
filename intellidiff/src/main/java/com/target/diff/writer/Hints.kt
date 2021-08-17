package com.target.diff.writer

import com.target.diff.differ.Change

internal data class Hint(val expected: String, val actual: String)

internal class Hints(val writer: HintWriter = HintWriter()) {

  fun generate(change: Change): Hint {
    return when (change) {
      is Change.DifferentContainerSize ->
        writer.write(
          expected = "size: ${change.oldSize}",
          actual = "size: ${change.currentSize}"
        )
      is Change.DifferentType -> {
        writer.write(
          expected = "type: ${change.old::class.qualifiedName}",
          actual = "type: ${change.current::class.qualifiedName}"
        )
      }
      is Change.Value -> {
        writer.write(
          expected = classifyContent(change.old),
          actual = classifyContent(change.current)
        )
      }
      is Change.Removed -> {
        writer.write(
          expected = classifyContent(change.removed),
          actual = classifyContent(null)
        )
      }
      is Change.Added -> {
        writer.write(
          expected = classifyContent(null),
          actual = classifyContent(change.added)
        )
      }
    }
  }

  fun classifyContent(content: Any?): String {
    val classification = when (content) {
      null -> "null"
      is String -> classifyString(content)
      else -> ""
    }

    return if (classification.isNotEmpty()) {
      "content: $classification"
    } else {
      classification
    }
  }

  private fun classifyString(content: String): String {
    return when {
      content.isEmpty() -> "empty"
      content.isBlank() -> "blank"
      content.trim().length != content.length -> "trim-alert"
      else -> ""
    }
  }
}

private const val DEFAULT_OPEN_SYMBOL = "-->("
private const val DEFAULT_CLOSE_SYMBOL = ")"
private const val DEFAULT_ALIGNMENT_SYMBOL = " "

internal class HintWriter(
  val openSymbol: String = DEFAULT_OPEN_SYMBOL,
  val closeSymbol: String = DEFAULT_CLOSE_SYMBOL,
  val alignmentSymbol: String = DEFAULT_ALIGNMENT_SYMBOL
) {

  init {
    check(alignmentSymbol.length == 1) {
      """
        alignmentSymbol is used t make up the difference in lengths between expected
        and actual values. This is only exposed as a String as to not make
        declaration feel weird moving between multiple quote types
      """.trimIndent()
    }
  }

  fun empty(): Hint {
    return Hint(expected = "", actual = "")
  }

  fun write(expected: String, actual: String): Hint {
    val expectedHint = format(content = expected)
    val actualHint = format(content = actual)
    val longestHint = Math.max(expectedHint.length, actualHint.length)
    return Hint(
      expected = align(
        content = expectedHint,
        size = longestHint
      ),
      actual = align(
        content = actualHint,
        size = longestHint
      )
    )
  }

  fun format(content: String): String {
    return if (content.isNotEmpty()) {
      return "$openSymbol$content$closeSymbol"
    } else {
      content
    }
  }

  fun align(content: String, size: Int): String {
    return if (content.length >= size) {
      content
    } else {
      content.plus(alignmentSymbol.repeat(size - content.length))
    }
  }
}
