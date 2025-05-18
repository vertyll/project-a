package com.vertyll.projectabackend.common.response

import java.time.LocalDateTime

class ValidationErrorResponse(
    override val message: String,
    val errors: Map<String, List<String>>,
    override val timestamp: LocalDateTime = LocalDateTime.now(),
) : BaseResponse<Nothing?>(
        data = null,
        message = message,
        timestamp = timestamp,
    )
