package com.vertyll.projectabackend.auth.controller

import com.vertyll.projectabackend.auth.dto.AuthRequestDto
import com.vertyll.projectabackend.auth.dto.AuthResponseDto
import com.vertyll.projectabackend.auth.dto.ChangeEmailRequestDto
import com.vertyll.projectabackend.auth.dto.ChangePasswordRequestDto
import com.vertyll.projectabackend.auth.dto.RegisterRequestDto
import com.vertyll.projectabackend.auth.service.AuthService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthControllerTest {
    @Mock
    private lateinit var authService: AuthService

    @Mock
    private lateinit var request: HttpServletRequest

    @Mock
    private lateinit var response: HttpServletResponse

    @Mock
    private lateinit var securityContext: SecurityContext

    @Mock
    private lateinit var authentication: Authentication

    @InjectMocks
    private lateinit var authController: AuthController

    private val testEmail = "test@example.com"

    @BeforeEach
    fun setUp() {
        SecurityContextHolder.setContext(securityContext)
        `when`(securityContext.authentication).thenReturn(authentication)
        `when`(authentication.name).thenReturn(testEmail)
    }

    @Test
    fun `register should call service and return success response`() {
        // given
        val registerRequest =
            RegisterRequestDto(
                firstName = "Test",
                lastName = "User",
                email = testEmail,
                password = "password123",
            )

        // when
        val response = authController.register(registerRequest)

        // then
        verify(authService).register(registerRequest)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNull(response.body?.data)
        assertEquals("User registered successfully", response.body?.message)
    }

    @Test
    fun `authenticate should call service and return auth response`() {
        // given
        val authRequest =
            AuthRequestDto(
                email = testEmail,
                password = "password123",
            )
        val authResponse =
            AuthResponseDto(
                token = "test-token",
                type = "Bearer",
            )
        `when`(authService.authenticate(authRequest, response)).thenReturn(authResponse)

        // when
        val result = authController.authenticate(authRequest, response)

        // then
        verify(authService).authenticate(authRequest, response)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(authResponse, result.body?.data)
        assertEquals("Authentication successful", result.body?.message)
    }

    @Test
    fun `refreshToken should call service and return auth response`() {
        // given
        val authResponse =
            AuthResponseDto(
                token = "new-test-token",
                type = "Bearer",
            )
        `when`(authService.refreshToken(request, response)).thenReturn(authResponse)

        // when
        val result = authController.refreshToken(request, response)

        // then
        verify(authService).refreshToken(request, response)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(authResponse, result.body?.data)
        assertEquals("Token refreshed successfully", result.body?.message)
    }

    @Test
    fun `logout should call service and return success response`() {
        // when
        val result = authController.logout(request, response)

        // then
        verify(authService).logout(request, response)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertNull(result.body?.data)
        assertEquals("Logged out successfully", result.body?.message)
    }

    @Test
    fun `logoutAll should call service and return success response`() {
        // when
        val result = authController.logoutAll(request, response)

        // then
        verify(authService).logoutAllSessions(request, response)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertNull(result.body?.data)
        assertEquals("Logged out from all sessions successfully", result.body?.message)
    }

    @Test
    fun `getSessions should get current user email and call service`() {
        // given
        val sessions =
            listOf(
                mapOf("id" to 1L, "deviceInfo" to "Device 1", "createdAt" to "2023-01-01"),
            )
        `when`(authService.getUserActiveSessions(testEmail)).thenReturn(sessions)

        // when
        val result = authController.getSessions()

        // then
        verify(authService).getUserActiveSessions(testEmail)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(sessions, result.body?.data)
        assertEquals("Active sessions retrieved successfully", result.body?.message)
    }

    @Test
    fun `verifyAccount should call service and return success response`() {
        // given
        val code = "123456"

        // when
        val result = authController.verifyAccount(code)

        // then
        verify(authService).verifyAccount(code)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertNull(result.body?.data)
        assertEquals("Account verified successfully", result.body?.message)
    }

    @Test
    fun `requestEmailChange should call service and return success response`() {
        // given
        val changeEmailRequest =
            ChangeEmailRequestDto(
                currentPassword = "password123",
                newEmail = "new-email@example.com",
            )

        // when
        val result = authController.requestEmailChange(changeEmailRequest)

        // then
        verify(authService).requestEmailChange(changeEmailRequest)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertNull(result.body?.data)
        assertEquals("Email change verification sent to new email", result.body?.message)
    }

    @Test
    fun `verifyEmailChange should call service and return auth response`() {
        // given
        val code = "123456"
        val authResponse =
            AuthResponseDto(
                token = "new-test-token",
                type = "Bearer",
            )
        `when`(authService.verifyEmailChange(code, response)).thenReturn(authResponse)

        // when
        val result = authController.verifyEmailChange(code, request, response)

        // then
        verify(authService).verifyEmailChange(code, response)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(authResponse, result.body?.data)
        assertEquals("Email changed successfully", result.body?.message)
    }

    @Test
    fun `requestPasswordChange should call service and return success response`() {
        // given
        val changePasswordRequest =
            ChangePasswordRequestDto(
                currentPassword = "password123",
                newPassword = "newpassword123",
            )

        // when
        val result = authController.requestPasswordChange(changePasswordRequest)

        // then
        verify(authService).requestPasswordChange(changePasswordRequest)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertNull(result.body?.data)
        assertEquals("Password change verification sent to email", result.body?.message)
    }

    @Test
    fun `verifyPasswordChange should call service and return success response`() {
        // given
        val code = "123456"

        // when
        val result = authController.verifyPasswordChange(code)

        // then
        verify(authService).verifyPasswordChange(code)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertNull(result.body?.data)
        assertEquals("Password changed successfully", result.body?.message)
    }
}
