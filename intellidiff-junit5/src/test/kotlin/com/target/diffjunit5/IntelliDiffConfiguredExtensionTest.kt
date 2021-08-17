package com.target.diffjunit5

import com.target.diff.configuration.IntelliDiffConfiguration
import com.target.diff.writer.IntelliDiff
import com.target.tools.attempt
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError
import java.io.Serializable
import kotlin.reflect.KClass

class Configuration5 : IntelliDiffConfiguration() {
    override fun valueTypes(): List<KClass<*>> {
        return listOf(BestMemeAward::class)
    }
}

data class BestMemeAward(val name: String, val timeless: Boolean): Serializable

/**
 * This test should be re-enabled when IntelliDiffConfiguration is resolvable from a ServiceLoader.
 * The instance created within our test extension isn't currently configurable
 */
@Disabled
class IntelliDiffConfiguredExtensionTest {

    @Test
    fun `configuration on classpath must used in extension`() {
        val old = BestMemeAward(name = "Grumpy Cat", timeless = false)
        val current = BestMemeAward(name = "Leroy Jenkins", timeless = true)

        val expected = IntelliDiff(configuration = Configuration5()).calculateDiff(
            message = "<message>",
            expected = old,
            actual = current)

        val syntheticFailure = AssertionFailedError(
            "<message>",
            old,
            current
        )

        val actual = attempt {
            IntelliDiffExtension().handleTestExecutionException(null, syntheticFailure)
        }?.message!!

        Assertions.assertEquals(
            expected,
            actual,
            "IntelliDiff output should match when provided with same configuration")

        Assertions.assertTrue(
            actual.contains(BestMemeAward::class.qualifiedName!!),
            "IntelliDiff output should list qualified name of registered value type")

        Assertions.assertFalse(
            actual.contains(String::class.qualifiedName!!),
            "IntelliDiff output should not contain name of value type members")

        Assertions.assertFalse(
            actual.contains(Boolean::class.qualifiedName!!),
            "IntelliDiff output should not contain name of value type members")
    }

}