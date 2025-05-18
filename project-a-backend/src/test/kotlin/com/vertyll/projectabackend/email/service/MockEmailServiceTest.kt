package com.vertyll.projectabackend.email.service

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.vertyll.projectabackend.email.enums.EmailTemplateName
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class MockEmailServiceTest {
    private lateinit var mockEmailService: MockEmailService
    private lateinit var listAppender: ListAppender<ILoggingEvent>
    private lateinit var logger: Logger

    @BeforeEach
    fun setUp() {
        mockEmailService = MockEmailService()

        // Set up logger to capture log messages
        logger = LoggerFactory.getLogger(MockEmailService::class.java) as Logger
        listAppender = ListAppender()
        listAppender.start()
        logger.addAppender(listAppender)
    }

    @AfterEach
    fun tearDown() {
        logger.detachAppender(listAppender)
    }

    @Test
    fun `sendEmail should log all email details`() {
        // given
        val to = "test@example.com"
        val username = "Test User"
        val emailTemplate = EmailTemplateName.ACTIVATE_ACCOUNT
        val activationCode = "123456"
        val subject = "Test Subject"

        // when
        mockEmailService.sendEmail(to, username, emailTemplate, activationCode, subject)

        // then
        val logsList = listAppender.list

        // Verify all expected log messages are present
        assertTrue(logsList.any { it.message.contains("MOCK EMAIL SERVICE") })
        assertTrue(logsList.any { it.message.contains("To: $to") })
        assertTrue(logsList.any { it.message.contains("Username: $username") })
        assertTrue(logsList.any { it.message.contains("Template: ${emailTemplate.name}") })
        assertTrue(logsList.any { it.message.contains("Activation Code: $activationCode") })
        assertTrue(logsList.any { it.message.contains("Subject: $subject") })
        assertTrue(logsList.any { it.message.contains("Email would be sent in production environment") })
    }

    @Test
    fun `sendEmail should handle null email template`() {
        // given
        val to = "test@example.com"
        val username = "Test User"
        val emailTemplate = null
        val activationCode = "123456"
        val subject = "Test Subject"

        // when
        mockEmailService.sendEmail(to, username, emailTemplate, activationCode, subject)

        // then
        val logsList = listAppender.list

        // Verify template fallback is used
        assertTrue(logsList.any { it.message.contains("Template: confirm-email") })
    }
}
