package com.vertyll.projectabackend.auth.enums

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class VerificationTokenTypeTest {
    @Test
    fun `enum should have correct number of values`() {
        // given/when
        val values = VerificationTokenType.entries

        // then
        assertEquals(4, values.size)
    }

    @Test
    fun `enum should contain expected values`() {
        // given/when/then
        assertTrue(VerificationTokenType.entries.contains(VerificationTokenType.ACCOUNT_ACTIVATION))
        assertTrue(VerificationTokenType.entries.contains(VerificationTokenType.EMAIL_CHANGE))
        assertTrue(VerificationTokenType.entries.contains(VerificationTokenType.PASSWORD_CHANGE))
        assertTrue(VerificationTokenType.entries.contains(VerificationTokenType.PASSWORD_RESET))
    }

    @Test
    fun `valueOf should return correct enum value`() {
        // given/when/then
        assertSame(VerificationTokenType.ACCOUNT_ACTIVATION, VerificationTokenType.valueOf("ACCOUNT_ACTIVATION"))
        assertSame(VerificationTokenType.EMAIL_CHANGE, VerificationTokenType.valueOf("EMAIL_CHANGE"))
        assertSame(VerificationTokenType.PASSWORD_CHANGE, VerificationTokenType.valueOf("PASSWORD_CHANGE"))
        assertSame(VerificationTokenType.PASSWORD_RESET, VerificationTokenType.valueOf("PASSWORD_RESET"))
    }
}
