package com.vertyll.projectabackend.config

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder

class ApplicationAuditAwareTest {
    private val auditAware = ApplicationAuditAware()

    @Test
    fun getCurrentAuditor_WhenAuthenticated_ShouldReturnUsername() {
        // given
        val authentication = mock(Authentication::class.java)
        val securityContext = mock(SecurityContext::class.java)
        SecurityContextHolder.setContext(securityContext)

        `when`(securityContext.authentication).thenReturn(authentication)
        `when`(authentication.isAuthenticated).thenReturn(true)
        `when`(authentication.name).thenReturn("testUser")

        // when
        val result = auditAware.currentAuditor

        // then
        assertTrue(result.isPresent)
        assertEquals("testUser", result.get())
    }

    @Test
    fun getCurrentAuditor_WhenNotAuthenticated_ShouldReturnSystem() {
        // given
        val authentication = mock(Authentication::class.java)
        val securityContext = mock(SecurityContext::class.java)
        SecurityContextHolder.setContext(securityContext)

        `when`(securityContext.authentication).thenReturn(authentication)
        `when`(authentication.isAuthenticated).thenReturn(false)

        // when
        val result = auditAware.currentAuditor

        // then
        assertTrue(result.isPresent)
        assertEquals("SYSTEM", result.get())
    }

    @Test
    fun getCurrentAuditor_WhenNoAuthentication_ShouldReturnSystem() {
        // given
        val securityContext = mock(SecurityContext::class.java)
        SecurityContextHolder.setContext(securityContext)
        `when`(securityContext.authentication).thenReturn(null)

        // when
        val result = auditAware.currentAuditor

        // then
        assertTrue(result.isPresent)
        assertEquals("SYSTEM", result.get())
    }
}
