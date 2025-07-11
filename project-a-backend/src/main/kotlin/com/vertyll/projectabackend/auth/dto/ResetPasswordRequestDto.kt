package com.vertyll.projectabackend.auth.dto

import jakarta.validation.constraints.NotBlank

data class ResetPasswordRequestDto(
    @field:NotBlank(message = "New password is required")
    val newPassword: String = "",
)
