package com.target.diffjunit5

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * This test should be re-enabled when IntelliDiffConfiguration is resolvable from a ServiceLoader.
 * The instance created within our test extension isn't currently configurable
 */
@Disabled
@ExtendWith(IntelliDiffExtension::class)
class JUnit5Test {

    @Test
    fun `remove ignore to see example`() {
        val old = BestMemeAward(name = "RollSafe", timeless = false)
        val current = BestMemeAward(name = "Leeroy Jenkins", timeless = true)

        Assertions.assertEquals(old, current)
    }
}