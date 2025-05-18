package com.vertyll.projectabackend.user.controller

import com.vertyll.projectabackend.common.response.ApiResponse
import com.vertyll.projectabackend.user.dto.UserCreateDto
import com.vertyll.projectabackend.user.dto.UserResponseDto
import com.vertyll.projectabackend.user.dto.UserUpdateDto
import com.vertyll.projectabackend.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "User management APIs")
class UserController(
    private val userService: UserService,
) {
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new user")
    fun createUser(
        @RequestBody @Valid dto: UserCreateDto,
    ): ResponseEntity<ApiResponse<UserResponseDto>> {
        val user = userService.createUser(dto)
        return ApiResponse.buildResponse(
            user,
            "User created successfully",
            HttpStatus.CREATED,
        )
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update existing user")
    fun updateUser(
        @PathVariable id: Long,
        @RequestBody @Valid dto: UserUpdateDto,
    ): ResponseEntity<ApiResponse<UserResponseDto>> {
        val user = userService.updateUser(id, dto)
        return ApiResponse.buildResponse(
            user,
            "User updated successfully",
            HttpStatus.OK,
        )
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get user by ID")
    fun getUser(
        @PathVariable id: Long,
    ): ResponseEntity<ApiResponse<UserResponseDto>> {
        val user = userService.getUserById(id)
        return ApiResponse.buildResponse(
            user,
            "User retrieved successfully",
            HttpStatus.OK,
        )
    }
}
