package com.vertyll.projectabackend.common.response

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class ApiResponseTest {
    @Test
    fun buildResponse_WithAllParameters_ShouldCreateCorrectResponse() {
        // given
        val data = "test data"
        val message = "test message"
        val status = HttpStatus.OK

        // when
        val response = ApiResponse.buildResponse(data, message, status)

        // then
        assertNotNull(response)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(data, response.body!!.data)
        assertEquals(message, response.body!!.message)
        assertNotNull(response.body!!.timestamp)
        assertInstanceOf(BaseResponse::class.java, response.body)
        assertInstanceOf(IResponse::class.java, response.body)
    }

    @Test
    fun buildResponse_WithNullData_ShouldCreateResponseWithNullData() {
        // when
        val response =
            ApiResponse.buildResponse(
                data = null as String?,
                message = "message",
                status = HttpStatus.OK,
            )

        // then
        assertNotNull(response)
        assertNull(response.body!!.data)
    }

    @Test
    fun buildResponse_ShouldSetCurrentTimestamp() {
        // when
        val response = ApiResponse.buildResponse("data", "message", HttpStatus.OK)
        val timestamp = response.body!!.timestamp

        // then
        assertNotNull(timestamp)
        assertTrue(timestamp.isBefore(LocalDateTime.now().plusSeconds(1)))
        assertTrue(timestamp.isAfter(LocalDateTime.now().minusSeconds(1)))
    }

    @Test
    fun response_ShouldImplementInterfaces() {
        // when
        val response = ApiResponse.buildResponse("data", "message", HttpStatus.OK).body!!

        // then
        assertInstanceOf(IResponse::class.java, response)
        assertInstanceOf(BaseResponse::class.java, response)
    }
}
