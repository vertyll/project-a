package com.vertyll.projectabackend.common.response

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ValidationErrorResponseTest {
    @Test
    fun shouldCreateValidationErrorResponseCorrectly() {
        // given
        val testMessage = "Validation failed"
        val testErrors =
            mapOf(
                "username" to listOf("Username cannot be empty"),
                "email" to listOf("Invalid email format"),
            )
        val testTime = LocalDateTime.now()

        // when
        val response =
            ValidationErrorResponse(
                message = testMessage,
                errors = testErrors,
                timestamp = testTime,
            )

        // then
        assertEquals(testMessage, response.message)
        assertEquals(testTime, response.timestamp)
        assertEquals(testErrors, response.errors)
        assertEquals(2, response.errors.size)
        assertEquals(listOf("Username cannot be empty"), response.errors["username"])
        assertEquals(listOf("Invalid email format"), response.errors["email"])
        assertNull(response.data)
    }

    @Test
    fun shouldHandleMultipleErrorsForSameField() {
        // given
        val testMessage = "Validation failed"
        val testErrors =
            mapOf(
                "password" to
                    listOf(
                        "Password must contain at least 8 characters",
                        "Password must contain at least one uppercase letter",
                    ),
            )

        // when
        val response =
            ValidationErrorResponse(
                message = testMessage,
                errors = testErrors,
            )

        // then
        assertEquals(1, response.errors.size)
        assertEquals(2, response.errors["password"]?.size)
        assertTrue(response.errors["password"]?.contains("Password must contain at least 8 characters") ?: false)
        assertTrue(
            response.errors["password"]?.contains(
                "Password must contain at least one uppercase letter",
            ) ?: false,
        )
    }

    @Test
    fun shouldUseDefaultTimestamp() {
        // when
        val response =
            ValidationErrorResponse(
                message = "Error",
                errors = emptyMap(),
            )

        // then
        assertNotNull(response.timestamp)
    }

    @Test
    fun shouldInheritFromBaseResponse() {
        // given
        val response =
            ValidationErrorResponse(
                message = "Error",
                errors = emptyMap(),
            )

        // then
        assertNull(response.data)
    }
}
