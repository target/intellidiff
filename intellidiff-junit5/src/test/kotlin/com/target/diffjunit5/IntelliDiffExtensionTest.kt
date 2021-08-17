package com.target.diffjunit5

import com.target.diff.writer.IntelliDiff
import com.target.tools.attempt
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError

class IntelliDiffExtensionTest {

  @Test
  fun `exception handler without exception does not throw`() {
    Assertions.assertNull(attempt {
      IntelliDiffExtension().handleTestExecutionException(null, null)
    })
  }

  @Test
  fun `exception handler with non assertion error still throws`() {
    val error = attempt {
      IntelliDiffExtension().handleTestExecutionException(
        null,
        RuntimeException())
    }

    Assertions.assertNotNull(error)
    Assertions.assertTrue(error is RuntimeException)
  }

  @Test
  fun `assertion missing expected and actual values does not generate diff`() {
    val error = attempt {
      IntelliDiffExtension().handleTestExecutionException(
        null,
        AssertionFailedError("<message>")
      )
    }

    Assertions.assertNotNull(error)
    Assertions.assertEquals("<message>", error?.message)
  }

  @Test
  fun `assertion with message and assertion does not generate diff`() {
    val error = attempt {
      IntelliDiffExtension().handleTestExecutionException(
        null,
        AssertionFailedError(
          "<message>",
          RuntimeException()
        )
      )
    }

    Assertions.assertNotNull(error)
    Assertions.assertEquals("<message>", error?.message)
  }

  @Test
  fun `assertion with expected and equals adds diff to message`() {
    val expected = listOf("Element-One", "Element-Two")
    val actual = listOf("One-Element", "Two-Element")

    val error = attempt {
      IntelliDiffExtension().handleTestExecutionException(
        null,
        AssertionFailedError(
          "<message>",
          expected,
          actual
        )
      )
    }

    // ensure error is present
    Assertions.assertNotNull(error)

    // ensure error is correct type upon rewrite
    Assertions.assertTrue(error is AssertionFailedError)

    // ensure IntelliDiff write is written to error message
    Assertions.assertEquals(
      IntelliDiff().calculateDiff(
        message = "<message>",
        expected = expected,
        actual = actual
      ).asFailureMessage(),
      error?.message
    )
  }

}