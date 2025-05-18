package com.vertyll.projectabackend.common.exception

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException

class GlobalExceptionHandlerTest {
    private lateinit var handler: GlobalExceptionHandler

    @BeforeEach
    fun setUp() {
        handler = GlobalExceptionHandler()
    }

    @Test
    fun handleApiException_ShouldReturnCorrectResponse() {
        // given
        val ex = ApiException("test message", HttpStatus.BAD_REQUEST)

        // when
        val response = handler.handleApiException(ex)

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("test message", response.body!!.message)
    }

    @Test
    fun handleValidationException_ShouldReturnValidationErrorResponse() {
        // given
        val ex = mock(MethodArgumentNotValidException::class.java)
        val bindingResult = mock(BindingResult::class.java)
        val fieldError = FieldError("object", "username", "Username is required")

        `when`(ex.bindingResult).thenReturn(bindingResult)
        `when`(bindingResult.fieldErrors).thenReturn(listOf(fieldError))

        // when
        val response = handler.handleValidationException(ex)

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("Validation failed", response.body!!.message)
        assertNotNull(response.body!!.timestamp)
        assertNotNull(response.body!!.errors)

        val errors = response.body!!.errors
        assertEquals(1, errors.size)
        assertEquals(listOf("Username is required"), errors["username"])
    }

    @Test
    fun handleValidationException_ShouldHandleMultipleErrors() {
        // given
        val ex = mock(MethodArgumentNotValidException::class.java)
        val bindingResult = mock(BindingResult::class.java)
        val passwordError1 = FieldError("object", "password", "Password must be at least 8 characters")
        val passwordError2 = FieldError("object", "password", "Password must contain an uppercase letter")
        val emailError = FieldError("object", "email", "Invalid email format")

        `when`(ex.bindingResult).thenReturn(bindingResult)
        `when`(bindingResult.fieldErrors).thenReturn(listOf(passwordError1, passwordError2, emailError))

        // when
        val response = handler.handleValidationException(ex)

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        val errors = response.body!!.errors
        assertEquals(2, errors.size)
        assertEquals(2, errors["password"]?.size)
        assertEquals(1, errors["email"]?.size)
        assertTrue(errors["password"]?.contains("Password must be at least 8 characters") == true)
        assertTrue(errors["password"]?.contains("Password must contain an uppercase letter") == true)
        assertEquals(listOf("Invalid email format"), errors["email"])
    }

    @Test
    fun handleBadCredentialsException_ShouldReturnUnauthorized() {
        // given
        val ex = BadCredentialsException("bad credentials")

        // when
        val response = handler.handleBadCredentialsException(ex)

        // then
        assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
        assertEquals("Invalid email or password", response.body!!.message)
    }

    @Test
    fun handleDisabledException_ShouldReturnForbidden() {
        // given
        val ex = DisabledException("disabled")

        // when
        val response = handler.handleDisabledException(ex)

        // then
        assertEquals(HttpStatus.FORBIDDEN, response.statusCode)
        assertEquals("Account is disabled", response.body!!.message)
    }

    @Test
    fun handleLockedException_ShouldReturnForbidden() {
        // given
        val ex = LockedException("locked")

        // when
        val response = handler.handleLockedException(ex)

        // then
        assertEquals(HttpStatus.FORBIDDEN, response.statusCode)
        assertEquals("Account is locked", response.body!!.message)
    }

    @Test
    fun handleException_ShouldReturnInternalServerError() {
        // given
        val ex = RuntimeException("unexpected error")

        // when
        val response = handler.handleException(ex)

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
        assertEquals("An unexpected error occurred", response.body!!.message)
    }
}
