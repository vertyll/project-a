package com.vertyll.projectabackend.common.exception

import org.springframework.http.HttpStatus

class ApiException(
    message: String,
    val status: HttpStatus,
) : RuntimeException(message)
