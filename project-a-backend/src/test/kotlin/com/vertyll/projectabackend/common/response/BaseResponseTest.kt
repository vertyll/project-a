package com.vertyll.projectabackend.common.response

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class BaseResponseTest {
    private class TestBaseResponse<T> private constructor(
        override val data: T?,
        override val message: String,
        override val timestamp: LocalDateTime,
    ) : BaseResponse<T>(data, message, timestamp) {
        companion object {
            fun <T> builder(): TestResponseBuilder<T> = TestResponseBuilder()
        }

        class TestResponseBuilder<T> {
            private var data: T? = null
            private var message: String? = null
            private var timestamp: LocalDateTime = LocalDateTime.now()

            fun data(data: T?): TestResponseBuilder<T> {
                this.data = data
                return this
            }

            fun message(message: String): TestResponseBuilder<T> {
                this.message = message
                return this
            }

            fun timestamp(timestamp: LocalDateTime): TestResponseBuilder<T> {
                this.timestamp = timestamp
                return this
            }

            fun build(): TestBaseResponse<T> =
                TestBaseResponse(
                    data,
                    message ?: "",
                    timestamp,
                )
        }
    }

    @Test
    fun builderShouldSetAllFields() {
        // given
        val testData = "test"
        val testMessage = "message"
        val testTime = LocalDateTime.now()

        // when
        val response =
            TestBaseResponse
                .builder<String>()
                .data(testData)
                .message(testMessage)
                .timestamp(testTime)
                .build()

        // then
        assertEquals(testData, response.data)
        assertEquals(testMessage, response.message)
        assertEquals(testTime, response.timestamp)
    }

    @Test
    fun shouldCreateEmptyResponse() {
        // when
        val now = LocalDateTime.now()
        val response =
            TestBaseResponse
                .builder<String>()
                .data("") // empty string as data
                .message("")
                .timestamp(now)
                .build()

        // then
        assertEquals("", response.data)
        assertEquals("", response.message)
        assertEquals(now, response.timestamp)
    }

    @Test
    fun builderShouldSetDefaultTimestamp() {
        // when
        val response =
            TestBaseResponse
                .builder<String>()
                .data("test")
                .message("message")
                .build()

        // then
        assertNotNull(response.timestamp)
    }

    @Test
    fun shouldCreateResponseWithNullData() {
        // when
        val response =
            TestBaseResponse
                .builder<String>()
                .data(null)
                .message("message")
                .build()

        // then
        assertNull(response.data)
        assertEquals("message", response.message)
        assertNotNull(response.timestamp)
    }
}
