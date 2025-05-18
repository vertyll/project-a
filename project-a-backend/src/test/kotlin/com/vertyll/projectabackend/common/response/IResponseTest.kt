package com.vertyll.projectabackend.common.response

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class IResponseTest {
    private class TestResponse<T>(
        override val data: T,
        override val message: String,
        override val timestamp: LocalDateTime,
    ) : IResponse<T>

    @Test
    fun shouldImplementInterfaceCorrectly() {
        // given
        val testData = "test"
        val testMessage = "message"
        val testTime = LocalDateTime.now()

        // when
        val response: IResponse<String> = TestResponse(testData, testMessage, testTime)

        // then
        assertEquals(testData, response.data)
        assertEquals(testMessage, response.message)
        assertEquals(testTime, response.timestamp)
    }

    @Test
    fun shouldSupportNullableData() {
        // given
        val testMessage = "message"
        val testTime = LocalDateTime.now()

        // when
        val response: IResponse<String?> = TestResponse(null, testMessage, testTime)

        // then
        assertNull(response.data)
        assertEquals(testMessage, response.message)
        assertEquals(testTime, response.timestamp)
    }
}
