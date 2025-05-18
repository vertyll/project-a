package com.vertyll.projectabackend.email.service

import com.vertyll.projectabackend.email.enums.EmailTemplateName
import jakarta.mail.MessagingException
import jakarta.mail.internet.MimeMessage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine
import java.nio.charset.StandardCharsets

/**
 * SMTP implementation of the IEmailService interface.
 * This implementation uses JavaMailSender to send emails via SMTP
 * and Thymeleaf for template processing.
 */
@Service
@Profile("!test")
class SmtpEmailService(
    private val mailSender: JavaMailSender,
    private val templateEngine: SpringTemplateEngine,
) : IEmailService {
    @Value("\${spring.mail.from}")
    private lateinit var fromEmail: String

    private val logger = LoggerFactory.getLogger(SmtpEmailService::class.java)

    @Async
    @Throws(MessagingException::class)
    override fun sendEmail(
        to: String,
        username: String,
        emailTemplate: EmailTemplateName?,
        activationCode: String,
        subject: String,
    ) {
        val templateName = emailTemplate?.name ?: "confirm-email"

        val mimeMessage: MimeMessage = mailSender.createMimeMessage()
        val helper =
            MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name(),
            )

        val properties =
            mapOf(
                "username" to username,
                "activation_code" to activationCode,
            )

        val context =
            Context().apply {
                setVariables(properties)
            }

        helper.setFrom(fromEmail)
        helper.setTo(to)
        helper.setSubject(subject)

        val template = templateEngine.process(templateName, context)
        helper.setText(template, true)

        mailSender.send(mimeMessage)
    }
}
