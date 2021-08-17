package com.target.diffjunit4

import com.target.diff.writer.IntelliDiff
import com.target.tools.attempt
import org.junit.Assert
import org.junit.Test

/**
 * The motivation of these test are only to verify methods that explicitly override the default implementation
 * provided by the kotlin-test
 */
internal class IntelliDiffAssertTest {

  @Test
  fun `assert equals passing when equal`() {
    val error = attempt {
      IntelliDiffAssert.assertEquals(
        message = "<message>",
        expected = true,
        actual = true
      )
    }

    Assert.assertNull(error)
  }

  @Test
  fun `assert equals fails when not equal`() {
    val message = "<message>"
    val expected = true
    val actual = false
    val error = attempt { IntelliDiffAssert.assertEquals(message, expected, actual) }!!
    Assert.assertNotNull(error)
    Assert.assertEquals(
      AssertionError::class,
      error::class
    )
    Assert.assertEquals(
      IntelliDiff().calculateDiff(message, expected, actual).asFailureMessage(),
      error.message
    )
  }

  @Test
  fun `assert not equals passing when not equal`() {
    val error = attempt {
      IntelliDiffAssert.assertNotEquals(
        message = "<message>",
        illegal = false,
        actual = true
      )
    }

    Assert.assertNull(error)
  }

  @Test
  fun `assert not equals failing when equal`() {
    val message = "<message>"
    val illegal = false
    val actual = false
    val error = attempt { IntelliDiffAssert.assertNotEquals(message, illegal, actual) }!!

    Assert.assertNotNull(error)
    Assert.assertEquals(
      AssertionError::class,
      error::class
    )
  }

  @Test
  fun `fail with message should throw assertion error containing message`() {
    val output = attempt { IntelliDiffAssert.fail("this is a message") }

    Assert.assertEquals(AssertionError::class, output!!::class)
    Assert.assertEquals("this is a message", output.message)
  }

  @Test
  fun `fail without message should throw assertion containing no message`() {
    val output = attempt { IntelliDiffAssert.fail(null) }
    Assert.assertEquals(AssertionError::class, output!!::class)
    Assert.assertNull(output.message)
  }
}
