package com.vertyll.projectabackend.email.enums

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

class EmailTemplateNameTest {
    @Test
    fun `enum should have correct number of values`() {
        // given/when
        val values = EmailTemplateName.entries

        // then
        assertEquals(4, values.size)
    }

    @Test
    fun `enum values should have correct template names`() {
        // given/when/then
        assertEquals("activate_account", EmailTemplateName.ACTIVATE_ACCOUNT.templateName)
        assertEquals("change_email", EmailTemplateName.CHANGE_EMAIL.templateName)
        assertEquals("change_password", EmailTemplateName.CHANGE_PASSWORD.templateName)
        assertEquals("reset_password", EmailTemplateName.RESET_PASSWORD.templateName)
    }

    @Test
    fun `valueOf should return correct enum value`() {
        // given/when/then
        assertSame(EmailTemplateName.ACTIVATE_ACCOUNT, EmailTemplateName.valueOf("ACTIVATE_ACCOUNT"))
        assertSame(EmailTemplateName.CHANGE_EMAIL, EmailTemplateName.valueOf("CHANGE_EMAIL"))
        assertSame(EmailTemplateName.CHANGE_PASSWORD, EmailTemplateName.valueOf("CHANGE_PASSWORD"))
        assertSame(EmailTemplateName.RESET_PASSWORD, EmailTemplateName.valueOf("RESET_PASSWORD"))
    }
}
