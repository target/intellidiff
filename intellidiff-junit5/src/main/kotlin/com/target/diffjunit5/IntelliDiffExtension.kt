package com.target.diffjunit5

import com.target.diff.writer.IntelliDiff
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler
import org.opentest4j.AssertionFailedError

open class IntelliDiffExtension : TestExecutionExceptionHandler {

  private val intelliDiff = IntelliDiff()

  override fun handleTestExecutionException(context: ExtensionContext?, throwable: Throwable?) {
    rewriteAssertionError(throwable)?.run { throw this }
  }

  private fun rewriteAssertionError(throwable: Throwable?): Throwable? {
    return when {
      throwable is AssertionFailedError &&
        throwable.isExpectedDefined &&
        throwable.isActualDefined -> {

        val result = intelliDiff.calculateDiff(
          message = throwable.message,
          expected = throwable.expected.ephemeralValue,
          actual = throwable.actual.ephemeralValue
        )

        return when (result) {
          is IntelliDiff.DiffResult.Fail -> {
            AssertionFailedError(result.asFailureMessage(), throwable)
          }
          is IntelliDiff.DiffResult.Pass -> {
            null
          }
        }
      }
      else -> throwable
    }
  }
}