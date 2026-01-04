package com.vertyll.projectabackend.auth.controller

import com.vertyll.projectabackend.auth.dto.AuthRequestDto
import com.vertyll.projectabackend.auth.dto.AuthResponseDto
import com.vertyll.projectabackend.auth.dto.ChangeEmailRequestDto
import com.vertyll.projectabackend.auth.dto.ChangePasswordRequestDto
import com.vertyll.projectabackend.auth.dto.RegisterRequestDto
import com.vertyll.projectabackend.auth.dto.ResetPasswordRequestDto
import com.vertyll.projectabackend.auth.service.AuthService
import com.vertyll.projectabackend.common.response.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.mail.MessagingException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Auth management APIs")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/register")
    @Operation(summary = "Register new user")
    @Throws(MessagingException::class)
    fun register(
        @RequestBody @Valid request: RegisterRequestDto,
    ): ResponseEntity<ApiResponse<Unit>> {
        authService.register(request)
        return ApiResponse.buildResponse(
            null,
            "User registered successfully",
            HttpStatus.OK,
        )
    }

    @PostMapping("/authenticate")
    @Operation(summary = "Authenticate user and get token")
    fun authenticate(
        @RequestBody @Valid request: AuthRequestDto,
        response: HttpServletResponse,
    ): ResponseEntity<ApiResponse<AuthResponseDto>> {
        val authResponse = authService.authenticate(request, response)
        return ApiResponse.buildResponse(
            authResponse,
            "Authentication successful",
            HttpStatus.OK,
        )
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh access token using refresh token cookie")
    fun refreshToken(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ResponseEntity<ApiResponse<AuthResponseDto>> {
        val authResponse = authService.refreshToken(request, response)
        return ApiResponse.buildResponse(
            authResponse,
            "Token refreshed successfully",
            HttpStatus.OK,
        )
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout from current session")
    fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ResponseEntity<ApiResponse<Unit>> {
        authService.logout(request, response)
        return ApiResponse.buildResponse(
            null,
            "Logged out successfully",
            HttpStatus.OK,
        )
    }

    @PostMapping("/logout-all")
    @Operation(summary = "Logout from all sessions")
    fun logoutAll(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ResponseEntity<ApiResponse<Unit>> {
        authService.logoutAllSessions(request, response)
        return ApiResponse.buildResponse(
            null,
            "Logged out from all sessions successfully",
            HttpStatus.OK,
        )
    }

    @GetMapping("/sessions")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get all active sessions for the current user")
    fun getSessions(): ResponseEntity<ApiResponse<List<Map<String, Any>>>> {
        val authentication =
            SecurityContextHolder.getContext().authentication
                ?: return ApiResponse.buildResponse(
                    null,
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED,
                )
        val email = authentication.name

        val sessions = authService.getUserActiveSessions(email)
        return ApiResponse.buildResponse(
            sessions,
            "Active sessions retrieved successfully",
            HttpStatus.OK,
        )
    }

    @PostMapping("/verify")
    @Operation(summary = "Verify user account with code")
    fun verifyAccount(
        @RequestParam code: String,
    ): ResponseEntity<ApiResponse<Unit>> {
        authService.verifyAccount(code)
        return ApiResponse.buildResponse(
            null,
            "Account verified successfully",
            HttpStatus.OK,
        )
    }

    @PostMapping("/change-email-request")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Request email change, sends verification to new email")
    @Throws(MessagingException::class)
    fun requestEmailChange(
        @RequestBody @Valid request: ChangeEmailRequestDto,
    ): ResponseEntity<ApiResponse<Unit>> {
        authService.requestEmailChange(request)
        return ApiResponse.buildResponse(
            null,
            "Email change verification sent to new email",
            HttpStatus.OK,
        )
    }

    @PostMapping("/verify-email-change")
    @Operation(summary = "Verify email change with code")
    fun verifyEmailChange(
        @RequestParam code: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ResponseEntity<ApiResponse<AuthResponseDto>> {
        val authResponse = authService.verifyEmailChange(code, response)
        return ApiResponse.buildResponse(
            authResponse,
            "Email changed successfully",
            HttpStatus.OK,
        )
    }

    @PostMapping("/change-password-request")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Request password change, sends verification email")
    @Throws(MessagingException::class)
    fun requestPasswordChange(
        @RequestBody @Valid request: ChangePasswordRequestDto,
    ): ResponseEntity<ApiResponse<Unit>> {
        authService.requestPasswordChange(request)
        return ApiResponse.buildResponse(
            null,
            "Password change verification sent to email",
            HttpStatus.OK,
        )
    }

    @PostMapping("/verify-password-change")
    @Operation(summary = "Verify password change with code")
    fun verifyPasswordChange(
        @RequestParam code: String,
    ): ResponseEntity<ApiResponse<Unit>> {
        authService.verifyPasswordChange(code)
        return ApiResponse.buildResponse(
            null,
            "Password changed successfully",
            HttpStatus.OK,
        )
    }

    @PostMapping("/reset-password-request")
    @Operation(summary = "Request password reset for a forgotten password")
    @Throws(MessagingException::class)
    fun requestPasswordReset(
        @RequestParam email: String,
    ): ResponseEntity<ApiResponse<Unit>> {
        authService.sendPasswordResetEmail(email)
        return ApiResponse.buildResponse(
            null,
            "Password reset instructions sent to email",
            HttpStatus.OK,
        )
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password using reset token")
    fun resetPassword(
        @RequestParam token: String,
        @RequestBody @Valid request: ResetPasswordRequestDto,
    ): ResponseEntity<ApiResponse<Unit>> {
        authService.resetPassword(token, request)
        return ApiResponse.buildResponse(
            null,
            "Password reset successfully",
            HttpStatus.OK,
        )
    }
}
