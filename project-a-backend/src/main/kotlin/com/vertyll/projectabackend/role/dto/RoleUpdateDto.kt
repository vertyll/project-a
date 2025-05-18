package com.vertyll.projectabackend.role.dto

import jakarta.validation.constraints.NotBlank

data class RoleUpdateDto(
    @field:NotBlank(message = "Name is required")
    val name: String = "",
    val description: String? = null,
)
