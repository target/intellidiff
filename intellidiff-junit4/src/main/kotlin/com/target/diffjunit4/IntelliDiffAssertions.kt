package com.target.diffjunit4

import com.target.diff.configuration.IntelliDiffConfiguration
import com.target.diff.writer.IntelliDiff
import org.junit.Assert

/**
 * Use this is you need to provide a custom configuration
 * to the differ
 */
class IntelliDiffAssertions(
  configuration: IntelliDiffConfiguration = IntelliDiffConfiguration.default()
) {

  private val intelliDiff = IntelliDiff(configuration = configuration)

  /**
   * Fails the current test with the specified message.
   *
   * @param message the message to report.
   */
  internal fun fail(message: String?) {
    if (message == null) {
      throw AssertionError()
    }

    throw AssertionError(message)
  }

  fun assertTrue(actual: Boolean) {
    Assert.assertTrue(actual)
  }

  /**
   * Asserts that the specified value is `true`.
   *
   * @param message the message to report if the assertion fails.
   */
  fun assertTrue(message: String? = null, actual: Boolean) {
    Assert.assertTrue(message, actual)
  }

  fun assertFalse(actual: Boolean) {
    Assert.assertFalse(actual)
  }

  /**
   * Asserts that the specified value is `false`.
   *
   * @param message the message to report if the assertion fails.
   */
  fun assertFalse(message: String? = null, actual: Boolean) {
    Assert.assertFalse(message, actual)
  }

  /**
   * Asserts that the specified values are equal.
   */
  fun <T> assertEquals(expected: T, actual: T) {
    assertEquals(
      message = null,
      expected = expected,
      actual = actual
    )
  }

  /**
   * Asserts that the specified values are equal.
   *
   * @param message the message to report if the assertion fails.
   */
  fun <T> assertEquals(message: String? = null, expected: T, actual: T) {
    if (expected != actual) {
      // Perform structural equality check before delegating to IntelliDiff to inspect for differences.
      when (val result = intelliDiff.calculateDiff(message = message, expected = expected, actual = actual)) {
        is IntelliDiff.DiffResult.Fail -> {
          fail(result.message)
        }
        is IntelliDiff.DiffResult.Pass -> {
          // There's potential for a test that would normally fail will now erroneously pass.
          // We're relying heavily on our test suite to cover all of the practical use cases.
        }
      }
    }
  }

  /**
   * Asserts that the specified values are not equal.
   */
  fun <T> assertNotEquals(illegal: T, actual: T) {
    Assert.assertNotEquals(illegal, actual)
  }

  /**
   * Asserts that the specified values are not equal.
   *
   * @param message the message to report if the assertion fails.
   */

  fun <T> assertNotEquals(message: String? = null, illegal: T, actual: T) {
    Assert.assertNotEquals(message, illegal, actual)
  }

  /**
   * Asserts that the specified values are the same instance.
   */

  fun <T> assertSame(expected: T, actual: T) {
    Assert.assertSame(expected, actual)
  }

  /**
   * Asserts that the specified values are the same instance.
   *
   * @param message the message to report if the assertion fails.
   */

  fun <T> assertSame(message: String? = null, expected: T, actual: T) {
    Assert.assertSame(message, expected, actual)
  }

  /**
   * Asserts that the specified values are not the same instance.
   */
  fun <T> assertNotSame(illegal: T, actual: T) {
    Assert.assertNotSame(illegal, actual)
  }

  /**
   * Asserts that the specified values are not the same instance.
   *
   * @param message the message to report if the assertion fails.
   */

  fun <T> assertNotSame(message: String? = null, illegal: T, actual: T) {
    Assert.assertNotSame(message, illegal, actual)
  }

  /**
   * Asserts that the specified value is `null`.
   */

  fun assertNull(actual: Any?) {
    Assert.assertNull(actual)
  }

  /**
   * Asserts that the specified value is `null`.
   *
   * @param message the message to report if the assertion fails.
   */

  fun assertNull(message: String? = null, actual: Any?) {
    Assert.assertNull(message, actual)
  }

  /**
   * Asserts that the specified value is not `null`.
   */

  fun assertNotNull(actual: Any?) {
    Assert.assertNotNull(actual)
  }

  /**
   * Asserts that the specified value is not `null`.
   *
   * @param message the message to report if the assertion fails.
   */

  fun assertNotNull(message: String? = null, actual: Any?) {
    Assert.assertNotNull(message, actual)
  }
}
