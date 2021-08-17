package com.target.diffjunit4

/**
 * Use this the way you would use org.junit.Assert.*
 */
object IntelliDiffAssert {

  private val assertions by lazy { IntelliDiffAssertions() }

  /**
   * Fails the current test with the specified message.
   *
   * @param message the message to report.
   */
  @JvmStatic
  internal fun fail(message: String?) {
    assertions.fail(message)
  }

  @JvmStatic
  fun assertTrue(actual: Boolean) {
    assertions.assertTrue(actual)
  }

  /**
   * Asserts that the specified value is `true`.
   *
   * @param message the message to report if the assertion fails.
   */
  @JvmStatic
  fun assertTrue(message: String? = null, actual: Boolean) {
    assertions.assertTrue(message, actual)
  }

  @JvmStatic
  fun assertFalse(actual: Boolean) {
    assertions.assertFalse(actual)
  }

  /**
   * Asserts that the specified value is `false`.
   *
   * @param message the message to report if the assertion fails.
   */
  @JvmStatic
  fun assertFalse(message: String? = null, actual: Boolean) {
    assertions.assertFalse(message, actual)
  }

  /**
   * Asserts that the specified values are equal.
   */
  @JvmStatic
  fun <T> assertEquals(expected: T, actual: T) {
    assertions.assertEquals(expected, actual)
  }

  /**
   * Asserts that the specified values are equal.
   *
   * @param message the message to report if the assertion fails.
   */
  @JvmStatic
  fun <T> assertEquals(message: String? = null, expected: T, actual: T) {
    assertions.assertEquals(message, expected, actual)
  }

  /**
   * Asserts that the specified values are not equal.
   */
  @JvmStatic
  fun <T> assertNotEquals(illegal: T, actual: T) {
    assertions.assertNotEquals(illegal, actual)
  }

  /**
   * Asserts that the specified values are not equal.
   *
   * @param message the message to report if the assertion fails.
   */
  @JvmStatic
  fun <T> assertNotEquals(message: String? = null, illegal: T, actual: T) {
    assertions.assertNotEquals(message, illegal, actual)
  }

  /**
   * Asserts that the specified values are the same instance.
   */
  @JvmStatic
  fun <T> assertSame(expected: T, actual: T) {
    assertions.assertSame(expected, actual)
  }

  /**
   * Asserts that the specified values are the same instance.
   *
   * @param message the message to report if the assertion fails.
   */
  @JvmStatic
  fun <T> assertSame(message: String? = null, expected: T, actual: T) {
    assertions.assertSame(message, expected, actual)
  }

  /**
   * Asserts that the specified values are not the same instance.
   */
  @JvmStatic
  fun <T> assertNotSame(illegal: T, actual: T) {
    assertions.assertNotSame(illegal, actual)
  }

  /**
   * Asserts that the specified values are not the same instance.
   *
   * @param message the message to report if the assertion fails.
   */
  @JvmStatic
  fun <T> assertNotSame(message: String? = null, illegal: T, actual: T) {
    assertions.assertNotSame(message, illegal, actual)
  }

  /**
   * Asserts that the specified value is `null`.
   */
  @JvmStatic
  fun assertNull(actual: Any?) {
    assertions.assertNull(actual)
  }

  /**
   * Asserts that the specified value is `null`.
   *
   * @param message the message to report if the assertion fails.
   */
  @JvmStatic
  fun assertNull(message: String? = null, actual: Any?) {
    assertions.assertNull(message, actual)
  }

  /**
   * Asserts that the specified value is not `null`.
   */
  @JvmStatic
  fun assertNotNull(actual: Any?) {
    assertions.assertNotNull(actual)
  }

  /**
   * Asserts that the specified value is not `null`.
   *
   * @param message the message to report if the assertion fails.
   */
  @JvmStatic
  fun assertNotNull(message: String? = null, actual: Any?) {
    assertions.assertNotNull(message, actual)
  }
}
