package com.target.tools

import org.junit.Assert
import org.junit.Test
import java.lang.RuntimeException

internal class ThrowablesTest {

  @Test
  fun `non throwing function returns null`() {
    Assert.assertNull(attempt { /* no-op */ })
  }

  @Test
  fun `throwing function returns throwable`() {
    val expected = RuntimeException("this is a message")
    Assert.assertSame(expected, attempt { throw expected })
  }

}