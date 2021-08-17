package com.target.diffjunit4

import com.target.diff.configuration.IntelliDiffConfiguration
import com.target.diff.writer.IntelliDiff
import com.target.tools.attempt
import org.junit.Assert
import org.junit.Test
import kotlin.reflect.KClass

data class Person(val name: String, val tired: Boolean)

class Configuration : IntelliDiffConfiguration() {
  override fun valueTypes(): List<KClass<*>> {
    return listOf(Person::class)
  }
}

/**
 * Ensure that our IntelliDiffConfiguration provided in this
 * test can be resolved from the classpath with no other
 * configurations present
 */
class IntelliDiffConfiguredAssertionTest {

  @Test
  fun `configuration on classpath must used in assertion tool`() {
    val assertions = IntelliDiffAssertions(Configuration())

    val old = Person(name = "Morty", tired = false)
    val current = Person(name = "Rick", tired = true)

    val expected = IntelliDiff(configuration = Configuration()).calculateDiff(
      expected = old,
      actual = current
    ).asFailureMessage()

    val actual = attempt {
      assertions.assertEquals(
        expected = old,
        actual = current
      )
    }?.message!!

    Assert.assertEquals(
      "IntelliDiff output should match when provided with same configuration",
      expected,
      actual
    )

    Assert.assertTrue(
      "IntelliDiff output should list qualified name of registered value type",
      actual.contains(Person::class.qualifiedName!!)
    )

    Assert.assertFalse(
      "IntelliDiff output should not contain name of value type members",
      actual.contains(String::class.qualifiedName!!)
    )
    Assert.assertFalse(
      "IntelliDiff output should not contain name of value type members",
      actual.contains(Boolean::class.qualifiedName!!)
    )
  }
}
