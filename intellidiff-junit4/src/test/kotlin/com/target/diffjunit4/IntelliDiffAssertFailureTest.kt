package com.target.diffjunit4

import com.target.tools.attempt
import org.junit.Assert
import org.junit.Test

/**
 * The motivation of these test are basically to ensure that [IntelliDiffAssert] functions
 * that are not overwritten continue to work as expected. This  is less about ensuring our
 * functionality and more as a safety net in case the underlying implementation changes
 * during a version upgrade
 */
internal class IntelliDiffAssertFailureTest {

  @Test
  fun `IntelliDiff true`() {
    Assert.assertNotNull(attempt { IntelliDiffAssert.assertTrue(message = null, actual = false) })
    Assert.assertNotNull(attempt { IntelliDiffAssert.assertTrue(message = "", actual = false) })
    Assert.assertNotNull(attempt { IntelliDiffAssert.assertTrue(false) })
  }

  @Test
  fun `IntelliDiff false`() {
    Assert.assertNotNull(attempt { IntelliDiffAssert.assertFalse(message = null, actual = true) })
    Assert.assertNotNull(attempt { IntelliDiffAssert.assertFalse(message = "", actual = true) })
    Assert.assertNotNull(attempt { IntelliDiffAssert.assertFalse(true) })
  }

  @Test
  fun `assert equals`() {
    Assert.assertNotNull(attempt { IntelliDiffAssert.assertEquals(message = null, expected = true, actual = false) })
    Assert.assertNotNull(attempt { IntelliDiffAssert.assertEquals(message = "", expected = true, actual = false) })
    Assert.assertNotNull(attempt { IntelliDiffAssert.assertEquals(expected = true, actual = false) })
  }

  @Test
  fun `assert not equals`() {
    Assert.assertNotNull(attempt { IntelliDiffAssert.assertNotEquals(message = null, illegal = true, actual = true) })
    Assert.assertNotNull(attempt { IntelliDiffAssert.assertNotEquals(message = "", illegal = true, actual = true) })
    Assert.assertNotNull(attempt { IntelliDiffAssert.assertNotEquals(illegal = true, actual = true) })
  }

  @Test
  fun `assert same`() {
    Assert.assertNotNull(attempt { IntelliDiffAssert.assertSame(message = null, expected = true, actual = false) })
    Assert.assertNotNull(attempt { IntelliDiffAssert.assertSame(message = "", expected = true, actual = false) })
    Assert.assertNotNull(attempt { IntelliDiffAssert.assertSame(expected = true, actual = false) })
  }

  @Test
  fun `assert not same`() {
    val same = "field only used for same reference id"
    Assert.assertNotNull(attempt { IntelliDiffAssert.assertNotSame(message = null, illegal = same, actual = same) })
    Assert.assertNotNull(attempt { IntelliDiffAssert.assertNotSame(message = "", illegal = same, actual = same) })
    Assert.assertNotNull(attempt { IntelliDiffAssert.assertNotSame(illegal = same, actual = same) })
  }

  @Test
  fun `assert null`() {
    Assert.assertNotNull(attempt { IntelliDiffAssert.assertNull(message = null, actual = "not null") })
    Assert.assertNotNull(attempt { IntelliDiffAssert.assertNull(message = "", actual = "not null") })
    Assert.assertNotNull(attempt { IntelliDiffAssert.assertNull(actual = "not null") })
  }

  @Test
  fun `assert not null`() {
    Assert.assertNotNull(attempt { IntelliDiffAssert.assertNotNull(message = null, actual = null) })
    Assert.assertNotNull(attempt { IntelliDiffAssert.assertNotNull(message = "", actual = null) })
    Assert.assertNotNull(attempt { IntelliDiffAssert.assertNotNull(actual = null) })
  }
}
