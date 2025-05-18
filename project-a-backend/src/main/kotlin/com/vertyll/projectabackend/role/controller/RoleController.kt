package com.vertyll.projectabackend.role.controller

import com.vertyll.projectabackend.common.response.ApiResponse
import com.vertyll.projectabackend.role.dto.RoleCreateDto
import com.vertyll.projectabackend.role.dto.RoleResponseDto
import com.vertyll.projectabackend.role.dto.RoleUpdateDto
import com.vertyll.projectabackend.role.enums.RoleType
import com.vertyll.projectabackend.role.service.RoleService
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
@RequestMapping("/roles")
@Tag(name = "Roles", description = "Role management APIs")
class RoleController(
    private val roleService: RoleService,
) {
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new role")
    fun createRole(
        @RequestBody @Valid dto: RoleCreateDto,
    ): ResponseEntity<ApiResponse<RoleResponseDto>> {
        val role = roleService.createRole(dto)
        return ApiResponse.buildResponse(
            role,
            "Role created successfully",
            HttpStatus.CREATED,
        )
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update existing role")
    fun updateRole(
        @PathVariable id: Long,
        @RequestBody @Valid dto: RoleUpdateDto,
    ): ResponseEntity<ApiResponse<RoleResponseDto>> {
        val role = roleService.updateRole(id, dto)
        return ApiResponse.buildResponse(
            role,
            "Role updated successfully",
            HttpStatus.OK,
        )
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get role by ID")
    fun getRole(
        @PathVariable id: Long,
    ): ResponseEntity<ApiResponse<RoleResponseDto>> {
        val role = roleService.getRoleById(id)
        return ApiResponse.buildResponse(
            role,
            "Role retrieved successfully",
            HttpStatus.OK,
        )
    }

    @GetMapping("/types")
    @Operation(summary = "Get all available role types")
    fun getAllRoleTypes(): ResponseEntity<ApiResponse<List<RoleType>>> {
        val types = RoleType.entries
        return ApiResponse.buildResponse(
            types,
            "Role types retrieved successfully",
            HttpStatus.OK,
        )
    }
}
