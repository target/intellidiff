package com.target.diff.writer

import com.target.diff.configuration.IntelliDiffConfiguration
import com.target.diff.differ.Change
import com.target.diff.differ.Differ
import com.target.diff.exhaustive

class IntelliDiff(configuration: IntelliDiffConfiguration = IntelliDiffConfiguration.default()) {

  private val differ by lazy {
    Differ(configuration)
  }

  @JvmOverloads
  fun calculateDiff(message: String? = null, expected: Any?, actual: Any?): DiffResult {
    val diff = differ.diff(expected, actual)
    if (diff.isEmpty()) {
      return DiffResult.Pass
    }
    return DiffResult.Fail(
      message = composeDiff {
        writeMessage(message)
        writeHeader()
        writeChanges(diff)
        writeJUnitBlock(expected, actual)
      }
    )
  }

  sealed class DiffResult {
    /**
     * Indicates no changes were found between the two objects.
     */
    object Pass : DiffResult()

    /**
     * Changes were detected between expected and actual.
     */
    data class Fail(val message: String) : DiffResult()

    /**
     * Force unwraps failure message.
     */
    fun asFailureMessage(): String {
      return (this as Fail).message
    }
  }
}

internal class IntelliDiffWriter {

  private val builder = StringBuilder()
  private val hints = Hints()

  fun writeMessage(message: String?) {
    if (message != null) {
      builder.append(message)
    }
  }

  fun writeHeader() {
    builder.append("\n\n### IntelliDiff:\n")
  }

  fun writeFieldDiff(changeId: String, hint: Hint, old: Any?, current: Any?) {
    builder
      .append("\n")
      .append("* $changeId\n")
      .append("|${hint.expected}--> expected: $old\n")
      .append("|${hint.actual}-->   actual: $current\n")
  }

  fun writeChange(change: Change) {
    val hint = hints.generate(change)
    when (change) {
      is Change.Value -> writeFieldDiff(changeId = change.id, hint = hint, old = change.old, current = change.current)
      is Change.Removed -> writeFieldDiff(changeId = change.id, hint = hint, old = change.removed, current = null)
      is Change.Added -> writeFieldDiff(changeId = change.id, hint = hint, old = null, current = change.added)
      is Change.DifferentType -> writeFieldDiff(
        changeId = "${change::class.simpleName}",
        hint = hint,
        old = change.old,
        current = change.current
      )
      is Change.DifferentContainerSize -> writeFieldDiff(
        changeId = change.id,
        hint = hint,
        old = change.old,
        current = change.current
      )
    }.exhaustive()
  }

  fun writeChanges(changes: List<Change>) {
    changes.forEach { change -> writeChange(change) }
  }

  fun writeJUnitBlock(expected: Any?, actual: Any?) {
    builder.append("\nexpected:<$expected> but was:<$actual>\n")
  }

  fun print(): String {
    return builder.toString()
  }
}

internal fun composeDiff(init: IntelliDiffWriter.() -> Unit): String {
  val writer = IntelliDiffWriter()
  writer.init()
  return writer.print()
}
