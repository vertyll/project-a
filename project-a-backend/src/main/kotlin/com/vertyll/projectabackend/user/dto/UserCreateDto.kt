package com.vertyll.projectabackend.user.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class UserCreateDto(
    @field:NotBlank(message = "First name is required")
    val firstName: String = "",
    @field:NotBlank(message = "Last name is required")
    val lastName: String = "",
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email should be valid")
    val email: String = "",
    @field:NotBlank(message = "Password is required")
    val password: String = "",
    val roleNames: Set<String> = emptySet(),
)
