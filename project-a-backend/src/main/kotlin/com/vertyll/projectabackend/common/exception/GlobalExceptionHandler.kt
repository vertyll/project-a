package com.vertyll.projectabackend.common.exception

import com.vertyll.projectabackend.common.response.ApiResponse
import com.vertyll.projectabackend.common.response.ValidationErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ApiException::class)
    fun handleApiException(ex: ApiException): ResponseEntity<ApiResponse<Nothing?>> {
        return ApiResponse.buildResponse(
            null,
            ex.message,
            ex.status,
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ValidationErrorResponse> {
        val errors = HashMap<String, MutableList<String>>()

        ex.bindingResult.fieldErrors.forEach { error ->
            val field = error.field
            val message = error.defaultMessage ?: "Invalid value"

            if (!errors.containsKey(field)) {
                errors[field] = ArrayList()
            }
            errors[field]?.add(message)
        }

        val response =
            ValidationErrorResponse(
                message = "Validation failed",
                errors = errors,
                timestamp = LocalDateTime.now(),
            )

        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(BadCredentialsException::class)
    @Suppress("UNUSED_PARAMETER")
    fun handleBadCredentialsException(ignoredEx: BadCredentialsException): ResponseEntity<ApiResponse<Nothing?>> {
        return ApiResponse.buildResponse(
            null,
            "Invalid email or password",
            HttpStatus.UNAUTHORIZED,
        )
    }

    @ExceptionHandler(DisabledException::class)
    @Suppress("UNUSED_PARAMETER")
    fun handleDisabledException(ignoredEx: DisabledException): ResponseEntity<ApiResponse<Nothing?>> {
        return ApiResponse.buildResponse(
            null,
            "Account is disabled",
            HttpStatus.FORBIDDEN,
        )
    }

    @ExceptionHandler(LockedException::class)
    @Suppress("UNUSED_PARAMETER")
    fun handleLockedException(ignoredEx: LockedException): ResponseEntity<ApiResponse<Nothing?>> {
        return ApiResponse.buildResponse(
            null,
            "Account is locked",
            HttpStatus.FORBIDDEN,
        )
    }

    @ExceptionHandler(Exception::class)
    @Suppress("UNUSED_PARAMETER")
    fun handleException(ignoredEx: Exception): ResponseEntity<ApiResponse<Nothing?>> {
        return ApiResponse.buildResponse(
            null,
            "An unexpected error occurred",
            HttpStatus.INTERNAL_SERVER_ERROR,
        )
    }
}
