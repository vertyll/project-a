package com.vertyll.projectabackend.common.exception

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class ApiExceptionTest {
    @Test
    fun constructor_ShouldSetMessageAndStatus() {
        // given
        val message = "Test error message"
        val status = HttpStatus.BAD_REQUEST

        // when
        val exception = ApiException(message, status)

        // then
        assertEquals(message, exception.message)
        assertEquals(status, exception.status)
    }

    @Test
    fun getStatus_ShouldReturnCorrectStatus() {
        // when
        val exception = ApiException("message", HttpStatus.NOT_FOUND)

        // then
        assertEquals(HttpStatus.NOT_FOUND, exception.status)
    }

    @Test
    fun getMessage_ShouldReturnCorrectMessage() {
        // when
        val exception = ApiException("test message", HttpStatus.BAD_REQUEST)

        // then
        assertEquals("test message", exception.message)
    }
}
