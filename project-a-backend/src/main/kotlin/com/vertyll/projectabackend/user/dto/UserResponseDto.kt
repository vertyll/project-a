package com.vertyll.projectabackend.user.dto

data class UserResponseDto(
    val id: Long = 0,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val roles: Set<String> = emptySet(),
    val enabled: Boolean = false,
)
